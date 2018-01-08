package controller;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.util.Duration;
import model.user;
import model.userlist;
/**
 * Controls the admin subsystem 
 * @author Ran Sa, Jami Nicastro
 *
 */
public class adminController{
	@FXML private ListView<user> listView;
	@FXML private Label addStatus;
	@FXML private Label deleteStatus;
	@FXML private Label nameInfo;
	@FXML private Button addB;
	@FXML private Button deleteB;
	@FXML private Button logout;
	@FXML private Button exit;
	@FXML private TextField nameField;
	private ObservableList<user> obsList;
	private userlist ul;
	private Stage pstage;
	/**
	 * Start the stage by filled list with data
	 * @param mainStage the stage that this window is on  
	 * @param list The list that filled the list view
	 */
	public void start(Stage mainStage, userlist list) {
		pstage = mainStage;
		ul = list;
		// create an ObservableList from an ArrayList              
		obsList = FXCollections.observableArrayList(ul.getList());
		listView.setItems(obsList);
		//select the first user
		listView.getSelectionModel().select(0);
		nameInfo.setText(listView.getItems().get(0).getName());
		//add listener for the list
		listView.getSelectionModel().selectedIndexProperty().addListener((obs,oldV,newV)->showInfo(mainStage));
	}
	//show user the song details when they select a certain song from the list
	//also display info for the edit if edit is selected
	/**
	 * show detail in the info section when a user is selected
	 * @param mainStage the stage that this window is on  
	 */
	private void showInfo(Stage mainStage){
		int i = listView.getSelectionModel().getSelectedIndex();
		if(i>=0){
			nameInfo.setText(listView.getItems().get(i).getName());
		}else{
			nameInfo.setText("");
		}
	}
	/**
	 * An event handler to handle the add user event
	 * @param e the action that has been triggered
	 */
	public void addUser(ActionEvent e){
		//an add action
		String name = nameField.getText();
		int result = ul.add(name,false);
		//check if the information user provided are valid
		if(result==0){
			addStatus.setTextFill(Color.web("#ff0000"));
			addStatus.setText("Fail to add user: User already existed");
		}else if(result==-1){
			addStatus.setTextFill(Color.web("#ff0000"));
			addStatus.setText("Fail to add user: The name of the user cannot be empty");
		}else{
			Alert ac = new Alert(AlertType.CONFIRMATION);
			ac.setTitle("Add User Confirmation");
			ac.setHeaderText("Are you sure you want to add the following user?");
			ac.setContentText("Username:   "+name);
			//set the button that user can choose
			ButtonType buttonTypeAdd = new ButtonType("Add", ButtonData.OK_DONE);
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			ac.getButtonTypes().setAll(buttonTypeAdd,buttonTypeCancel);
			Optional<ButtonType> r = ac.showAndWait();
			if(r.get()==buttonTypeAdd){
				result = ul.add(name,true);
				obsList = FXCollections.observableArrayList(ul.getList());
				listView.setItems(obsList);
				//select user after addition
				listView.refresh();
				listView.getSelectionModel().select(result);
				//notice the admin that their addition is successful
				addStatus.setTextFill(Color.web("#32CD32"));
				addStatus.setText("Successfully Added");
				nameField.setText("");
				//display the text for 1.5 seconds
				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> addStatus.setText("")));
				timeline.play();
				
			}	
		}
	}
	/**
	 * An event handler to handle the delete user event
	 * @param e the action that has been triggered
	 */
	public void deleteUser(ActionEvent e){
		int i = listView.getSelectionModel().getSelectedIndex();
		//make sure that admin is not deleted
		if(ul.getUser(i).getName().equals("admin")){
			deleteStatus.setTextFill(Color.web("#ff0000"));
			deleteStatus.setText("Deletion Failed: admin account cannot be deleted");
			return;
		}
		//set the alert window
		Alert dc = new Alert(AlertType.CONFIRMATION);
		dc.setTitle("Deletion Confirmation");
		dc.setHeaderText("Are you sure you want to delete the following user?");
		dc.setContentText("Name:   "+listView.getItems().get(i).getName());
		//set the button that user can choose
		ButtonType buttonTypeDelete = new ButtonType("Delete", ButtonData.OK_DONE);
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dc.getButtonTypes().setAll(buttonTypeDelete,buttonTypeCancel);
		Optional<ButtonType> result = dc.showAndWait();
		//select song after deletion
		if(result.get()==buttonTypeDelete){
			int s;
			if(i==ul.getList().size()-1 && i!=0){
				s = i-1;
			}else if(i==ul.getList().size()-1 && i==0){
				s = -1;
			}else{
				s = i;
			}
			//delete from list and file
			//delete all the photo in the pool
			user u = ul.getUser(i);
			for(int x=0; x<u.getAlbumList().getSize(); x++){
				for(int y=0; y<u.getAlbumList().getList().get(x).getSize();y++){
					try{
						Files.delete(u.getAlbumList().getList().get(x).getList().get(y).getPath());
					}catch(IOException ioe){
						deleteStatus.setTextFill(Color.web("#ff0000"));
						deleteStatus.setText("Fail to delete photo, please try to delete manually");
						//display the text for 1.5 seconds
						Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> deleteStatus.setText("")));
						timeline.play();
					}
				}
			}
			ul.remove(i);		
			//refresh listView
			obsList = FXCollections.observableArrayList(ul.getList());
			listView.setItems(obsList);
			listView.refresh();
			listView.getSelectionModel().select(s);
			//notice the user that their deletion is successful
			deleteStatus.setTextFill(Color.web("#32CD32"));
			deleteStatus.setText("Successfully Deleted");
			//display the text for 1.5 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> deleteStatus.setText("")));
			timeline.play();
		}
	}
	/**
	 * An event handler to handle the logout event
	 * @param e the action that has been triggered
	 * @throws Exception
	 */
	public void logout(ActionEvent e) throws Exception{
		//set the alert window
		Alert dc = new Alert(AlertType.CONFIRMATION);
		dc.setTitle("Logout Confirmation");
		dc.setHeaderText("Are you sure you want to logout?\nAll the changes will be saved.");
		//set the button that user can choose
		ButtonType buttonTypeLogout = new ButtonType("Logout", ButtonData.OK_DONE);
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dc.getButtonTypes().setAll(buttonTypeLogout,buttonTypeCancel);
		Optional<ButtonType> result = dc.showAndWait();
		if(result.get()==buttonTypeLogout){
			try{
				userlist.writeData(ul);
			}catch(IOException ioe){
				//set error alert
				Alert er = new Alert(AlertType.ERROR);
				er.setTitle("Error");
				er.setHeaderText("Fail to save changes");
				er.setContentText("An error occured when the program tried to save the changes\nPlease try logout again\nIf you have encountered this error multiple times, please try exit");
				er.showAndWait();
				return;
			}
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/login.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			loginController log = loader.getController();
			log.start(pstage);
			Scene scene = new Scene(root);
			pstage.setScene(scene);
			pstage.setTitle("Photos-Login");
			pstage.setResizable(false);
			pstage.show();
		}
	}
	/**
	 * An event handler to handle the exit event
	 * @param e the action that has been triggered
	 */
	public void exit(ActionEvent e){
		//set the alert window
		Alert dc = new Alert(AlertType.CONFIRMATION);
		dc.setTitle("Exit Confirmation");
		dc.setHeaderText("Are you sure you want to exit?\nAll the changes will be saved.");
		//set the button that user can choose
		ButtonType buttonTypeExit = new ButtonType("Exit", ButtonData.OK_DONE);
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dc.getButtonTypes().setAll(buttonTypeExit,buttonTypeCancel);
		Optional<ButtonType> result = dc.showAndWait();
		if(result.get()==buttonTypeExit){
			try{
				userlist.writeData(ul);
			}catch(IOException ioe){
				//set error alert
				Alert er = new Alert(AlertType.CONFIRMATION);
				er.setTitle("Error");
				er.setHeaderText("Fail to save changes");
				er.setContentText("An error occured when the program tried to save the changes\nPlease try exit again\nIf you have encountered this error multiple times or you fail to logout multiple times\n"
						+ "please try Force exit and all the changes will not be safed");
				ButtonType buttonTypeFExit = new ButtonType("FORCE Exit", ButtonData.OK_DONE);
				ButtonType buttonTypeRetry = new ButtonType("Retry", ButtonData.CANCEL_CLOSE);
				er.getButtonTypes().setAll(buttonTypeFExit,buttonTypeCancel);
				Optional<ButtonType> re = er.showAndWait();
				if(re.get()==buttonTypeRetry){
					return;
				}else if(re.get()==buttonTypeFExit){
					System.exit(0);
				}
			}
			System.exit(0);
		}
	}
}