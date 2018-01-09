package abilities;

import counters.DamageCounter;
import model.Player;
import utils.ATTACKRESULT;
import utils.AttackTable;

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
        AttackTable aTable = new AttackTable(character.getHit(), character.getCrit(), character.findMhWeaponSkill());
        ATTACKRESULT result = aTable.mhAttackRoll();
        damage *= ATTACKRESULT.getDamageModifier(result, character.findMhWeaponSkill());
        character.addRageByDamage((int)damage);
        dmgCounter.addDamage((int)damage);
        String stringToDisplay = "MH " + result.toString() + " for " + damage;
        AttackResultContainer arContainer = new AttackResultContainer(result, stringToDisplay);
        return arContainer;
    }
}
