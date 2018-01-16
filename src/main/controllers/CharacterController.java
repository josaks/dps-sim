package main.controllers;

import main.model.Player;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CharacterController {
	SimulationController simController;
	
	public void initialize() {
		save.setOnAction(e -> {
		    int hitValue = hit.getText().equals("") ? 0 : Integer.parseInt(hit.getText());
	        int critValue = crit.getText().equals("") ? 0 : Integer.parseInt(crit.getText());
	        int apValue = attackPower.getText().equals("") ? 0 : Integer.parseInt(attackPower.getText());
		    int axeskillValue = axeSkill.getText().equals("") ? 0 : Integer.parseInt(axeSkill.getText());
		    int maceskillValue = maceSkill.getText().equals("") ? 0 : Integer.parseInt(maceSkill.getText());
		    int swordskillValue = swordSkill.getText().equals("") ? 0 : Integer.parseInt(swordSkill.getText());
		    int daggerskillValue = daggerSkill.getText().equals("") ? 0 : Integer.parseInt(daggerSkill.getText());
			simController.setCharacter(Player.builder()
                    .hit(hitValue)
                    .crit(critValue)
                    .ap(apValue)
                    .axeSkill(axeskillValue)
                    .maceSkill(maceskillValue)
                    .swordSkill(swordskillValue)
                    .daggerSkill(daggerskillValue)
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
