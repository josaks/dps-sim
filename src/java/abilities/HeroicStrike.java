package abilities;

import counters.DamageCounter;
import model.Player;
import utils.ATTACKRESULT;
import utils.AttackTable;

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
        AttackTable aTable = new AttackTable(character.getHit(), character.getCrit(), character.findMhWeaponSkill());
        ATTACKRESULT result = aTable.yellowAttackRoll();
        damage *= ATTACKRESULT.getDamageModifier(result, character.findMhWeaponSkill());
        character.removeRage(RAGECOST);
        dmgCounter.addDamage((int)damage);
        String stringToDisplay = "Heroic Strike " + result.toString() + " for " + damage;
        AttackResultContainer arContainer = new AttackResultContainer(result, stringToDisplay);
        return arContainer;
    }
}
