package main.controllers;

import javafx.fxml.FXML;

public class MainController {
	public SimulationController getSimulationController() {
		return simulationController;
	}
	public void initialize() {
		characterController.setSimController(simulationController);
		weaponController.setSimController(simulationController);
		procsController.setSimController(simulationController);
	}
	
	@FXML 
	private SimulationController simulationController;
	@FXML 
	private CharacterController characterController;
	@FXML 
	private WeaponController weaponController;
	@FXML 
	private ProcsController procsController;
}