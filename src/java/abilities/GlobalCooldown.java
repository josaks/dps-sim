package abilities;

import java.util.Timer;
import java.util.TimerTask;

public class GlobalCooldown {
	//if true, gcd is rdy to be used by an ability
	//if false, abilities must wait for gcd to be ready
	//takes 1.5s to get rdy after an ability
	private boolean ready = true;
	private Timer timer = new Timer();
	
	//returns true if the global cooldown is ready, returns false if it is not ready
	//if it is ready and true is returned, also starts cooling down
	public boolean use() {
		if(ready) {
			ready = false;
			//readies global cooldown after 1.5s
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					ready = true;
				}
			}, 1500);
			return true;
		}
		return false;
	}
	
	public boolean isReady() {
		return ready;
	}
}
