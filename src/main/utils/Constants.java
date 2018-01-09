package main.utils;

import java.util.Random;

public class Constants {
    //boss defence skill used in Simulation class, will be changable from ui
    public static int bossDefSkill = 315;
    
    //get random int 
    //exclusive 0, inclusive max
    public static int getRandomIntWithCeiling(int max){
    	Random random = new Random();
    	return random.nextInt(max) + 1;
    }
}
