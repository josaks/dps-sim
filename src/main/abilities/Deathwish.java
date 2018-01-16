package main.abilities;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import main.model.Player;

public class Deathwish implements Cooldown{
    private Player character;
    private ScheduledFuture<?> actionHandle;
    private ScheduledFuture<?> cooldownHandle;
    private final ScheduledExecutorService scheduler;
    private int duration = 30;
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
        cooldownHandle = scheduler.schedule(() -> ready = true, COOLDOWN, TimeUnit.SECONDS);
    }
    
    @Override
    public void reset() {
        if(cooldownHandle != null) cooldownHandle.cancel(true);
        if(actionHandle != null) actionHandle.cancel(true);
        ready = true;
        character.setDamageModifier(1.00);
    }
    
    public Deathwish(Player character) {
        this.character = character;
        scheduler = Executors.newScheduledThreadPool(2);
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public int getCooldown() {
        return COOLDOWN;
    }
    
    public boolean willBeReadyAgain(int durationInSeconds, int elapsedTime) {
        int remainingTime = durationInSeconds - elapsedTime;
        if(remainingTime > COOLDOWN) return true;
        return false;
    }
    
    public void use() {
        cooldown();
        character.removeRage(RAGECOST);
        character.setDamageModifier(character.getDamageModifier() + 0.20);
        actionHandle = scheduler.schedule(() -> character.setDamageModifier(character.getDamageModifier() - 0.20), duration, TimeUnit.SECONDS);
    }

    
}
