package main.abilities;

public interface Cooldown {
    public void use();
    public boolean isReady();
    public int getRageCost();
    public int getCooldown();
    public boolean willBeReadyAgain(int durationInSeconds, int elapsedTime);
    public void reset();
}
