package test.model;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import main.counters.RageCounter;
import main.model.Player;

public class PlayerTest {
    @Test
    public void toStringTest() {
        
        Player p = Player.builder()
                .hit(8)
                .crit(20)
                .ap(1400)
                .axeSkill(300)
                .daggerSkill(250)
                .maceSkill(140)
                .swordSkill(139)
                .rageCounter(new RageCounter())
                .flurryStacks(1)
                .build();
        
        assertEquals("",
                p.toString());
    }
}
