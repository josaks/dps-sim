package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Player;

public class CharacterController {
	SimulationController simController;
	
	public void initialize() {
		save.setOnAction(t -> {
			simController.setCharacter(Player.builder()
                    .hit(Integer.parseInt(hit.getText()))
                    .crit(Integer.parseInt(crit.getText()))
                    .ap(Integer.parseInt(attackPower.getText()))
                    .axeSkill(Integer.parseInt(axeSkill.getText()))
                    .maceSkill(Integer.parseInt(maceSkill.getText()))
                    .swordSkill(Integer.parseInt(swordSkill.getText()))
                    .daggerSkill(Integer.parseInt(daggerSkill.getText()))
                    .build());
		});
	}
	
	public void setSimController(SimulationController simController) {
		this.simController = simController;
	}
	
	@FXML 
	private TextField attackPower;
    @FXML 
    private TextField hit;
    @FXML 
    private TextField crit;
    @FXML 
    private TextField axeSkill;
    @FXML 
    private TextField maceSkill;
    @FXML 
    private TextField swordSkill;
    @FXML 
    private TextField daggerSkill;
    @FXML 
    private Button save;
}
