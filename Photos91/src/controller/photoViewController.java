package controller;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.io.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.imgR;
import model.album;
/**
 * Controls the photo view(when user want to view a single image)
 * @author Ran Sa, Jami Nicastro
 *
 */
public class photoViewController{
	@FXML private ImageView view;
	@FXML private Label Status;
	private Stage pstage;
	private int index;
	private album ab;
	/**
	 * start the GUI by filling in data
	 * @param ab the album contains the photo that been selected
	 * @param index the index of the selected photo in this list
	 * @throws Exception
	 */
	public void start(album ab, int index)throws Exception{
		this.index = index;
		this.ab = ab;
		imgR img = ab.getList().get(index);
		FileInputStream input = new FileInputStream(img.getPath().toString());
		Image image = new Image(input);
		view.setImage(image);
		input.close();
	}
	/**
	 * store the stage of this controller
	 * @param pstage The stage of this controller
	 */
	public void setStage(Stage pstage){
		this.pstage = pstage;
	}
	/**
	 * An event handler to handle the back event, set window to prev stage
	 * @param e the action that has been triggered
	 */
	public void back(ActionEvent e){
		pstage.close();
	}
	/**
	 * An event handler to handle the next event, view next photo in the list
	 * @param e the action that has been triggered
	 */
	public void next(ActionEvent e){
		if(index==ab.getSize()-1){
			//reach the end of the album
			Status.setTextFill(Color.web("#ff0000"));
			Status.setText("End of the Album");
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> Status.setText("")));
			timeline.play();
			return;
		}
		index++;
		imgR img = ab.getList().get(index);
		try{
			Status.setText("");
			FileInputStream input = new FileInputStream(img.getPath().toString());
			Image image = new Image(input);
			view.setImage(image);
			input.close();
		}catch(IOException ioe){
			Status.setTextFill(Color.web("#ff0000"));
			Status.setText("Fail to access the image");
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> Status.setText("")));
			timeline.play();
		}
	}
	/**
	 * An event handler to handle the prev event, view the previous photo
	 * @param e the action that has been triggered
	 */
	public void prev(ActionEvent e){
		if(index==0){
			//reach the end of the album
			Status.setTextFill(Color.web("#ff0000"));
			Status.setText("Beginning of the album");
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> Status.setText("")));
			timeline.play();
			return;
		}
		index--;
		imgR img = ab.getList().get(index);
		try{
			Status.setText("");
			FileInputStream input = new FileInputStream(img.getPath().toString());
			Image image = new Image(input);
			view.setImage(image);
			input.close();
		}catch(IOException ioe){
			Status.setTextFill(Color.web("#ff0000"));
			Status.setText("Fail to access the image");
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> Status.setText("")));
			timeline.play();
		}
	}
}