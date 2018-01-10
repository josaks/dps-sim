package main.utils;

public class YellowAttackTable {
	private final int hit;
    private final int crit;
    private final int mhWeaponSkill;
    private int bossDefSkill;
    
	public YellowAttackTable(int hit, int crit, int mhWeaponSkill) {
		this.hit = hit;
		this.crit = crit;
		this.mhWeaponSkill = mhWeaponSkill;
	}
    
	public ATTACKRESULT attack(int rollForHit, int rollForCrit){
	    if(rollForHit <= 0 || rollForCrit <= 0) throw new IllegalArgumentException("rolls can not be 0 or less");
        if(rollForHit > 1000 || rollForCrit > 1000) throw new IllegalArgumentException("rolls can not be more than 1000");
	    
    	int dodgeChance = 56;
        int weaponSkill = mhWeaponSkill;
        int missChance;
        if(Constants.bossDefSkill - weaponSkill <= 10) missChance = 50 - hit + (Constants.bossDefSkill - weaponSkill);
        else missChance = 70 - hit + (Constants.bossDefSkill - weaponSkill - 10) * 4;
        
        if(rollForHit > 0 && rollForHit <= missChance) return ATTACKRESULT.MISS;
        if(rollForHit > missChance && rollForHit <= (missChance + dodgeChance)) return ATTACKRESULT.DODGE;
        if(rollForCrit > crit) return ATTACKRESULT.CRIT;
        else return ATTACKRESULT.HIT;
    }
}
