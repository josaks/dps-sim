package main.model;

import main.counters.RageCounter;
import main.utils.WEAPONTYPE;

//contains all information about character stats, like hit, crit, ap
//also contains a reference to what weapon is in use
public class Player {
	
	private RageCounter rage;
	private int hit;
	private int crit;
	private int ap;
	private int axeSkill;
	private int maceSkill;
	private int swordSkill;
	private int daggerSkill;
	private Weapon weapon;
	private int flurryStacks;
	
	public Proc getMhProc() {
		return weapon.getMhProc();
	}
	public Proc getOhProc() {
		return weapon.getOhProc();
	}

	/*public Player(int hit, int crit, int ap, Weapon weapon) {
		this(hit,crit,ap,weapon,300,300,300,300, 0);
	}*/
	
	public int getFlurryStacks() {
		return flurryStacks;
	}
	public synchronized void setFlurryStacks(int flurryStacks) {
		this.flurryStacks = flurryStacks;
	}
	public synchronized void consumeFlurryStack() {
		this.flurryStacks--;
	}

	public int findMhWeaponSkill() {
		if(weapon.getMhWeaponType().equals(WEAPONTYPE.SWORD)) return swordSkill;
		else if(weapon.getMhWeaponType().equals(WEAPONTYPE.AXE)) return axeSkill;
		else if (weapon.getMhWeaponType().equals(WEAPONTYPE.MACE)) return maceSkill;
		else return daggerSkill;
	}
	
	public int findOhWeaponSkill() {
		if(weapon.getMhWeaponType().equals(WEAPONTYPE.SWORD)) return swordSkill;
		else if(weapon.getMhWeaponType().equals(WEAPONTYPE.AXE)) return axeSkill;
		else if (weapon.getMhWeaponType().equals(WEAPONTYPE.MACE)) return maceSkill;
		else return daggerSkill;
	}
	
	//only builder should call the constructor
	private Player(PlayerBuilder builder) {
        this.hit = builder.hit;
        this.crit = builder.crit;
        this.ap = builder.ap;
        this.axeSkill = builder.axeSkill;
        this.maceSkill = builder.maceSkill;
        this.swordSkill = builder.swordSkill;
        this.daggerSkill = builder.daggerSkill;
        this.rage = builder.rageCounter;
        this.flurryStacks = builder.flurryStacks;
    }
	
	public static Hit builder() {
	    return new PlayerBuilder();
	}
	
	public static class PlayerBuilder implements Hit, Crit, Ap, Build{
	    //mandatory
	    private int hit;
	    private int crit;
	    private int ap;
	     
	    @Override
        public Crit hit(int hit) {
            this.hit = hit;
            return this;
        }
	    @Override
        public Ap crit(int crit) {
            this.crit = crit;
            return this;
        }
	    @Override
        public Build ap(int ap) {
            this.ap = ap;
            return this;
        }
        
	    //non-mandatory
	    private RageCounter rageCounter = new RageCounter();
	    private int flurryStacks = 0;
	    private int axeSkill = 300;
	    private int maceSkill = 300;
	    private int swordSkill = 300;
	    private int daggerSkill = 300;
	    
        @Override
        public Build rageCounter(RageCounter rageCounter) {
            this.rageCounter = rageCounter;
            return this;
        }
        @Override
        public Build flurryStacks(int flurryStacks) {
            this.flurryStacks = flurryStacks;
            return this;
        }
        @Override
        public Build axeSkill(int axeSkill) {
            this.axeSkill = axeSkill;
            return this;
        }
        @Override
        public Build maceSkill(int maceSkill) {
            this.maceSkill = maceSkill;
            return this;
        }
        @Override
        public Build swordSkill(int swordSkill) {
            this.swordSkill = swordSkill;
            return this;
        }
        @Override
        public Build daggerSkill(int daggerSkill) {
            this.daggerSkill = daggerSkill;
            return this;
        }
        @Override
        public Player build(){
            return new Player(this);
        }
	}
	
	public interface Hit{
	    public Crit hit(int hit);
	}
	public interface Crit{
	    public Ap crit(int crit);
	}
	public interface Ap{
	    public Build ap(int ap);
	}

	public interface Build{
	    public Build rageCounter(RageCounter rageCounter);
	    public Build flurryStacks(int flurryStacks);
	    public Build axeSkill(int axeSkill);
	    public Build maceSkill(int maceSkill);
	    public Build swordSkill(int swordSkill);
	    public Build daggerSkill(int daggerSkill);
	    public Player build();
	}
	
	
	public int getDaggerSkill() {
		return daggerSkill;
	}
	public int getAxeSkill() {
		return axeSkill;
	}
	public int getMaceSkill() {
		return maceSkill;
	}
	public int getSwordSkill() {
		return swordSkill;
	}
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	public int getHit() {
		return hit;
	}
	public int getCrit() {
		return crit;
	}
	public int getAp() {
		return ap;
	}
	public void addRageByDamage(int damage) {
		this.rage.addRageByDamage(damage);
	}
	public void addRage(int rage) {
		this.rage.addRage(rage);
	}
	public boolean removeRage(int rage) {
		return this.rage.removeRage(rage);
	}
	public int getRage() {
		return rage.getRage();
	}
	public int getMhWeaponDamageMin() {
		return weapon.getMhWeaponDamageMin();
	}
	public int getMhWeaponDamageMax() {
		return weapon.getMhWeaponDamageMax();
	}
	public int getMainhandSpeed() {
		return weapon.getMainhandSpeed();
	}
	public int getOffhandSpeed() {
		return weapon.getOffhandSpeed();
	}
	public int getOhWeaponDamageMin() {
		return weapon.getOhWeaponDamageMin();
	}
	public int getOhWeaponDamageMax() {
		return weapon.getMhWeaponDamageMax();
	}
	public double getNormalizedMhSpeed() {
		return weapon.getNormalizedMhSpeed();
	}
	public double getNormalizedOhSpeed() {
		return weapon.getNormalizedOhSpeed();
	}
	public WEAPONTYPE getMhWeaponType() {
		return weapon.getMhWeaponType();
	}
	public WEAPONTYPE getohWeaponType() {
		return weapon.getohWeaponType();
	}
	public void resetRage() {
		rage.reset();
	}
}
