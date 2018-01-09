package main.controllers;

import main.model.Weapon;
import java.util.HashMap;
import java.util.Map;
import main.utils.WEAPONTYPE;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class WeaponController {
	SimulationController simController;
	Map<String, Double> normalizedSpeedMap;
	String mhNormalizedSpeedKey;
	String ohNormalizedSpeedKey;
	
	public void setSimController(SimulationController simController) {
		this.simController = simController;
	}
	
	public void initialize() {
		//fill the choiceboxes with items
		mhWeapontype.setItems(FXCollections.observableArrayList(
				WEAPONTYPE.AXE, WEAPONTYPE.MACE, WEAPONTYPE.SWORD, WEAPONTYPE.DAGGER));
		mhNormalizedWeaponSpeed.setItems(FXCollections.observableArrayList(
				"Two-hand", "One-hand", "Dagger"));
		ohWeapontype.setItems(FXCollections.observableArrayList(
				WEAPONTYPE.AXE, WEAPONTYPE.MACE, WEAPONTYPE.SWORD, WEAPONTYPE.DAGGER));
		ohNormalizedWeaponSpeed.setItems(FXCollections.observableArrayList(
				"Two-hand", "One-hand", "Dagger"));
		
		//set the default values of the choiceboxes
		mhWeapontype.getSelectionModel().select(2);
		mhNormalizedWeaponSpeed.getSelectionModel().select(0);
		ohWeapontype.getSelectionModel().select(0);
		ohNormalizedWeaponSpeed.getSelectionModel().select(0);
		
		//create a map for normalized weapon speeds
		normalizedSpeedMap = new HashMap<String, Double>();
		normalizedSpeedMap.put("Two-hand", 3.3);
		normalizedSpeedMap.put("One-hand", 2.4);
		normalizedSpeedMap.put("Dagger", 1.7);
		
		//setting default value of the normalized speed keys
		mhNormalizedSpeedKey = mhNormalizedWeaponSpeed.getSelectionModel().getSelectedItem();
		ohNormalizedSpeedKey = ohNormalizedWeaponSpeed.getSelectionModel().getSelectedItem();
		
		//eventlisteners for when values change
		mhSpeed.textProperty().addListener((a,o,n) -> setWeapon());
		mhMinDamage.textProperty().addListener((a,o,n) -> setWeapon());
		mhMaxDamage.textProperty().addListener((a,o,n) -> setWeapon());
		ohSpeed.textProperty().addListener((a,o,n) -> setWeapon());
		ohMinDamage.textProperty().addListener((a,o,n) -> setWeapon());
		ohMaxDamage.textProperty().addListener((a,o,n) -> setWeapon());
		mhWeapontype.getSelectionModel().selectedIndexProperty().addListener((a,o,n) -> setWeapon());
		ohWeapontype.getSelectionModel().selectedIndexProperty().addListener((a,o,n) -> setWeapon());
		mhNormalizedWeaponSpeed.getSelectionModel().selectedItemProperty().addListener((a,o,n) -> {
			mhNormalizedSpeedKey = n;
			setWeapon();
		});
		ohNormalizedWeaponSpeed.getSelectionModel().selectedItemProperty().addListener((a,o,n) -> {
			ohNormalizedSpeedKey = n;
			setWeapon();
		});
	}
	
	public void setWeapon() {
		Weapon weapon = Weapon.builder()
                .mhWeaponDamageMin(Integer.parseInt(mhMinDamage.getText()))
                .mhWeaponDamageMax(Integer.parseInt(mhMaxDamage.getText()))
                .mhSpeed(Integer.parseInt(mhSpeed.getText()))
                .normalizedMhSpeed(normalizedSpeedMap.get(mhNormalizedSpeedKey))
                .mhWeaponType(mhWeapontype.getSelectionModel().getSelectedItem())
                .ohWeaponDamageMin(Integer.parseInt(ohMinDamage.getText()))
                .ohWeaponDamageMax(Integer.parseInt(ohMaxDamage.getText()))
                .ohSpeed(Integer.parseInt(ohSpeed.getText()))
                .ohWeaponType(ohWeapontype.getSelectionModel().getSelectedItem())
                .normalizedOhSpeed(normalizedSpeedMap.get(ohNormalizedSpeedKey))
                .build();
		simController.setWeapon(weapon);
	}
	
	@FXML
    private TextField mhMinDamage;
    @FXML
    private TextField mhMaxDamage;
    @FXML
    private TextField mhSpeed;
    @FXML
    private TextField ohMinDamage;
    @FXML
    private TextField ohMaxDamage;
    @FXML
    private TextField ohSpeed;
    @FXML
    private ChoiceBox<WEAPONTYPE> mhWeapontype;
    @FXML
    private ChoiceBox<String> mhNormalizedWeaponSpeed;
    @FXML
    private ChoiceBox<WEAPONTYPE> ohWeapontype;
    @FXML
    private ChoiceBox<String> ohNormalizedWeaponSpeed;
    @FXML
    private Button save;
}
