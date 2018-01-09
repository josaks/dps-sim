package main.abilities;

import main.counters.DamageCounter;
import main.model.Player;
import main.utils.ATTACKRESULT;
import main.utils.WhiteAttackTable;
import main.utils.Constants;

public class MainhandAttack implements Swing{
    Player character;
    DamageCounter dmgCounter;
    
    public MainhandAttack(Player character, DamageCounter dmgCounter) {
        this.character = character;
        this.dmgCounter = dmgCounter;
    }
    
    @Override
    public AttackResultContainer perform() {
        int min = character.getMhWeaponDamageMin();
        long damage = Math.round(((min + (character.getMhWeaponDamageMax()-min)*Math.random())
                + ((character.getNormalizedMhSpeed()*character.getAp())/14)));
        WhiteAttackTable aTable = new WhiteAttackTable(character.getHit(), character.getCrit(), character.findMhWeaponSkill(), 
        		Constants.getRandomIntWithCeiling(1000), Constants.getRandomIntWithCeiling(1000));
        ATTACKRESULT result = aTable.mhAttack();
        damage *= ATTACKRESULT.getDamageModifier(result, character.findMhWeaponSkill());
        character.addRageByDamage((int)damage);
        dmgCounter.addDamage((int)damage);
        String stringToDisplay = "MH " + result.toString() + " for " + damage;
        AttackResultContainer arContainer = new AttackResultContainer(result, stringToDisplay);
        return arContainer;
    }
}
