package main.counters;

public class DamageCounter {
	private int damage = 0;

    public synchronized void addDamage(int damage){
        this.damage += damage;
    }
    public synchronized int getDamage() {
        return damage;
    }
    public synchronized void reset() {
    	damage = 0;
    }
}
