package main.counters;

public class DamageCounter {
	private int damage = 0;

    public void addDamage(int damage){
        synchronized(this) {
            this.damage += damage;
        }
    }
    public int getDamage() {
        synchronized(this) {
            return damage;
        }
    }
    public void reset() {
        synchronized(this) {
            damage = 0;
        }
    }
}
