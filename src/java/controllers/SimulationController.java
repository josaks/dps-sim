package controllers;


import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import counters.RageCounter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import model.Player;
import model.Proc;
import model.Weapon;
import sim.Simulation;
import utils.WEAPONTYPE;

public class SimulationController {
	Simulation sim;
	Queue<String> combatLogQueue;
	Timer GUIUpdaterTimer = new Timer();
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
		
		//gets the combatlogqueue
		combatLogQueue = sim.getCombatLogQueue();
		
		//set what happens when user clicks start button
		//updates gui from the fx application thread through runlater method
		startSim.setOnAction((e) -> {
		    windfury.setDisable(true);
			combatLog.clear();
			sim.begin();
			GUIUpdaterTimer.schedule(new TimerTask() {
				public void run() {
					Platform.runLater(() -> {
						int damageDone = sim.getDamage();
						long time = sim.getTime() / 1000;
						double currentDps = 0;
						if(time != 0) currentDps = damageDone / time;
						damage.setText(Integer.toString(damageDone));
						timeElapsed.setText(Long.toString(time));
						dps.setText(Double.toString(currentDps));
						clearRageBar();
						updateRageBar();
						while(!combatLogQueue.isEmpty()) combatLog.appendText(combatLogQueue.poll());
					});
				}
			}, 0, 10);
		});
		
		//set what happens when user clicks stop button
		stopSim.setOnAction((e) -> {
			GUIUpdaterTimer.cancel();
			GUIUpdaterTimer = new Timer();
			sim.stop();
			windfury.setDisable(false);
		});
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
