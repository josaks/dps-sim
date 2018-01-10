package main.abilities;

import main.model.Player;
import main.utils.ATTACKRESULT;
import main.utils.Constants;
import main.utils.WhiteAttackTable;
import main.utils.YellowAttackTable;

public class HeroicStrike implements Swing{
    Player character;
    private final int RAGECOST = 15;
    YellowAttackTable aTable;
    
    public HeroicStrike(Player character, YellowAttackTable aTable) {
        this.character = character;
        this.aTable = aTable;
    }
    
    @Override
    public AttackResultContainer perform() {
        int min = character.getMhWeaponDamageMin();
        long damage = Math.round(((min + (character.getMhWeaponDamageMax()-min)*Math.random())
                + ((character.getNormalizedMhSpeed()*character.getAp())/14)) + 157);
        ATTACKRESULT result = aTable.attack(Constants.getRandomIntWithCeiling(1000),
                Constants.getRandomIntWithCeiling(1000));
        damage *= ATTACKRESULT.getDamageModifier(result, character.findMhWeaponSkill());
        String stringToDisplay = "Heroic Strike " + result.toString() + " for " + damage;
        AttackResultContainer arContainer = new AttackResultContainer(result, stringToDisplay, (int)damage);
        return arContainer;
    }
    
    public int getRageCost() {
        return RAGECOST;
    }
}
