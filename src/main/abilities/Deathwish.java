package main.abilities;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import main.model.Player;

public class Deathwish implements Cooldown{
    private Player character;
    private final ScheduledExecutorService scheduler;
    private final int DURATION = 30;
    private final int COOLDOWN = 180;
    private final int RAGECOST = 10;
    private boolean ready = true;
    
    public int getRageCost() {
        return RAGECOST;
    }

    public boolean isReady() {
        return ready;
    }
    
    private void cooldown() {
        ready = false;
        scheduler.schedule(() -> ready = true, COOLDOWN, TimeUnit.SECONDS);
    }
    
    public Deathwish(Player character) {
        this.character = character;
        scheduler = Executors.newScheduledThreadPool(2);
    }
    
    public void use() {
        cooldown();
        character.removeRage(RAGECOST);
        character.setDamageModifier(1.20);
        scheduler.schedule(() -> character.setDamageModifier(1.00), DURATION, TimeUnit.SECONDS);
    }
}
