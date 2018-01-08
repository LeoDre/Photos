package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.album;
import model.albumList;
import model.imgR;
import model.tag;
import model.taglist;
/**
 * Search by date or by tag, search among all the photo the user has
 * @author Ran Sa, Jami Nicastro
 *
 */
public class searchViewController{
	@FXML private ListView<imgR> rList;
	@FXML private ListView<tag> tList;
	@FXML private RadioButton sbd;
	@FXML private RadioButton sbt;
	@FXML private Label dStatus;
	@FXML private Label tStatus;
	@FXML private Label aStatus;
	@FXML private TextField catField;
	@FXML private TextField valueField;
	@FXML private TextField nameField;
	@FXML private DatePicker from;
	@FXML private DatePicker to;
	@FXML private Button searchD;
	@FXML private Button searchT;
	@FXML private Button addf;
	@FXML private Button deletef;
	private Stage pstage;
	private albumList al;
	private ArrayList<tag> tgl;
	private ArrayList<imgR> im;
	private ObservableList<imgR> obsList;
	private ObservableList<tag> obstagList;
	/**
	 * Start the GUI and the controller by filling in the stage it is on and the albumlist it can perform search later
	 * @param pstage the stage it is on
	 * @param al the albumlist it can perform search on
	 */
	public void start(Stage pstage, albumList al){
		this.pstage = pstage;
		this.al = al;
		//a new list of imgR
		im = new ArrayList<imgR>();
		//set to listview
		obsList = FXCollections.observableArrayList(im);
		rList.setItems(obsList);
		//a new list of tag
		tgl = new ArrayList<tag>();
		obstagList = FXCollections.observableArrayList(tgl);
		tList.setItems(obstagList);
		//add radio buttons to a group and select add button by default
		ToggleGroup group = new ToggleGroup();
		sbd.setToggleGroup(group);
		sbd.setSelected(true);
		catField.setText("");
		valueField.setText("");
		tList.setDisable(true);
		catField.setDisable(true);
		valueField.setDisable(true);
		addf.setDisable(true);
		deletef.setDisable(true);
		searchT.setDisable(true);
		searchD.setDisable(false);
		from.setDisable(false);
		to.setDisable(false);
		sbt.setToggleGroup(group);
	}
	/**
	 * An event handler to handle the switch event, switch between search by date and search by tag
	 * @param e the action that has been triggered
	 */
	public void switchAction(ActionEvent e){
		if(sbd.isSelected()){
			catField.setText("");
			valueField.setText("");
			tgl = new ArrayList<tag>();
			obstagList = FXCollections.observableArrayList(tgl);
			tList.setItems(obstagList);
			tList.refresh();
			tList.setDisable(true);
			catField.setDisable(true);
			valueField.setDisable(true);
			addf.setDisable(true);
			deletef.setDisable(true);
			searchT.setDisable(true);
			searchD.setDisable(false);
			from.setDisable(false);
			to.setDisable(false);
		}else{
			from.setValue(null);
			to.setValue(null);
			tList.setDisable(false);
			catField.setDisable(false);
			valueField.setDisable(false);
			addf.setDisable(false);
			deletef.setDisable(false);
			searchT.setDisable(false);
			searchD.setDisable(true);
			from.setDisable(true);
			to.setDisable(true);
		}
	}
	/**
	 * An event handler to handle the search by date event
	 * @param e the action that has been triggered
	 * @throws Exception
	 */
	public void searchByDate(ActionEvent e)throws Exception{
		if(from.getValue()==null||to.getValue()==null){
			dStatus.setTextFill(Color.web("#ff0000"));
			dStatus.setText("Date range cannot be empty");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> dStatus.setText("")));
			timeline.play();
			return;
		}
		DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String fromD = (from.getValue()).format(df);
		Date f = sdf.parse(fromD);
		String toD = (to.getValue()).format(df);
		Date t = sdf.parse(toD);
		im = new ArrayList<imgR>();
		for(int x=0; x<al.getSize();x++){
			album tpa = al.getList().get(x);
			for(int i = 0; i<tpa.getSize(); i++){
				imgR tmp = tpa.getList().get(i);
				if(sdf.parse(tmp.getDate()).compareTo(f)>=0&&sdf.parse(tmp.getDate()).compareTo(t)<=0&&im.indexOf(tmp)<0){
					im.add(tmp);
				}
			}
		}
		if(im.size()<=0){
			obsList = FXCollections.observableArrayList(im);
			rList.setItems(obsList);
			rList.refresh();
			dStatus.setTextFill(Color.web("#ff0000"));
			dStatus.setText("No Photo Found");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> dStatus.setText("")));
			timeline.play();
			return;
		}
		this.update(im);
	}
	/**
	 * An event handler to handle the search by tag event
	 * @param e the action that has been triggered
	 */
	public void searchByTag(ActionEvent e){
		if(tgl.size()<=0){
			tStatus.setTextFill(Color.web("#ff0000"));
			tStatus.setText("Filter is empty");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> tStatus.setText("")));
			timeline.play();
			return;
		}
		im = new ArrayList<imgR>();
		for(int x=0; x<al.getSize(); x++){
			album tab = al.getList().get(x);
			for(int y=0; y<tab.getSize();y++){
				imgR tmi = tab.getList().get(y);
				taglist ttgl = tmi.getList();
				boolean b = true;
				if(ttgl.getSize()<=0){
					b = false;
				}
				for(int i=0;i<tgl.size();i++){
					boolean b2 = false;
					tag tar = tgl.get(i);
					for(int z = 0; z<ttgl.getSize();z++){
						if(ttgl.getList().get(z).equals(tar)){
							b2 = true;
							break;
						}
					}
					if (!b2){
						b = false;
						break;
					}
				}
				if(b&&im.indexOf(tmi)<0){
					im.add(tmi);
				}
			}
		}
		if(im.size()<=0){
			obsList = FXCollections.observableArrayList(im);
			rList.setItems(obsList);
			rList.refresh();
			tStatus.setTextFill(Color.web("#ff0000"));
			tStatus.setText("No Photo Found");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> tStatus.setText("")));
			timeline.play();
			return;
		}
		this.update(im);
	}
	/**
	 * An event handler to handle the add a tag filter for search event
	 * @param e the action that has been triggered
	 */
	public void addt(ActionEvent e){
		if(catField.getText().equals("")||valueField.getText().equals("")){
			tStatus.setTextFill(Color.web("#ff0000"));
			tStatus.setText("Invalid Tag");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> tStatus.setText("")));
			timeline.play();
			return;
		}
		tag tmp = new tag(catField.getText(),valueField.getText());
		if(tgl.indexOf(tmp)>=0){
			tStatus.setTextFill(Color.web("#ff0000"));
			tStatus.setText("Tag Already Exist");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> tStatus.setText("")));
			timeline.play();
			return;
		}
		tgl.add(tmp);
		catField.setText("");
		valueField.setText("");
		obstagList = FXCollections.observableArrayList(tgl);
		tList.setItems(obstagList);
		tList.refresh();
	}
	/**
	 * An event handler to handle the delete a tag from the filter event
	 * @param e the action that has been triggered
	 */
	public void deletet(ActionEvent e){
		if(tgl.size()<=0){
			tStatus.setTextFill(Color.web("#ff0000"));
			tStatus.setText("Nothing can be deleted");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> tStatus.setText("")));
			timeline.play();
			return;
		}
		tgl.remove(tList.getSelectionModel().getSelectedIndex());
		obstagList = FXCollections.observableArrayList(tgl);
		tList.setItems(obstagList);
		tList.refresh();
	}
	/**
	 * Set the thumbnail to the listview
	 * @param il the list that need to be set to the listview
	 */
	private void update(ArrayList<imgR> il){
		obsList = FXCollections.observableArrayList(il);
		rList.setItems(obsList);
		rList.setCellFactory(param -> new ListCell<imgR>(){
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
	/**
	 * An event handler to handle the add the result to a album event
	 * @param e the action that has been triggered
	 * @throws Exception
	 */
	public void addAB(ActionEvent e)throws Exception{
		if(im.size()<=0){
			aStatus.setTextFill(Color.web("#ff0000"));
			aStatus.setText("No Photo is selected");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> aStatus.setText("")));
			timeline.play();
			return;
		}
		if(nameField.getText().equals("")){
			aStatus.setTextFill(Color.web("#ff0000"));
			aStatus.setText("Name cannot be empty");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> aStatus.setText("")));
			timeline.play();
			return;
		}
		album a = new album(nameField.getText());
		if(al.getList().indexOf(a)>=0){
			aStatus.setTextFill(Color.web("#ff0000"));
			aStatus.setText("Album already exist");
			//display the text for 3 seconds
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> aStatus.setText("")));
			timeline.play();
			return;
		}
		for(int i = 0; i<im.size();i++){
			imgR tmp = im.get(i).clone();
			a.addP(tmp);
		}
		al.add(a);
		nameField.setText("");
		pstage.close();
	}
	/**
	 * An event handler to handle the back event, go back to the previous window
	 * @param e the action that has been triggered
	 */
	public void back(ActionEvent e){
		pstage.close();
	}
}