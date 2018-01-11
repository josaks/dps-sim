package main.sim;

import java.util.List;
import main.abilities.Cooldown;

public class CooldownUser implements Runnable{
    List<Cooldown> cdList;
    
    protected CooldownUser(List<Cooldown> cdList) {
        this.cdList = cdList;
    }
    
    public void run() {
        cdList.forEach(Cooldown::use);
    }
    
}
