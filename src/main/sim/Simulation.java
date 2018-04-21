package main.sim;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import main.utils.ATTACKRESULT;
import main.utils.AttackTable;

import org.apache.commons.lang3.time.StopWatch;
import static java.util.concurrent.TimeUnit.*;

import main.abilities.Ability;
import main.abilities.AttackResultContainer;
import main.abilities.Bloodthirst;
import main.abilities.Deathwish;
import main.abilities.GlobalCooldown;
import main.abilities.HeroicStrike;
import main.abilities.MainhandAttack;
import main.abilities.Swing;
import main.abilities.Whirlwind;
import main.counters.DamageCounter;
import main.model.Player;
import main.model.Proc;

//represents a DPS simulation
public class Simulation {
	//how long the simulation has been running for
	private StopWatch stopwatch;
	//how much damage was done
	private DamageCounter damageCounter;
	//player character used in this simulation
	private Player character;
	//mh swing handle
	private ScheduledFuture<?> mhSwing;
	//oh swing handle
	private ScheduledFuture<?> ohSwing;
	//abilityuser handle
	private ScheduledFuture<?> abilityUser;
	//scheduler
	private final ScheduledExecutorService scheduler;
	//thread safe queue used for passing the combatlog to the controller/gui
	Queue<String> combatLogQueue = new ConcurrentLinkedQueue<String>();
	//how long the simulation should run for
	private int durationInSeconds= 0;
	//health of the boss being attacked
	private int bossHealth = 0;
	
	AttackTable aTable;
	
	public int getBossHealth() {
        return bossHealth;
    }
    public void setBossHealth(int bossHealth) {
        this.bossHealth = bossHealth;
    }
    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }
	public int getDurationInSeconds() {
	    return durationInSeconds;
	}
	
	private int minimumRageForHS = 70;
	
	public void setMinimumRageForHS(int minimumRageForHS) {
        this.minimumRageForHS = minimumRageForHS;
    }
	
    public Simulation(Player character) {
		//setting this simulations Player
		this.character = character;
		this.damageCounter = new DamageCounter();
		stopwatch = new StopWatch();
		//creating scheduler
        scheduler = Executors.newScheduledThreadPool(3);
        dw = new Deathwish(character);
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
	
	public StopWatch getStopWatch() {
		return stopwatch;
	}
	
	private void display(String message) {
		LocalDateTime ldt = LocalDateTime.now();
		String datetime = ldt.getHour() + ":" + ldt.getMinute() + ":" + ldt.getSecond() + "." + ldt.getNano() / 1000000;
		System.out.println(datetime + " > " + message);
		combatLogQueue.add(datetime + " > " + message + System.lineSeparator());
	}
	
	//starts this simulation
	public void begin() {
	    aTable = new AttackTable(character.getHit(), character.getCrit(), character.findMhWeaponSkill(), character.findOhWeaponSkill());
	    
		if(!stopwatch.isStarted()) stopwatch.start();
		
		//cannot schedule task with period of 0
		if(character.getMainhandSpeed() != 0) {
		    //schedule mh swing
	        this.mhSwing = scheduler.scheduleAtFixedRate(new MainhandSwing(), 0, character.getMainhandSpeed(), MILLISECONDS);
		}
		
		if(character.getOffhandSpeed() != 0) {
		    //schedule oh swing
	        this.ohSwing = scheduler.scheduleAtFixedRate(new OffhandSwing(), 0, character.getOffhandSpeed(), MILLISECONDS);
		}
		
		List<Ability> abilityList = new LinkedList<Ability>();
		abilityList.add(new Bloodthirst());
        abilityList.add(new Whirlwind());
		
		//schedule abilityuser, attempts to use ability every 0,01s
		this.abilityUser = scheduler.scheduleAtFixedRate(new AbilityUser(abilityList), 0, 10, MILLISECONDS);
		
		/*List<Cooldown> cooldowns = new LinkedList<Cooldown>();
		cooldowns.add(new Deathwish(character));
		this.cooldownUser = scheduler.scheduleAtFixedRate(new CooldownUser(cooldowns), 0, 10, MILLISECONDS);*/
	}
	
	public void stop() {
		if(mhSwing != null) mhSwing.cancel(false);
		if(ohSwing != null) ohSwing.cancel(false);
		if(abilityUser != null) abilityUser.cancel(false);
		stopwatch.reset();
		damageCounter.reset();
		character.resetRage();
		combatLogQueue.clear();
		character.setFlurryStacks(0);
		
		//reset cooldowns
		dw.reset();
	}
	
	/*private class CooldownUser implements Runnable{
	    List<Cooldown> cdList;
	    int elapsedTime;
	    
	    protected CooldownUser(List<Cooldown> cdList) {
	        this.cdList = cdList;
	    }
	    
	    public void run() {
	        elapsedTime = (int)(Constants.getTime(stopwatch) / 1000);
	        for(Cooldown cd : cdList) {
	            if(cd.willBeReadyAgain(durationInSeconds, elapsedTime)) {
	                if(cd.isReady()) {
	                    if(character.removeRage(cd.getRageCost())) {
	                        cd.use();
	                    }
	                }
	            }
	            //if less than say 3m of fight remaining dont use dw till last 30s
	            else {
	                
	            }
	        }
	    }
	}*/
	
	private Deathwish dw;
	
	public boolean useDeathwish() {
	    if(dw.isReady()) {
	        dw.use();
	        display("Deathwish activated");
	        return true;
	    }
	    else {
	        display("Deathwish failed to activate, is on cooldown");
	        return false;
	    }
	}
	
	private class AbilityUser implements Runnable{
	    private List<Ability> abilityList;
	    private GlobalCooldown gcd = new GlobalCooldown();
	    
	    private AbilityUser(List<Ability> abilityList) {
	        this.abilityList = abilityList;
	    }
	    
	    public void run() {
	        if(gcd.isReady()) {
	            for(Ability a : abilityList) {
	                if(a.isReady()) {
	                    if(character.removeRage(a.getRageCost())) {
	                        registerAttackResult(a.perform(character, aTable));
	                        gcd.use();
	                    }
	                    break;
	                }
	            }
            }
	    }
	}
	
    private class MainhandSwing implements Runnable{
        public void run() {
            Swing swing;
            if(character.getRage() > minimumRageForHS) swing = new HeroicStrike();
            else swing = new MainhandAttack();
            if(character.removeRage(swing.getRageCost())) registerAttackResult(swing.perform(character, aTable));
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
		else character.setFlurryStacks(3);
	}
	
	private class FlurryfiedMainHandSwing implements Runnable{
		public void run() {
			character.consumeFlurryStack();
			Swing swing;
            if(character.getRage() > minimumRageForHS) swing = new HeroicStrike();
            else swing = new MainhandAttack();
            if(character.removeRage(swing.getRageCost())) {
                registerAttackResult(swing.perform(character, aTable));
            }
			if(character.getFlurryStacks() == 0) {
			    mhSwing.cancel(false);
			    mhSwing = scheduler.scheduleAtFixedRate(new MainhandSwing(), character.getMainhandSpeed(), character.getMainhandSpeed(), MILLISECONDS);
				//ohSwingTimer.schedule(new OffhandSwing(), weapon.getOffhandSpeed(), weapon.getOffhandSpeed());
			}
		}
	}
	
	private class OffhandSwing implements Runnable{
	    public void run() {
	        throw new UnsupportedOperationException("offhandswing not implemented yet");
	    }
	}
	
	private void registerAttackResult(AttackResultContainer arc) {
	    registerDamage(arc.getDamage(), arc.getAttackResultString());
        compareAttackResult(arc.getAttackResult());
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
			    registerDamage(procDamage, proc.getName() + " procced for " + procDamage);
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
	            MainhandAttack mhAtk = new MainhandAttack();
	            AttackResultContainer arContainer = mhAtk.perform(character, aTable);
	            registerDamage(arContainer.getDamage(), arContainer.getAttackResultString());
	            if(arContainer.getAttackResult().equals(ATTACKRESULT.CRIT)) gainFlurry();
	            countProcDamage(character.getMhProc());
	        } 
	    }
	}
	
	public void registerDamage(int damage, String displayString) {
	    damageCounter.addDamage((int)(damage * character.getDamageModifier()));
	    display(displayString);
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

