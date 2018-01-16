package main.controllers;

import main.model.Weapon;
import java.util.HashMap;
import java.util.Map;
import main.utils.WEAPONTYPE;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class WeaponController {
	SimulationController simController;
	Map<NormalizedSpeed, Double> normalizedSpeedMap;
	NormalizedSpeed mhNormalizedSpeedKey;
	NormalizedSpeed ohNormalizedSpeedKey;
	
	public void setSimController(SimulationController simController) {
		this.simController = simController;
	}
	
	public void initialize() {
		//fill the choiceboxes with items
		mhWeapontype.setItems(FXCollections.observableArrayList(
				WEAPONTYPE.AXE, WEAPONTYPE.MACE, WEAPONTYPE.SWORD, WEAPONTYPE.DAGGER));
		mhNormalizedWeaponSpeed.setItems(FXCollections.observableArrayList(
		        NormalizedSpeed.TWOHAND, NormalizedSpeed.ONEHAND));
		ohWeapontype.setItems(FXCollections.observableArrayList(
				WEAPONTYPE.AXE, WEAPONTYPE.MACE, WEAPONTYPE.SWORD, WEAPONTYPE.DAGGER));
		ohNormalizedWeaponSpeed.setItems(FXCollections.observableArrayList(
				NormalizedSpeed.TWOHAND, NormalizedSpeed.ONEHAND));
		
		//set the default values of the choiceboxes
		mhWeapontype.getSelectionModel().select(2);
		mhNormalizedWeaponSpeed.getSelectionModel().select(0);
		ohWeapontype.getSelectionModel().select(0);
		ohNormalizedWeaponSpeed.getSelectionModel().select(0);
		
		//create a map for normalized weapon speeds
		normalizedSpeedMap = new HashMap<NormalizedSpeed, Double>();
		normalizedSpeedMap.put(NormalizedSpeed.TWOHAND, 3.3);
		normalizedSpeedMap.put(NormalizedSpeed.ONEHAND, 2.4);
		normalizedSpeedMap.put(NormalizedSpeed.DAGGER, 1.7);
		
		//setting default value of the normalized speed keys
		mhNormalizedSpeedKey = mhNormalizedWeaponSpeed.getSelectionModel().getSelectedItem();
		ohNormalizedSpeedKey = ohNormalizedWeaponSpeed.getSelectionModel().getSelectedItem();
		
		//eventlisteners for when values change
		mhNormalizedWeaponSpeed.getSelectionModel().selectedItemProperty().addListener((a,o,n) -> mhNormalizedSpeedKey = n);
		ohNormalizedWeaponSpeed.getSelectionModel().selectedItemProperty().addListener((a,o,n) -> ohNormalizedSpeedKey = n);
		
		mhWeapontype.getSelectionModel().selectedItemProperty().addListener((a,o,n) -> {
		    if(n.equals(WEAPONTYPE.DAGGER)) {
		        mhNormalizedWeaponSpeed.setItems(FXCollections.observableArrayList(NormalizedSpeed.DAGGER));
		        mhNormalizedWeaponSpeed.setVisible(false);
		    }
		    else {
		        mhNormalizedWeaponSpeed.setItems(FXCollections.observableArrayList(NormalizedSpeed.TWOHAND, NormalizedSpeed.ONEHAND));
		        mhNormalizedWeaponSpeed.setVisible(true);
		    }
		    mhNormalizedWeaponSpeed.getSelectionModel().select(0);
		});
		
		ohWeapontype.getSelectionModel().selectedItemProperty().addListener((a,o,n) -> {
            if(n.equals(WEAPONTYPE.DAGGER)) {
                ohNormalizedWeaponSpeed.setItems(FXCollections.observableArrayList(NormalizedSpeed.DAGGER));
                ohNormalizedWeaponSpeed.setVisible(false);
            }
            else {
                 ohNormalizedWeaponSpeed.setItems(FXCollections.observableArrayList(NormalizedSpeed.TWOHAND, NormalizedSpeed.ONEHAND));
                 ohNormalizedWeaponSpeed.setVisible(true);
            }
            ohNormalizedWeaponSpeed.getSelectionModel().select(0);
        });
		
		save.setOnAction(e -> setWeapon());
	}
	
	public void setWeapon() {
		Weapon weapon = Weapon.builder()
                .mhWeaponDamageMin(mhMinDamage.getText().equals("") ? 0 : Integer.parseInt(mhMinDamage.getText()))
                .mhWeaponDamageMax(mhMaxDamage.getText().equals("") ? 0 : Integer.parseInt(mhMaxDamage.getText()))
                .mhSpeed(mhSpeed.getText().equals("") ? 0 : Integer.parseInt(mhSpeed.getText()))
                .normalizedMhSpeed(normalizedSpeedMap.get(mhNormalizedSpeedKey))
                .mhWeaponType(mhWeapontype.getSelectionModel().getSelectedItem())
                .ohWeaponDamageMin(ohMinDamage.getText().equals("") ? 0 : Integer.parseInt(ohMinDamage.getText()))
                .ohWeaponDamageMax(ohMaxDamage.getText().equals("") ? 0 : Integer.parseInt(ohMaxDamage.getText()))
                .ohSpeed(ohSpeed.getText().equals("") ? 0 : Integer.parseInt(ohSpeed.getText()))
                .ohWeaponType(ohWeapontype.getSelectionModel().getSelectedItem())
                .normalizedOhSpeed(normalizedSpeedMap.get(ohNormalizedSpeedKey))
                .build();
		simController.setWeapon(weapon);
	}
	
	private enum NormalizedSpeed{
        TWOHAND, ONEHAND, DAGGER;
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
    private ChoiceBox<NormalizedSpeed> mhNormalizedWeaponSpeed;
    @FXML
    private ChoiceBox<WEAPONTYPE> ohWeapontype;
    @FXML
    private ChoiceBox<NormalizedSpeed> ohNormalizedWeaponSpeed;
    @FXML
    private Button save;
}
