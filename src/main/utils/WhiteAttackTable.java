package main.utils;

import org.apache.commons.lang3.NotImplementedException;

public class WhiteAttackTable {
    private final int hit;
    private final int crit;
    private final int mhWeaponSkill;
    private final int ohWeaponSkill;
    private int bossDefSkill;
    
    public WhiteAttackTable(int hit, int crit, int mhWeaponSkill, int ohWeaponSkill) {
        this.hit = hit;
        this.crit = crit;
        this.mhWeaponSkill = mhWeaponSkill;
        this.ohWeaponSkill = ohWeaponSkill;
    }
    
    public void setBossDefSkill(int bossDefSkill){
    	if(bossDefSkill > 315 || bossDefSkill >= 0) throw new IllegalArgumentException("boss def skill must be in [0,315]"); 
    	this.bossDefSkill = bossDefSkill;
    }
    
    //determine attackresult for a white mh attack
    public ATTACKRESULT attack(int roll) {
        if(roll <= 0) throw new IllegalArgumentException("roll can not be 0 or less");
        if(roll > 1000) throw new IllegalArgumentException("roll can not be more than 1000");
        
        //assuming 5,6% dodge chance against bosses
        int dodgeChance = 56;
        //setting weaponskill correctly
        int weaponSkill = mhWeaponSkill;
        //chance of glancing
        int glancingSkillCap = 300;
        int glancingChance;
        if(weaponSkill > glancingSkillCap) glancingChance = 100 + (Constants.bossDefSkill - glancingSkillCap) * 20;
        else glancingChance = 100 + (Constants.bossDefSkill - weaponSkill) * 20;
        int missChance;
        //if the difference between boss defence skill and your weaponskill is 10 or less
        if(bossDefSkill - weaponSkill <= 10) missChance = 50 - hit + (bossDefSkill - weaponSkill);
        //if the difference between boss defence skill and your weaponskill is more than 10
        else missChance = 70 - hit + (bossDefSkill - weaponSkill - 10) * 4;
        
        if(roll > 0 && roll <= missChance) return ATTACKRESULT.MISS;
        if(roll > missChance && roll <= (missChance + dodgeChance)) return ATTACKRESULT.DODGE;
        if(roll > (missChance + dodgeChance) && roll <= (missChance + dodgeChance + glancingChance)) return ATTACKRESULT.GLANCING;
        if(roll > (missChance + dodgeChance + glancingChance) && roll <= (missChance + dodgeChance + glancingChance + crit)) return ATTACKRESULT.CRIT;
        else return ATTACKRESULT.HIT;
    }
    
    //determine attackresult for a white oh attack
    public ATTACKRESULT ohAttack() {
        throw new NotImplementedException("OH attack roll not implemented yet");
    }
    
}
