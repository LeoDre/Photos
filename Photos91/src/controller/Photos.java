package controller;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
/**
 * The main function that starts the GUI and the program
 * @author Ran Sa, Jami Nicastro
 *
 */
public class Photos extends Application {
	/**
	 * start the whole program
	 */
	@Override
	public void start(Stage primaryStage) throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		loginController log = loader.getController();
		log.start(primaryStage);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Photos-Login");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	/**
	 * The main method to trigger the start method
	 * @param args arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
