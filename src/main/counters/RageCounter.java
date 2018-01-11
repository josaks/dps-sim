package main.counters;

public class RageCounter {
	private int rage = 0;

    public void addRage(int rage) {
        synchronized(this) {
            this.rage += rage;
            if(rage > 100) rage = 100;
        }
    }
    //returns false if you have less rage than you try to remove. returns true if the rage was removed/spent successfully
    public boolean removeRage(int rage) {
        synchronized(this) {
            if(this.rage < rage) return false;
            this.rage -= rage;
            return true;
        }
    }
    public void addRageByDamage(int damage) {
        synchronized(this) {
            this.rage += damage / 30;
            if(rage > 100) rage = 100;
        }
    }
 
    public int getRage() {
        synchronized(this) {
            return rage;
        }
    }
    
    public void reset() {
        synchronized(this) {
    	rage = 0;
        }
    }
}
