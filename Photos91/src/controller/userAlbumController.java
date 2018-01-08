package controller;

import java.util.Optional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.album;
import model.albumList;
import model.imgR;
import model.userlist;
/**
 * controller for the user subsystem 
 * @author Ran Sa, Jami Nicastro
 *
 */
public class userAlbumController{
	@FXML private ListView<album> listView;
	@FXML private Label nameInfo;
	@FXML private Label numOfPhotos;
	@FXML private Label rangeOfDates;
	@FXML private Label ODStatus;
	@FXML private Label EALabel;
	@FXML private Label EAStatus;
	@FXML private Label logInfo;
	@FXML private TextField nameTextField;
	@FXML private Button openButton;
	@FXML private Button deleteButton;
	@FXML private Button saveButton;
	@FXML private Button cancelButton;
	@FXML private Button logoutButton;
	@FXML private Button exitButton;
	@FXML private RadioButton addButton;
	@FXML private RadioButton editButton;
	private ObservableList<album> obsList;
	private albumList al;
	private userlist ul;
	private Stage pstage;
	private int index;
	/**
	 * set up the GUI and the controller by filling in the stage it is on, the userlist and the index of which user logs in
	 * @param mainStage the stage it is on
	 * @param us the list of all users
	 * @param index	the index of the currently loged in user in the user list
	 */
	public void start(Stage mainStage, userlist us, int index) {
		pstage = mainStage;
		ul = us;
		this.index = index;
		//load albumlist from userlist
		al = us.getUser(index).getAlbumList();
		String userName = us.getUser(index).getName();
		// create an ObservableList from an ArrayList              
		obsList = FXCollections.observableArrayList(al.getList());
		listView.setItems(obsList);
		//check if the list contains any album
		if(al.getSize()>0){
			//if the list is not empty
			//select the first item
			listView.getSelectionModel().select(0);
			nameInfo.setText(listView.getItems().get(0).getName());
			numOfPhotos.setText(Integer.toString(listView.getItems().get(0).getCount()));
			rangeOfDates.setText(listView.getItems().get(0).getRange());
		}else{
			//if the list is empty
			nameInfo.setText("");
			numOfPhotos.setText("");
			rangeOfDates.setText("");
		}
		//set all status bar to default
		EAStatus.setText("");
		ODStatus.setText("");
		//set Text Field to default
		nameTextField.setText("");
		//set login user name
		logInfo.setText(userName);
		//add radio buttons to a group and select add button by default
		ToggleGroup group = new ToggleGroup();
		addButton.setToggleGroup(group);
		addButton.setSelected(true);
		editButton.setToggleGroup(group);
		//add listener for the list
		listView.getSelectionModel().selectedIndexProperty().addListener((obs,oldV,newV)->showInfo(mainStage));
	}
	//show user the song details when they select a certain song from the list
	//also display info for the edit if edit is selected
	/**
	 * show information of the selected user album when selected 
	 * @param mainStage the stage it is on
	 */
	private void showInfo(Stage mainStage){
		int i = listView.getSelectionModel().getSelectedIndex();
		if(i>=0){
			nameInfo.setText(listView.getItems().get(i).getName());
			numOfPhotos.setText(Integer.toString(listView.getItems().get(i).getCount()));
			rangeOfDates.setText(listView.getItems().get(i).getRange());
		}else{
			nameInfo.setText("");
			numOfPhotos.setText("");
			rangeOfDates.setText("");
		}
		//display info for edit
		if(editButton.isSelected()){
			//if the list is no album in the library reject edit
			if(al.getSize()==0&&i<0){
				//change header
				EALabel.setText("Edit Album");
				//set text to warn user
				//switch back to add
				EAStatus.setTextFill(Color.web("#ff0000"));
				EAStatus.setText("Library is empty. No song can be edited.");
				addButton.setSelected(true);
				//change header
				EALabel.setText("Add Song");
				//set text field to default
				nameTextField.setText("");
				//display the text for 3 seconds
				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> EAStatus.setText("")));
				timeline.play();
			}else if(al.getSize()>0&&i>=0){
				//change header
				EALabel.setText("Edit Album");
				//set lower feedback to empty
				EAStatus.setText("");
				//display selected song info in the text field
				nameTextField.setText(listView.getItems().get(i).getName());
			}
		}
	}
	/**
	 * select a certain album and display the changes on the lisview
	 * @param a the album you want to select
	 */
	public void setSelect(album a){
		listView.getSelectionModel().select(a);
	}
	/**
	 * An event handler to handle the switch event, switch between add and edit button
	 * @param e the action that has been triggered
	 */
	//switch between add and edit button
	public void switchAction(ActionEvent e){
		int i = listView.getSelectionModel().getSelectedIndex();
		if(editButton.isSelected()){
			//change header
			EALabel.setText("Edit Album");
			//if the list is no song in the library reject edit
			if(al.getSize()<=0){
				//set text to warn user
				//switch back to add
				EAStatus.setTextFill(Color.web("#ff0000"));
				EAStatus.setText("Library is empty. No Album can be edited.");
				addButton.setSelected(true);
				//change header
				EALabel.setText("Add Album");
				//set text field to default
				nameTextField.setText("");
				//display the text for 3 seconds
				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> EAStatus.setText("")));
				timeline.play();
			}else{
				//set lower feedback to empty
				EAStatus.setText("");
				//display selected song info in the text field
				nameTextField.setText(listView.getItems().get(i).getName());
			}
		}else{
			//change header
			EALabel.setText("Add Album");
			//set lower feedback to empty
			EAStatus.setText("");
			//set text field to default
			nameTextField.setText("");
		}
	}
	/**
	 * An event handler to handle the delete event
	 * @param e the action that has been triggered
	 */
	public void deleteA(ActionEvent e){
		//check if the list is empty
		if(al.getSize()<=0){
			ODStatus.setTextFill(Color.web("#ff0000"));
			ODStatus.setText("Library is empty. No album can be deleted.");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ODStatus.setText("")));
			timeline.play();
		}else{
			//if not empty set the alert window
			int i = listView.getSelectionModel().getSelectedIndex();
			Alert dc = new Alert(AlertType.CONFIRMATION);
			dc.setTitle("Deletion Confirmation");
			dc.setHeaderText("Are you sure you want to delete the following album?");
			dc.setContentText("Album Name:   "+listView.getItems().get(i).getName());
			//set the button that user can choose
			ButtonType buttonTypeDelete = new ButtonType("Delete", ButtonData.OK_DONE);
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			dc.getButtonTypes().setAll(buttonTypeDelete,buttonTypeCancel);
			Optional<ButtonType> result = dc.showAndWait();
			//select song after deletion
			if(result.get()==buttonTypeDelete){
				int s;
				if(i==al.getSize()-1 && i!=0){
					s = i-1;
				}else if(i==al.getSize()-1 && i==0){
					s = -1;
				}else{
					s = i;
				}
				//remove all the photos in this album
				album ab = listView.getItems().get(i);
				for(int ic=0; ic<ab.getSize(); ic++){
					if(ab.getSize()>0){
						//check if this is the last reference point points to this photo, if yes delete from disk
						imgR tmp = ab.getList().get(ic);
						int indicate = 0;
						for(int id=0; id<al.getSize();id++){
							if(al.getList().get(id).getList().indexOf(tmp)>=0){
									indicate++;
							}
						}
						if(indicate<=1){
							//delete from disk
							try{
								Files.delete(tmp.getPath());
							}catch(NoSuchFileException x){
								//do nothing, assume there is duplicate
							}catch (IOException e1) {
									ODStatus.setTextFill(Color.web("#ff0000"));
									ODStatus.setText("Fail to delete: Please manually delete from disk");
									//display the text for 3 seconds
									Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ODStatus.setText("")));
									timeline.play();
							}
						}
					}
				}
						
			//delete from list
			al.remove(i);
			//refresh listView
			obsList = FXCollections.observableArrayList(al.getList());
			listView.setItems(obsList);
			listView.refresh();
			listView.getSelectionModel().select(s);
			//notice the user that their deletion is successful
			ODStatus.setTextFill(Color.web("#32CD32"));
			ODStatus.setText("Successfully Deleted");
			//display the text for 1.5 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> ODStatus.setText("")));
			timeline.play();
			}
		}
	}
	/**
	 * An event handler to handle the open album event, open up a new window for the album detail
	 * @param e the action that has been triggered
	 * @throws Exception
	 */
	public void openA(ActionEvent e) throws Exception{
		if(al.getSize()<=0){
			ODStatus.setTextFill(Color.web("#ff0000"));
			ODStatus.setText("Library is empty. No album can be added");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ODStatus.setText("")));
			timeline.play();
			return;
		}
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/usersubPList.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		userPListController upc = loader.getController();
		album a = listView.getSelectionModel().getSelectedItem();
		upc.start(pstage,ul,al,a,index);
		Scene scene = new Scene(root);
		pstage.setScene(scene);
		pstage.setTitle("Photos-UserSystem");
		pstage.setResizable(false);
		pstage.show();
	}
	/**
	 * An event handler to handle the add or edit album event
	 * @param e the action that has been triggered
	 */
	public void AEAction(ActionEvent e){
		//check the original selected album
		int o = listView.getSelectionModel().getSelectedIndex();
		if(addButton.isSelected()){
			//an add action
			String name = nameTextField.getText();
			int result = al.add(name, false);
			//check if the information user provided are valid
			if(result==-1){
				EAStatus.setTextFill(Color.web("#ff0000"));
				EAStatus.setText("Fail to add album: album name cannot be empty");
			}else if(result==0){
				EAStatus.setTextFill(Color.web("#ff0000"));
				EAStatus.setText("Fail to add album: Album already exist");
			}else{
				Alert ac = new Alert(AlertType.CONFIRMATION);
				ac.setTitle("Add Album Confirmation");
				ac.setHeaderText("Are you sure you want to add the following album to your album library?");
				ac.setContentText("Album Name:   "+name);
				//set the button that user can choose
				ButtonType buttonTypeAdd = new ButtonType("Add", ButtonData.OK_DONE);
				ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
				ac.getButtonTypes().setAll(buttonTypeAdd,buttonTypeCancel);
				Optional<ButtonType> r = ac.showAndWait();
				if(r.get()==buttonTypeAdd){
					result = al.add(name, true);
					obsList = FXCollections.observableArrayList(al.getList());
					listView.setItems(obsList);
					//select album after addition
					listView.refresh();
					listView.getSelectionModel().select(result);
					//notice the user that their addition is successful
					EAStatus.setTextFill(Color.web("#32CD32"));
					EAStatus.setText("Successfully Added");
					//clear the text field
					nameTextField.setText("");
					//display the text for 1.5 seconds
					Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> EAStatus.setText("")));
					timeline.play();
				}	
			}
		}else{
			//an edit action
			String name = nameTextField.getText();
			int result = al.edit(name, o,false);
			if(result==-1){
				EAStatus.setTextFill(Color.web("#ff0000"));
				EAStatus.setText("Fail to edit album: album already existed");
			}else if(result==0){
				EAStatus.setTextFill(Color.web("#ff0000"));
				EAStatus.setText("Fail to edit album: Album name cannot be empty");
			}else if(result==-2){
				EAStatus.setTextFill(Color.web("#ff0000"));
				EAStatus.setText("Fail to edit album: Cannot rename the album with the same name");
			}else{
				Alert ec = new Alert(AlertType.CONFIRMATION);
				ec.setTitle("Rename album Confirmation");
				ec.setHeaderText("Are you sure you want to rename the album with the following name?");
				ec.setContentText("Name:   "+name+"\n");
				//set the button that user can choose
				ButtonType buttonTypeRename = new ButtonType("Rename", ButtonData.OK_DONE);
				ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
				ec.getButtonTypes().setAll(buttonTypeRename,buttonTypeCancel);
				Optional<ButtonType> r = ec.showAndWait();
				if(r.get()==buttonTypeRename){
					result = al.edit(name,o,true);
					obsList = FXCollections.observableArrayList(al.getList());
					listView.setItems(obsList);
					//select song after addition
					listView.refresh();
					listView.getSelectionModel().select(result);
					//notice the user that their addition is successful
					EAStatus.setTextFill(Color.web("#32CD32"));
					EAStatus.setText("Successfully Edited");
					//display the text for 1.5 seconds
					Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> EAStatus.setText("")));
					timeline.play();
				}
			}
		}		
	}
	/**
	 * An event handler to handle the cancel event,cancel the edit or add
	 * @param e the action that has been triggered
	 */
	public void cancelAction(ActionEvent e){
		//check which action user wants to cancel
		if(addButton.isSelected()){
			nameTextField.setText("");
			EAStatus.setText("");
		}else{
			nameTextField.setText(listView.getSelectionModel().getSelectedItem().getName());
			EAStatus.setText("");
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
	 * @throws Exception
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