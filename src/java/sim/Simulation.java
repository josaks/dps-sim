package sim;


import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.lang3.time.StopWatch;
import static java.util.concurrent.TimeUnit.*;
import abilities.Ability;
import abilities.AttackResultContainer;
import abilities.Bloodthirst;
import abilities.GlobalCooldown;
import abilities.HeroicStrike;
import abilities.MainhandAttack;
import abilities.Swing;
import abilities.Whirlwind;
import counters.DamageCounter;
import model.Player;
import model.Proc;
import utils.ATTACKRESULT;
import utils.AttackTable;

//represents a DPS simulation
public class Simulation {
	//how long the simulation took before being stopped
	private StopWatch stopwatch;
	//how much damage was done
	private DamageCounter damageCounter;
	//player character used in this simulation
	private Player character;
	//mh swing timer
	private ScheduledFuture<?> mhSwing;
	//oh swing timer
	private ScheduledFuture<?> ohSwing;
	//ability user timer
	private ScheduledFuture<?> abilityUser;
	//scheduler
	private final ScheduledExecutorService scheduler;
	//thread safe queue used for passing the combatlog to the controller/gui
	Queue<String> combatLogQueue = new ConcurrentLinkedQueue<String>();
	
	public Simulation(Player character) {
		//setting this simulations Player
		this.character = character;
		this.damageCounter = new DamageCounter();
		stopwatch = new StopWatch();
		//creating scheduler
        scheduler = Executors.newScheduledThreadPool(3);
	}
	
	public Queue<String> getCombatLogQueue() {
		return combatLogQueue;
	}
	
	public int getRage() {
		return character.getRage();
	}
	
	public int getDamage() {
		return damageCounter.getDamage();
	}
	
	public long getTime() {
		long time;
		if(stopwatch.isStarted()) {
			stopwatch.split();
			time = stopwatch.getTime();
			stopwatch.unsplit();
		}
		else time = stopwatch.getTime();
		return time;
	}
	
	private void display(String message) {
		LocalDateTime ldt = LocalDateTime.now();
		String datetime = ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond() + "." + ldt.getNano() / 1000000;
		System.out.println(datetime + " > " + message);
		combatLogQueue.add(datetime + " > " + message + System.lineSeparator());
	}
	
	//starts this simulation
	public void begin() {
		stopwatch.start();
		
		//cannot schedule task with period of 0
		if(character.getMainhandSpeed() != 0) {
		  //schedule mh swing
	        this.mhSwing = scheduler.scheduleAtFixedRate(new MainhandSwing(), 0, character.getMainhandSpeed(), MILLISECONDS);
		}
		
		if(character.getOffhandSpeed() != 0) {
		  //schedule oh swing
	        this.ohSwing = scheduler.scheduleAtFixedRate(new OffhandSwing(), 0, character.getOffhandSpeed(), MILLISECONDS);
		}
		
		//schedule abilityuser, attempts to use ability every 0,01s
		this.abilityUser = scheduler.scheduleAtFixedRate(new AbilityUser(), 0, 10, MILLISECONDS);
	}
	
	public void stop() {
		if(mhSwing != null) mhSwing.cancel(false);
		if(ohSwing != null) ohSwing.cancel(false);
		abilityUser.cancel(false);
		stopwatch.reset();
		damageCounter.reset();
		character.resetRage();
		combatLogQueue.clear();
		character.setFlurryStacks(0);
	}
	
	private class AbilityUser implements Runnable{
	    private List<Ability> abilityList = new LinkedList<Ability>();
	    private GlobalCooldown gcd = new GlobalCooldown();
	    
	    private AbilityUser() {
	        int mhWeapSkill = character.findMhWeaponSkill();
	        AttackTable aTable = new AttackTable(character.getHit(), character.getCrit(), mhWeapSkill);
	        abilityList.add(new Bloodthirst(character.getAp(), aTable, damageCounter, character.findMhWeaponSkill()));
	        abilityList.add(new Whirlwind(character.getMhWeaponDamageMin(), character.getMhWeaponDamageMax(), aTable, damageCounter, mhWeapSkill));
	    }
	    
	    public void run() {
	        if(gcd.isReady()) {
	            ATTACKRESULT result = ATTACKRESULT.MISS;
	            for(Ability a : abilityList) {
	                if(a.isReady()) {
	                    if(character.removeRage(a.getRageCost())) {
	                        AttackResultContainer arContainer = a.perform();
	                        result = arContainer.getAttackResult();
	                        display(arContainer.getAttackResultString());
	                        gcd.use();
	                    }
	                    break;
	                }
	            }
	            compareAttackResult(result);
            }
	    }
	}
	
    private class MainhandSwing implements Runnable{
        public void run() {
            ATTACKRESULT result;
            Swing swing;
            if(character.getRage() > 80) swing = new HeroicStrike(character, damageCounter);
            else swing = new MainhandAttack(character, damageCounter);
            AttackResultContainer arContainer = swing.perform();
            result = arContainer.getAttackResult();
            display(arContainer.getAttackResultString());
            compareAttackResult(result);
        }
    }
	
	public void gainFlurry() {
		//if flurry is already active we don't want to replace the swingtimers
		if(character.getFlurryStacks() == 0) {
			character.setFlurryStacks(3);
			//clear mh and oh timers and schedule new flurryfied mhswing and ohswing
			this.mhSwing.cancel(false);
			this.mhSwing = scheduler.scheduleAtFixedRate(new FlurryfiedMainHandSwing(),
			        (int)(character.getMainhandSpeed() / 1.30), (int)(character.getMainhandSpeed() / 1.30), MILLISECONDS);
			//do same with ohswingtimer
		}
		else {
			character.setFlurryStacks(3);
		}
	}
	
	private class FlurryfiedMainHandSwing implements Runnable{
		public void run() {
			character.consumeFlurryStack();
			ATTACKRESULT result;
			Swing swing;
            if(character.getRage() > 80) swing = new HeroicStrike(character, damageCounter);
            else swing = new MainhandAttack(character, damageCounter);
            AttackResultContainer arContainer = swing.perform();
            result = arContainer.getAttackResult();
            display(arContainer.getAttackResultString());
			compareAttackResult(result);
			if(character.getFlurryStacks() == 0) {
			    mhSwing.cancel(false);
			    mhSwing = scheduler.scheduleAtFixedRate(new MainhandSwing(), 0, character.getMainhandSpeed(), MILLISECONDS);
				//ohSwingTimer.schedule(new OffhandSwing(), weapon.getOffhandSpeed(), weapon.getOffhandSpeed());
			}
		}
	}
	
	private class OffhandSwing implements Runnable{
	    public void run() {
	        throw new UnsupportedOperationException("offhandswing not implemented yet");
	    }
	}
	
	private void compareAttackResult(ATTACKRESULT result) {
	    if(ATTACKRESULT.compareWithMany(result, ATTACKRESULT.HIT, ATTACKRESULT.CRIT, ATTACKRESULT.GLANCING)) {
            windfury();
            countProcDamage(character.getMhProc());
            if(result.equals(ATTACKRESULT.CRIT)) gainFlurry();
        }
	}
	
	//adds the damage if the proc happened(int returned from proc() method is not 0) and displays it in combatlog
	private void countProcDamage(Proc proc) {
		//checking if proc is set to something, if not dont do anything
		if(proc != null) {
			int procDamage = proc.proc();
			if(procDamage > 0) {
				damageCounter.addDamage(procDamage);
				display(proc.getName() + " procced for " + procDamage);
			}
		}
	}
	
	private boolean windfuryOn = true;
	
	public void setWindfury(boolean b) {
	    windfuryOn = b;
	}
	
	private synchronized void windfury() {
	    if(windfuryOn) {
	        if(Math.random() < 0.20) {
	            display("Windfury proc!");
	            MainhandAttack mhAtk = new MainhandAttack(character, damageCounter);
	               AttackResultContainer arContainer = mhAtk.perform();
	               display(arContainer.getAttackResultString());
	        } 
	    }
	}
	
	public static boolean compareWithMany(String first, String next, String ... rest)
	{
	    if(first.equalsIgnoreCase(next))
	        return true;
	    for(int i = 0; i < rest.length; i++)
	    {
	        if(first.equalsIgnoreCase(rest[i]))
	            return true;
	    }
	    return false;
	}
}
