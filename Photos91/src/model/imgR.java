package model;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * imgR is a reference point of an image. It dosen't really store the image physically
 * @author Ran Sa, Jami Nicastro
 *
 */
public class imgR implements Comparable<imgR>, Serializable,Cloneable{
	/**
	 * Field:
	 * 		serialVersionUID: serial ID id is used for serialize the object
	 * 		name: name of the imgR
	 * 		caption: caption of the photo
	 * 		tl: list of tag
	 * 		date: the date of the photo in a string
	 * 		p: path of the real image file
	 * 		isCopied: a boolean field to check if the image is copied
	 */
	private static final long serialVersionUID = 6716165646293755351L;
	private String name;
	private String caption;
	private taglist tl;
	private String date;
	private String p;
	private boolean isCopied;
	/**
	 * The constructor that initialize the imgR. also initialize a taglist which contains all the tags
	 * @param name The name of the photo in a string
	 * @param date The date of the image in a string format
	 * @param p The physical path of the image in a string
	 */
	public imgR(String name,String date,Path p){
		this.name = name;
		this.date = date;
		this.p = p.toString();
		tl = new taglist();
		isCopied=false;
		caption = "N/A";
	}
	/**
	 * Check if the certain photo is copied
	 * @return true if it is copied, false if it is not
	 */
	public boolean getCopied(){
		return isCopied;
	}
	/**
	 * Change the iscopied value of the image, change to true means this image is copied, false means it is not
	 * @param b	the iscopied value user wants to set to
	 */
	public void setCopied(boolean b){
		isCopied = b;
	}
	//return 0 if repeated
	//return -1 if string is empty
	//return 1 if valid
	/**
	 * Set the caption field of the image
	 * @param c the string value of the caption user wants to set to
	 * @param b True if user wants to set the value to the input string c, False if the suer just want to test to see if this caption is valid
	 * @return Return 1 if the caption is valid/set. Return -1 if the caption has not been changed.
	 */
	public int setCaption(String c, boolean b){
		if(c.equals("")){
			return -1;
		}
		if(c.equals(caption)){
			return 0;
		}
		if(b){
			caption = c;
		}
		return 1;
	}
	/**
	 * get the path of the imgR in type Path
	 * @return Return the path of the image
	 */
	public Path getPath(){
		return Paths.get(p);
	}
	/**
	 * Get the last modified date of the image 
	 * @return A string contains the last modified date of the image
	 */
	public String getDate(){
		return date;
	}
	/**
	 * Get the last modified date of the image in type Date
	 * @return A date contains the last modified date of the image
	 * @throws ParseException
	 */
	public Date getDated()throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date d = sdf.parse(date);
		return d;
	}
	/**
	 * Get the name of the imgR usually in a format of name.jpg
	 * @return A string contains the name of teh image
	 */
	public String getName(){
		return name;
	}
	/**
	 * Get the caption of the imgR in a String
	 * @return A string contains the caption
	 */
	public String getCap(){
		return caption;
	}
	/**
	 * Get a list of tag belongs to this image in a taglist format
	 * @return A taglist contains all the tag
	 */
	public taglist getList(){
		return tl;
	}
	/**
	 * Equality check for the imgR based only on the name of the image
	 */
	public boolean equals(Object o){
		if(o==null||(!(o instanceof imgR))){
			return false;
		}
		imgR tmp = (imgR)o;
		return (name.equals(tmp.getName()));
	}
	/**
	 * compare two image by their name(for sorting)
	 */
	@Override
	public int compareTo(imgR o) {
		return name.compareTo(o.getName());
	}
	/**
	 * convert the imgR to string(Only the name)
	 */
	public String toString(){
		return name;
	}
	/**
	 * Clone the imgR. This funtion only clone the reference, not physical image is cloned.
	 */
	public imgR clone()throws CloneNotSupportedException{
		imgR img = (imgR)super.clone();
		for(int i =0;i<tl.getSize(); i++){
			img.getList().getList().set(i, new tag(tl.getList().get(i).getCat(),tl.getList().get(i).getValue()));
		}
		return img;
	}
}