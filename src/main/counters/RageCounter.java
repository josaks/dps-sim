package main.counters;

public class RageCounter {
	private int rage = 0;

    public synchronized void addRage(int rage) {
        this.rage += rage;
        if(rage > 100) rage = 100;
    }
    //returns false if you have less rage than you try to remove. returns true if the rage was removed/spent successfully
    public synchronized boolean removeRage(int rage) {
    	if(this.rage < rage) return false;
        this.rage -= rage;
        return true;
    }
    public synchronized void addRageByDamage(int damage) {
        this.rage += damage / 30;
        if(rage > 100) rage = 100;
    }
 
    public synchronized int getRage() {
        return rage;
    }
    
    public synchronized void reset() {
    	rage = 0;
    }
}
