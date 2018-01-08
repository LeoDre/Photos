package controller;

import java.util.*;
import java.nio.file.*;
import java.io.*;
import java.text.SimpleDateFormat;
import model.album;
import model.albumList;
import model.userlist;
import model.imgR;
import model.tag;
import model.taglist;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * controls the view of the list of photos
 * @author Ran Sa, Jami Nicastro
 *
 */
public class userPListController{
	@FXML private ListView<imgR> listView;
	@FXML private ListView<tag> tagList;
	@FXML private ComboBox<album> selectAB;
	@FXML private Label nameInfo;
	@FXML private Label logInfo;
	@FXML private Label captionInfo;
	@FXML private Label ADDStatus;
	@FXML private Label dateInfo;
	@FXML private Label capStatus;
	@FXML private Label tagStatus;
	@FXML private TextField captionField;
	@FXML private TextField catField;
	@FXML private TextField valueField;
	private ObservableList<imgR> obsList;
	private ObservableList<tag> obstagList;
	private ObservableList<album> option;
	private userlist ul;
	private Stage pstage;
	private albumList al;
	private album ab;
	private int index;
	/**
	 * start the GUI and the controller by filling the following data
	 * @param mainstage the stage it is on
	 * @param us a list of all users
	 * @param as an albumlist of this user
	 * @param a an album of this user you want to diplay 
	 * @param index the index of the user in the userlist
	 */
	public void start(Stage mainstage,userlist us,albumList as,album a,int index){
		pstage = mainstage;
		al = as;
		ab=a;
		this.index = index;
		ul = us;
		al = us.getUser(index).getAlbumList();
		String userName = us.getUser(index).getName();
		// create an ObservableList from an ArrayList              
		obsList = FXCollections.observableArrayList(ab.getList());
		listView.setItems(obsList);
		option = FXCollections.observableArrayList(al.getList());
		selectAB.setItems(option);
		this.updateImage();
		//check if the list contains any album
		if(ab.getSize()>0){
			//if the list is not empty
			//select the first item
			listView.getSelectionModel().select(0);
			nameInfo.setText(listView.getItems().get(0).getName());
			dateInfo.setText(listView.getItems().get(0).getDate());
			captionInfo.setText(listView.getItems().get(0).getCap());
			captionField.setText(listView.getItems().get(0).getCap());
			if(listView.getItems().get(0).getList().getList().size()<=0){
				tagList.setPlaceholder(new Label("No Content In List"));
			}else{
				obstagList = FXCollections.observableArrayList(listView.getItems().get(0).getList().getList());
				tagList.setItems(obstagList);
				tagList.refresh();
				tagList.getSelectionModel().select(0);
			}
		}else{
			//if the list is empty
			nameInfo.setText("");
			captionInfo.setText("");
			dateInfo.setText("");
			captionField.setText("");
			tagList.setPlaceholder(new Label("No Content In List"));
		}
		//set all status bar to default
		ADDStatus.setText("");
		//set login user name
		logInfo.setText(userName);
		//add listener for the list
		listView.getSelectionModel().selectedIndexProperty().addListener((obs,oldV,newV)->showInfo(mainstage));
	}
	/**
	 * show the information of the selected image
	 * @param mainStage the stage it is on
	 */
	private void showInfo(Stage mainStage){
		int i = listView.getSelectionModel().getSelectedIndex();
		if(i>=0){
			nameInfo.setText(listView.getItems().get(i).getName());
			dateInfo.setText(listView.getItems().get(i).getDate());
			captionInfo.setText(listView.getItems().get(i).getCap());
			captionField.setText(listView.getItems().get(i).getCap());
			if(listView.getItems().get(i).getList().getSize()<=0){
				ObservableList<tag> ept = FXCollections.observableArrayList(new ArrayList<tag>());
				tagList.setItems(ept);
			}else{
				obstagList = FXCollections.observableArrayList(listView.getItems().get(i).getList().getList());
				tagList.setItems(obstagList);
				tagList.refresh();
				tagList.getSelectionModel().select(0);
			}
		}else{
			nameInfo.setText("");
			dateInfo.setText("");
			captionInfo.setText("");
			ADDStatus.setText("");
			captionField.setText("");
			tagList.setPlaceholder(new Label("No Content In List"));
			tagList.refresh();
		}
	}
	/**
	 * An event handler to handle the move image event, move from one album to another
	 * @param e the action that has been triggered
	 * @throws Exception
	 */
	public void move(ActionEvent e)throws Exception{
		int ai = selectAB.getSelectionModel().getSelectedIndex();
		if(ab.getSize()<=0){
			ADDStatus.setTextFill(Color.web("#ff0000"));
			ADDStatus.setText("Album is empty. No photo can be move");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
			timeline.play();
		}else if(ai<0){
			ADDStatus.setTextFill(Color.web("#ff0000"));
			ADDStatus.setText("Plase choose an album to move to");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
			timeline.play();
		}else{
			//if not empty set the alert window
			int i = listView.getSelectionModel().getSelectedIndex();
			album tab = al.getList().get(ai);
			imgR tmp = ab.getList().get(i).clone();
			//check if the photo already exist in the traget album
			for(int x =0; x<tab.getSize(); x++){
				if(tab.getList().get(x).equals(tmp)){
					ADDStatus.setTextFill(Color.web("#ff0000"));
					ADDStatus.setText("Photo already exist in traget album");
					//display the text for 3 seconds
					Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
					timeline.play();
					return;
				}
			}
			Alert dc = new Alert(AlertType.CONFIRMATION);
			dc.setTitle("Deletion Confirmation");
			dc.setHeaderText("Are you sure you want to move the following photo?");
			dc.setContentText("Photo Name:   "+listView.getItems().get(i).getName()+"\n"+"Date: "+listView.getItems().get(i).getDate()+"\nThis photo will be removed from this album");
			//set the button that user can choose
			ButtonType buttonTypeDelete = new ButtonType("Move", ButtonData.OK_DONE);
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			dc.getButtonTypes().setAll(buttonTypeDelete,buttonTypeCancel);
			Optional<ButtonType> result = dc.showAndWait();
			//select song after deletion
			if(result.get()==buttonTypeDelete){
				tab.addP(tmp);
				int s;
				if(i==al.getSize()-1 && i!=0){
					s = i-1;
				}else if(i==al.getSize()-1 && i==0){
					s = -1;
				}else{
					s = i;
				}
				ab.remove(i);
				//refresh listView
				obsList = FXCollections.observableArrayList(ab.getList());
				listView.setItems(obsList);
				this.updateImage();
				listView.refresh();
				listView.getSelectionModel().select(s);
				//notice the user that their deletion is successful
				ADDStatus.setTextFill(Color.web("#32CD32"));
				ADDStatus.setText("Successfully Moved");
				//display the text for 1.5 seconds
				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> ADDStatus.setText("")));
				timeline.play();
			}
		}
	}
	/**
	 * An event handler to handle the add to pool event, add a image from system to the program
	 * @param e the action that has been triggered
	 */
	public void addToPool(ActionEvent e){
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
		fileChooser.setTitle("Add Photo");
		Stage cpstage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		File file = fileChooser.showOpenDialog(cpstage);
		if(file==null){
			return;
		}
		Path src = file.toPath();
		Path imgPath = Paths.get(ul.getUser(index).getPool()+"/"+file.getName());
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String date = sdf.format(file.lastModified());
		try{
			Files.copy(src, imgPath);
		}catch(IOException ioe){
			//file already exist NEED TO TELL USER TO RENAME
			ADDStatus.setTextFill(Color.web("#ff0000"));
			ADDStatus.setText("Failed to add Photo: Photo already exist");
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
			timeline.play();
			return;
		}
		imgR image = new imgR(file.getName(),date,imgPath);
		//add to album
		int result = ab.addP(image);
		obsList = FXCollections.observableArrayList(ab.getList());
		listView.setItems(obsList);
		this.updateImage();
		//select photo after addition
		listView.refresh();
		listView.getSelectionModel().select(result);
		//notice the user that their addition is successful
		ADDStatus.setTextFill(Color.web("#32CD32"));
		ADDStatus.setText("Successfully Added");
		//display the text for 1.5 seconds
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> ADDStatus.setText("")));
		timeline.play();
		
	}
	/**
	 * An event handler to handle the delete event, delete the photo reference if this is the last reference delete the physical photo from the pool
	 * @param e the action that has been triggered
	 */
	public void delete(ActionEvent e){
		//first delete from the album
		//check if the list is empty
		if(ab.getSize()<=0){
			ADDStatus.setTextFill(Color.web("#ff0000"));
			ADDStatus.setText("Album is empty. No photo can be deleted.");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
			timeline.play();
		}else{
			//if not empty set the alert window
			int i = listView.getSelectionModel().getSelectedIndex();
			Alert dc = new Alert(AlertType.CONFIRMATION);
			dc.setTitle("Deletion Confirmation");
			dc.setHeaderText("Are you sure you want to delete the following photo?");
			dc.setContentText("Photo Name:   "+listView.getItems().get(i).getName()+"\n"+"Date: "+listView.getItems().get(i).getDate());
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
				//check if this is the last reference point points to this photo, if yes delete from disk
				imgR tmp = ab.getList().get(i);
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
						ab.remove(i);
						ADDStatus.setTextFill(Color.web("#ff0000"));
						ADDStatus.setText("No such file. Deleting the reference");
						//display the text for 3 seconds
						Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
						timeline.play();
					} catch (IOException e1) {
						ab.remove(i);
						ADDStatus.setTextFill(Color.web("#ff0000"));
						ADDStatus.setText("Fail to delete: Please manually delete from disk");
						//display the text for 3 seconds
						Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
						timeline.play();
					}
				}
				//delete from list
				ab.remove(i);
				//refresh listView
				obsList = FXCollections.observableArrayList(ab.getList());
				listView.setItems(obsList);
				this.updateImage();
				listView.refresh();
				listView.getSelectionModel().select(s);
				//notice the user that their deletion is successful
				ADDStatus.setTextFill(Color.web("#32CD32"));
				ADDStatus.setText("Successfully Deleted");
				//display the text for 1.5 seconds
				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> ADDStatus.setText("")));
				timeline.play();
			}
		}
	}
	/**
	 * An event handler to handle the copy event, set the photo to copy
	 * @param e the action that has been triggered
	 */
	public void copy(ActionEvent e){
		//check if the list is empty
		if(ab.getSize()<=0){
			ADDStatus.setTextFill(Color.web("#ff0000"));
			ADDStatus.setText("Album is empty. No photo can be copied.");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
			timeline.play();
		}else{
			//uncopy all the image
			for(int i=0; i<al.getSize(); i++){
				for(int x = 0; x<al.getList().get(i).getSize(); x++){
					al.getList().get(i).getList().get(x).setCopied(false);
				}
			}
			//if not empty set the alert window
			int i = listView.getSelectionModel().getSelectedIndex();
			//set the selected on to copied
			ab.getList().get(i).setCopied(true);
			ADDStatus.setTextFill(Color.web("#32CD32"));
			ADDStatus.setText("Copied");
			//display the text for 1.5 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> ADDStatus.setText("")));
			timeline.play();
		}
	}
	/**
	 * An event handler to handle the paste event, paste the copied photo to the target album
	 * @param e the action that has been triggered
	 */
	public void paste(ActionEvent e){
		//check if there is anything be copied
		imgR tmp = null;
		for(int i=0; i<al.getSize(); i++){
			for(int x = 0; x<al.getList().get(i).getSize(); x++){
				if(al.getList().get(i).getList().get(x).getCopied()){
					try{
						tmp = al.getList().get(i).getList().get(x).clone();
					}catch(CloneNotSupportedException cns){
						break;
					}
					break;
				}
			}
		}
		if(tmp==null){
			ADDStatus.setTextFill(Color.web("#ff0000"));
			ADDStatus.setText("Nothing has been copied yet");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
			timeline.play();
		}else{
			if(ab.getList().indexOf(tmp)>=0){
				//photo already exist
				ADDStatus.setTextFill(Color.web("#ff0000"));
				ADDStatus.setText("Photo Already Exist");
				//display the text for 3 seconds
				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
				timeline.play();
			}else{
				int result = ab.addP(tmp);
				obsList = FXCollections.observableArrayList(ab.getList());
				listView.setItems(obsList);
				this.updateImage();
				//select photo after addition
				listView.refresh();
				listView.getSelectionModel().select(result);
				//notice the user that their addition is successful
				ADDStatus.setTextFill(Color.web("#32CD32"));
				ADDStatus.setText("Successfully Pasted");
				//display the text for 1.5 seconds
				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> ADDStatus.setText("")));
				timeline.play();
			}
		}
	}
	/**
	 * An event handler to handle the save the caption event, save the caption to the photo
	 * @param e the action that has been triggered
	 */
	public void saveC(ActionEvent e){
		if(ab.getSize()<=0){
			capStatus.setTextFill(Color.web("#ff0000"));
			capStatus.setText("Album is empty");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
			timeline.play();
			return;
		}
		//edit the caption
		int i = listView.getSelectionModel().getSelectedIndex();
		String cap = captionField.getText();
		int result = listView.getItems().get(i).setCaption(cap,false);
		if(result==-1){
			capStatus.setTextFill(Color.web("#ff0000"));
			capStatus.setText("Caption cannot be Empty");
		}else if(result==0){
			capStatus.setTextFill(Color.web("#ff0000"));
			capStatus.setText("Nothing has Changed");
		}else{
			Alert ec = new Alert(AlertType.CONFIRMATION);
			ec.setTitle("Caption Confirmation");
			ec.setHeaderText("Are you sure you want to save caption?");
			//set the button that user can choose
			ButtonType buttonTypeRename = new ButtonType("Save", ButtonData.OK_DONE);
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			ec.getButtonTypes().setAll(buttonTypeRename,buttonTypeCancel);
			Optional<ButtonType> r = ec.showAndWait();
			if(r.get()==buttonTypeRename){
				listView.getItems().get(i).setCaption(cap,true);
				obsList = FXCollections.observableArrayList(ab.getList());
				listView.setItems(obsList);
				this.updateImage();
				//select song after addition
				listView.refresh();
				listView.getSelectionModel().select(result);
				//notice the user that their addition is successful
				capStatus.setTextFill(Color.web("#32CD32"));
				capStatus.setText("Successfully Edited");
				//display the text for 1.5 seconds
				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> capStatus.setText("")));
				timeline.play();
			}
		}
	}
	/**
	 * An event handler to handle the cancel save event, cancel the save caption
	 * @param e the action that has been triggered
	 */
	public void cancelC(ActionEvent e){
		if(ab.getSize()>0){
		int i = listView.getSelectionModel().getSelectedIndex();
		captionField.setText(listView.getItems().get(i).getCap());
		}
		capStatus.setText("");
	}
	/**
	 * An event handler to handle the add tag event
	 * @param e the action that has been triggered
	 */
	public void addTag(ActionEvent e){
		//check the original selected album
		int o = listView.getSelectionModel().getSelectedIndex();
		taglist tl = listView.getItems().get(o).getList();
		String cat = catField.getText();
		String value = valueField.getText();
		int result = tl.add(cat, value,false);
		//check if the information user provided are valid
		if(result==-1){
			tagStatus.setTextFill(Color.web("#ff0000"));
			tagStatus.setText("Tag already exist");
		}else if(result==0){
			tagStatus.setTextFill(Color.web("#ff0000"));
			tagStatus.setText("None of category or value can be empty");
		}else{
			Alert ac = new Alert(AlertType.CONFIRMATION);
			ac.setTitle("Add Tag Confirmation");
			ac.setHeaderText("Are you sure you want to add the following tag to your photo?");
			ac.setContentText("Tag Category:   "+cat+"\nTag Value:	"+value);
			//set the button that user can choose
			ButtonType buttonTypeAdd = new ButtonType("Add", ButtonData.OK_DONE);
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			ac.getButtonTypes().setAll(buttonTypeAdd,buttonTypeCancel);
			Optional<ButtonType> r = ac.showAndWait();
			if(r.get()==buttonTypeAdd){
				result = tl.add(cat, value,true);
				obstagList = FXCollections.observableArrayList(listView.getItems().get(o).getList().getList());
				tagList.setItems(obstagList);
				tagList.refresh();
				//select album after addition
				tagList.getSelectionModel().select(result);
				//set text field to empty
				catField.setText("");
				valueField.setText("");
				//notice the user that their addition is successful
				tagStatus.setTextFill(Color.web("#32CD32"));
				tagStatus.setText("Successfully Added");
				//display the text for 1.5 seconds
				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> tagStatus.setText("")));
				timeline.play();
			}
		}
	}
	/**
	 * An event handler to handle the delete tag event
	 * @param e the action that has been triggered
	 */
	public void deleteTag(ActionEvent e){
		int o = listView.getSelectionModel().getSelectedIndex();
		taglist tl = listView.getItems().get(o).getList();
		//check if the list is empty
		if(tl.getSize()<=0){
			tagStatus.setTextFill(Color.web("#ff0000"));
			tagStatus.setText("No tag can be deleted.");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> tagStatus.setText("")));
			timeline.play();
		}else{
			//if not empty set the alert window
			int i = tagList.getSelectionModel().getSelectedIndex();
			Alert dc = new Alert(AlertType.CONFIRMATION);
			dc.setTitle("Deletion Confirmation");
			dc.setHeaderText("Are you sure you want to delete the following tag?");
			dc.setContentText("Category:   "+tl.getList().get(i).getCat()+"\nValue:	"+tl.getList().get(i).getValue());
			//set the button that user can choose
			ButtonType buttonTypeDelete = new ButtonType("Delete", ButtonData.OK_DONE);
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			dc.getButtonTypes().setAll(buttonTypeDelete,buttonTypeCancel);
			Optional<ButtonType> result = dc.showAndWait();
			//select song after deletion
			if(result.get()==buttonTypeDelete){
				int s;
				if(i==tl.getSize()-1 && i!=0){
					s = i-1;
				}else if(i==tl.getSize()-1 && i==0){
					s = -1;
				}else{
					s = i;
				}
				//delete from list
				tl.remove(i);
				//refresh listView
				obstagList = FXCollections.observableArrayList(listView.getItems().get(o).getList().getList());
				tagList.setItems(obstagList);
				tagList.refresh();
				//select album after addition
				tagList.getSelectionModel().select(s);
				//notice the user that their addition is successful
				tagStatus.setTextFill(Color.web("#32CD32"));
				tagStatus.setText("Successfully Deleted");
				//display the text for 1.5 seconds
				Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), evt -> tagStatus.setText("")));
				timeline.play();
			}
		}
	}
	/**
	 * An event handler to handle the view album event, open up a new window to view the photo
	 * @param e the action that has been triggered
	 * @throws Exception
	 */
	public void view(ActionEvent e)throws Exception{
		//check if the list is empty
		if(ab.getSize()<=0){
			ADDStatus.setTextFill(Color.web("#ff0000"));
			ADDStatus.setText("Nothing can be viewed");
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
			timeline.play();
			return;
		}
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/photoView.fxml"));
        // initializing the controller
        AnchorPane root = (AnchorPane)loader.load();
        photoViewController pvc = loader.getController();
        pvc.start(ab,listView.getSelectionModel().getSelectedIndex());
        Scene scene = new Scene(root);
        // this is the popup stage
        Stage popupStage = new Stage();
        pvc.setStage(popupStage);
        // Giving the popup controller access to the popup stage (to allow the controller to close the stage) 
        popupStage.initOwner(pstage);
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setTitle("Photos-Search");
        popupStage.setScene(scene);
		popupStage.setResizable(false);
        popupStage.showAndWait();
	}
	/**
	 * An event handler to handle the search event, open up a new window for search
	 * @param e the action that has been triggered
	 * @throws Exception
	 */
	public void search(ActionEvent e)throws Exception{
		//check if the list is empty
		if(ab.getSize()<=0){
			ADDStatus.setTextFill(Color.web("#ff0000"));
			ADDStatus.setText("Nothing can be searched");
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> ADDStatus.setText("")));
			timeline.play();
			return;
		}
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/searchView.fxml"));
		// initializing the controller
		AnchorPane root = (AnchorPane)loader.load();
		searchViewController svc = loader.getController();
		Scene scene = new Scene(root);
		// this is the popup stage
		Stage popupStage = new Stage();
		svc.start(popupStage,al);
		// Giving the popup controller access to the popup stage (to allow the controller to close the stage) 
		popupStage.initOwner(pstage);
		popupStage.initModality(Modality.WINDOW_MODAL);
		popupStage.setTitle("Photos-Search");
		popupStage.setScene(scene);
		popupStage.setResizable(false);
		popupStage.showAndWait();
	}
	/**
	 * An event handler to handle the back event, switch back to the prev windows
	 * @param e the action that has been triggered
	 * @throws Exception
	 */
	public void back(ActionEvent e)throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/usersubAlbum.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		userAlbumController uac = loader.getController();
		uac.start(pstage,ul,index);
		uac.setSelect(ab);
		Scene scene = new Scene(root);
		pstage.setScene(scene);
		pstage.setTitle("Photos-UserSystem");
		pstage.setResizable(false);
		pstage.show();
	}
	/**
	 * An event handler to handle the log out event
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
	/**
	 * update the list with thumbnail on the left
	 */
	private void updateImage(){
		listView.setCellFactory(param -> new ListCell<imgR>(){
				private ImageView imageView = new ImageView();
				@Override
				public void updateItem(imgR img, boolean empty) {
					super.updateItem(img, empty);
					if (empty) {
						setText(null);
						setGraphic(null);
					} else {
						try{
							FileInputStream input = new FileInputStream(img.getPath().toString());
							Image image = new Image(input);
							imageView.setFitHeight(100);
							imageView.setFitWidth(100);
							imageView.setImage(image);
							input.close();
						}catch(IOException ioe){
							setGraphic(null);
						}finally{
							setText(img.getName());
						}
						setGraphic(imageView);
					}
				}
		});
	}
}