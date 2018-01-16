package main.abilities;

import main.model.Player;
import main.utils.ATTACKRESULT;
import main.utils.Constants;
import main.utils.AttackTable;

public class HeroicStrike implements Swing{
    private final int RAGECOST = 15;

    @Override
    public AttackResultContainer perform(Player character, AttackTable aTable) {
        int min = character.getMhWeaponDamageMin();
        long damage = Math.round(((min + (character.getMhWeaponDamageMax()-min)*Math.random())
                + ((character.getNormalizedMhSpeed()*character.getAp())/14)) + 157);
        ATTACKRESULT result = aTable.yellowAttack(Constants.getRandomIntWithCeiling(1000),
                Constants.getRandomIntWithCeiling(1000));
        damage *= ATTACKRESULT.getResultDamageModifier(result, character.findMhWeaponSkill());
        damage *= character.getDamageModifier();
        String stringToDisplay = "Heroic Strike " + result.toString() + " for " + damage;
        AttackResultContainer arContainer = new AttackResultContainer(result, stringToDisplay, (int)damage);
        return arContainer;
    }
    
    public int getRageCost() {
        return RAGECOST;
    }
}
