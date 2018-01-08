package controller;
import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import model.userlist;
/**
 * Controls the login event and view
 * @author Ran Sa, Jami Nicastro
 *
 */
public class loginController{
	@FXML private Button loginB;
	@FXML private Button exitB;
	@FXML private TextField enName;
	@FXML private Label status;
	private userlist list;
	private Stage mainstage;
	/**
	 * Set up the stage and load data from the disk
	 * @param p the satge which window is on
	 */
	public void start(Stage p){
		try{
		list = userlist.loadData();
		}catch(IOException i){
			display("Failed to load data, please restart the application", "#ff0000", -1);
		}catch(ClassNotFoundException c){
			display("Failed to load data, please restart the application", "#ff0000", -1);
		}
		if(list==null){
			list = new userlist();
			try{
				userlist.writeData(list);
			}catch(IOException i){
				display("Failed to load data, please restart the application", "#ff0000", -1);
			}
		}
		mainstage = p;
	}
	/**
	 * An event handler to handle the login event
	 * @param e the action that has been triggered
	 * @throws Exception
	 */
	public void login(ActionEvent e)throws Exception{
		String name = enName.getText();
		if(name.equals("")){
			this.display("User name cannot be empty", "#ff0000", -1);
		}else{
			int index = list.check(name);
			if(index!=-1){
				//login successfully
				this.display("Login Successfully", "#32CD32", 1.5);
				FXMLLoader loader = new FXMLLoader();
				if(name.equals("admin")){
					//go to admin subsystem
					loader.setLocation(getClass().getResource("/view/adminsub.fxml"));
					AnchorPane root = (AnchorPane)loader.load();
					adminController amc = loader.getController();
					amc.start(mainstage,list);
					Scene scene = new Scene(root);
					mainstage.setScene(scene);
					mainstage.setTitle("Photos-AdminSystem");
				}else{
					//go to user subsystem
					loader.setLocation(getClass().getResource("/view/usersubAlbum.fxml"));
					AnchorPane root = (AnchorPane)loader.load();
					userAlbumController uac = loader.getController();
					uac.start(mainstage, list, index);
					Scene scene = new Scene(root);
					mainstage.setScene(scene);
					mainstage.setTitle("Photos-UserSystem");
				}
				mainstage.setResizable(false);
				mainstage.show();
			}else{
				//fail to login
				this.display("User dosen't exist", "#ff0000", -1);
			}
		}
	}
	//exit
	/**
	 * An event handler to handle the exit event
	 * @param e the action that has been triggered
	 */
	public void exit(ActionEvent e){
		System.exit(0);
	}
	//String to display
	//color of the string
	//time
	/**
	 * display a certain color of the certain text for a certain second
	 * @param content the content user want to display
	 * @param color the color of the font
	 * @param time the time user wants to display
	 */
	private void display(String content, String color, double time){
		status.setText(content);
		status.setTextFill(Color.web(color));
		if(time>0){
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(time), evt -> status.setText("")));
			timeline.play();
		}
	}
	
}