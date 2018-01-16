package main.utils;

import java.util.Random;

import org.apache.commons.lang3.time.StopWatch;

public class Constants {
    //boss defence skill used in Simulation class, will be changable from ui
    public static int bossDefSkill = 315;
    
    //get random int 
    //exclusive 0, inclusive max
    public static int getRandomIntWithCeiling(int max){
    	Random random = new Random();
    	return random.nextInt(max) + 1;
    }
    
    public synchronized static long getTime(StopWatch sw) {
        long time;
        if(sw.isStarted()) {
            sw.split();
            time = sw.getTime();
            sw.unsplit();
        }
        else time = sw.getTime();
        return time;
    }
}
