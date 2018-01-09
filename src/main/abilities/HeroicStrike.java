package main.abilities;

import main.counters.DamageCounter;
import main.model.Player;
import main.utils.ATTACKRESULT;
import main.utils.Constants;
import main.utils.YellowAttackTable;

public class HeroicStrike implements Swing{
    Player character;
    DamageCounter dmgCounter;
    private final int RAGECOST = 15;
    
    public HeroicStrike(Player character, DamageCounter dmgCounter) {
        this.character = character;
        this.dmgCounter = dmgCounter;
    }
    
    @Override
    public AttackResultContainer perform() {
        int min = character.getMhWeaponDamageMin();
        long damage = Math.round(((min + (character.getMhWeaponDamageMax()-min)*Math.random())
                + ((character.getNormalizedMhSpeed()*character.getAp())/14)) + 157);
        YellowAttackTable aTable = new YellowAttackTable(character.getHit(), character.getCrit(), character.findMhWeaponSkill(), 
        		Constants.getRandomIntWithCeiling(1000), Constants.getRandomIntWithCeiling(1000));
        ATTACKRESULT result = aTable.yellowAttack();
        damage *= ATTACKRESULT.getDamageModifier(result, character.findMhWeaponSkill());
        character.removeRage(RAGECOST);
        dmgCounter.addDamage((int)damage);
        String stringToDisplay = "Heroic Strike " + result.toString() + " for " + damage;
        AttackResultContainer arContainer = new AttackResultContainer(result, stringToDisplay);
        return arContainer;
    }
}
