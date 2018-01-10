package main.abilities;

import main.model.Player;
import main.utils.ATTACKRESULT;
import main.utils.WhiteAttackTable;
import main.utils.Constants;

public class MainhandAttack implements Swing{
    Player character;
    WhiteAttackTable aTable;
    
    public MainhandAttack(Player character, WhiteAttackTable aTable) {
        this.character = character;
        this.aTable = aTable;
    }
    
    @Override
    public AttackResultContainer perform() {
        int min = character.getMhWeaponDamageMin();
        long damage = Math.round(((min + (character.getMhWeaponDamageMax()-min)*Math.random())
                + ((character.getNormalizedMhSpeed()*character.getAp())/14)));
        ATTACKRESULT result = aTable.attack(Constants.getRandomIntWithCeiling(1000));
        damage *= ATTACKRESULT.getDamageModifier(result, character.findMhWeaponSkill());
        character.addRageByDamage((int)damage);
        String stringToDisplay = "MH " + result.toString() + " for " + damage;
        AttackResultContainer arContainer = new AttackResultContainer(result, stringToDisplay, (int)damage);
        return arContainer;
    }
    
    public int getRageCost() {
        return 0;
    }
}
