package main.utils;

public enum ATTACKRESULT {
	MISS, DODGE, PARRY, GLANCING, BLOCK, CRIT, HIT;
    
    public static double getResultDamageModifier(ATTACKRESULT result, int weaponSkill) {
        if(result.equals(ATTACKRESULT.CRIT)) return 2.2;
        else if(result.equals(ATTACKRESULT.HIT)) return 1;
        else if(result.equals(ATTACKRESULT.GLANCING)) {
            double reduction = ((Constants.bossDefSkill - 5 - weaponSkill) * 0.03); 
            if(reduction > 0.30) reduction = 0.30;
            return (1 - reduction);
        }
        else return 0;
    }
    
    public static boolean compareWithMany(ATTACKRESULT first, ATTACKRESULT next, ATTACKRESULT ... rest)
    {
        if(first.equals(next))
            return true;
        for(int i = 0; i < rest.length; i++)
        {
            if(first.equals(rest[i]))
                return true;
        }
        return false;
    }
}
