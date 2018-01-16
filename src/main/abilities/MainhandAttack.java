package main.abilities;

import main.model.Player;
import main.utils.ATTACKRESULT;
import main.utils.AttackTable;
import main.utils.Constants;

public class MainhandAttack implements Swing{
    @Override
    public AttackResultContainer perform(Player character, AttackTable aTable) {
        int min = character.getMhWeaponDamageMin();
        long damage = Math.round(((min + (character.getMhWeaponDamageMax()-min)*Math.random())
                + ((character.getNormalizedMhSpeed()*character.getAp())/14)));
        ATTACKRESULT result = aTable.mhAttack(Constants.getRandomIntWithCeiling(1000));
        damage *= ATTACKRESULT.getResultDamageModifier(result, character.findMhWeaponSkill());
        damage *= character.getDamageModifier();
        character.addRageByDamage((int)damage);
        String stringToDisplay = "MH " + result.toString() + " for " + damage;
        AttackResultContainer arContainer = new AttackResultContainer(result, stringToDisplay, (int)damage);
        return arContainer;
    }
    
    public int getRageCost() {
        return 0;
    }
}
