package abilities;

public interface Ability {
    public AttackResultContainer perform();
    public boolean isReady();
    public int getRageCost();
}