package main.controllers;

import main.model.Player;
import main.model.Proc;
import main.model.Weapon;
import main.sim.Simulation;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import main.utils.WEAPONTYPE;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class SimulationController {
	Simulation sim;
	Queue<String> combatLogQueue;
	ScheduledExecutorService scheduler;
	ScheduledFuture<?> guiUpdate;
	Player character;
	Weapon weapon;
	Proc mhProc;
	Proc ohProc;
	
	public void initialize() {
		combatLog.setEditable(false);
		damage.setEditable(false);
		timeElapsed.setEditable(false);
		dps.setEditable(false);
		clearRageBar();
		
		//initialize sim
		weapon = Weapon.builder()
		            .mhWeaponDamageMin(192)
		            .mhWeaponDamageMax(289)
		            .mhSpeed(3400)
		            .normalizedMhSpeed(Weapon.TWOHAND)
		            .mhWeaponType(WEAPONTYPE.SWORD)
		            .build();
		character = Player.builder()
		                .hit(80)
		                .crit(330)
		                .ap(1500)
		                .build();
		character.setWeapon(weapon);
		createNewSimulation();
		
		//keeps the combatlog scrolled down
		combatLog.textProperty().addListener((a,b,c) -> {
			combatLog.setScrollTop(Double.MAX_VALUE);
		});
		
		//register eventlistener on windfury checkbox
		windfury.selectedProperty().addListener((a,o,n) -> {
		    setWindfury(n);
		});
	}
	
	public void createNewSimulation() {
		weapon.setMhProc(mhProc);
		weapon.setOhProc(ohProc);
		character.setWeapon(weapon);
		sim = new Simulation(character);
		
		//set scheduler
        this.scheduler = Executors.newScheduledThreadPool(1);
		
		//gets the combatlogqueue
		combatLogQueue = sim.getCombatLogQueue();
		
		//set what happens when user clicks start button
		//updates gui from the fx application thread through runlater method
		startSim.setOnAction((e) -> {
		    windfury.setDisable(true);
		    duration.setEditable(false);
			combatLog.clear();
			sim.begin();
			
			
			guiUpdate = scheduler.scheduleAtFixedRate(new Runnable() {
				public void run() {
				    time = sim.getTime() / 1000;
				    damageDone = sim.getDamage();
				    int duration = sim.getDurationInSeconds();
				    int bossHealth = sim.getBossHealth();
				    
					Platform.runLater(() -> {
						setDps(time, damageDone);
						clearRageBar();
						updateRageBar();
						while(!combatLogQueue.isEmpty()) combatLog.appendText(combatLogQueue.poll());
						
						//stop sim if time is up or boss is dead
						if(duration != 0 && time >= duration) stopSim();
						if(bossHealth != 0 && damageDone >= bossHealth) stopSim();
					});
				}
			}, 0, 10, TimeUnit.MILLISECONDS);
		});
		
		//user clicks stop button
		stopSim.setOnAction((e) -> {
			stopSim();
		});
		
		duration.textProperty().addListener((a,o,n) -> {
            if(n.equals("")) sim.setDurationInSeconds(0);
            else sim.setDurationInSeconds(Integer.parseInt(n));
		});
		
		bossHealth.textProperty().addListener((a,o,n) ->{
		    if(n.equals("")) sim.setBossHealth(0);
            else sim.setBossHealth(Integer.parseInt(n));
		});
	}
	
	//how long the simulation has been running for
	long time;
	//damage done in the simulation
	int damageDone;
	
	private void setDps(long time, int damageDone) {
	    damage.setText(Integer.toString(damageDone));
        timeElapsed.setText(Long.toString(time));
        double currentDps = time != 0 ? damageDone / time : 0;
        dps.setText(Double.toString(currentDps));
	}
	
	private void stopSim() {
	    guiUpdate.cancel(false);
        sim.stop();
        duration.setEditable(true);
        windfury.setDisable(false);
	}
	
	public void setSim(Simulation sim) {
		this.sim = sim;
	}
	
	public void setWindfury(boolean b) {
	    sim.setWindfury(b);
	}
	
	private void updateRageBar() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.RED);
		gc.fillRect(0, 0, ((double)sim.getRage() / 100) * canvas.getWidth(), canvas.getHeight());
	}
	
	private void clearRageBar() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	public void setCharacter(Player character) {
		this.character = character;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public void setMhProc(Proc mhProc) {
		this.mhProc = mhProc;
	}

	public void setOhProc(Proc ohProc) {
		this.ohProc = ohProc;
	}
	
	@FXML
	private TextField bossHealth;
	@FXML
	private TextField duration;
	@FXML
	private Canvas canvas;
	@FXML
    private TextArea combatLog;
    @FXML
    private TextField damage;
    @FXML
    private TextField timeElapsed;
    @FXML
    private TextField dps;
    @FXML
    private Button startSim;
    @FXML
    private Button stopSim;
    @FXML
    private CheckBox windfury;
}
