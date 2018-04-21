package main.sim;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application{
	public static void main(String[] args) {
        launch(args);
    }
	
	@Override
	public void start(Stage primaryStage) {
		/*Weapon weapon = new Weapon(192, 289, 3400, 0, 0, 0, WEAPONTYPE.SWORD, null, Weapon.TWOHAND, 0);
		weapon.setMhProc(new Proc("Decapitate", 1000, 600));
		Player character = new Player(80, 330, 1500, weapon, 300, 300, 300, 300);
		Simulation sim = new Simulation(character);*/
		try {
    		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/Main.fxml"));
    		TabPane root = (TabPane)loader.load();
    		
    		Scene scene = new Scene(root);
    		primaryStage.setScene(scene);
    		primaryStage.setOnCloseRequest(e -> {Platform.exit(); System.exit(0);});
    		primaryStage.setTitle("Warrior DPS simulator");
    		primaryStage.show();
    	}
       catch(IOException e) {
    	   Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
       }
	}
}