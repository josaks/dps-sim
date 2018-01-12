package main.abilities;

public interface Cooldown {
    public void use();
    public boolean isReady();
    public int getRageCost();
}
