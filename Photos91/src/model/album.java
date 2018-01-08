package model;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * album class is the class that hold all the photos(Reference of photo as imgR)
 * It used an ArrayList to hold all the imgR object
 * @author Ran Sa, Jami Nicastro
 *
 */
public class album implements Comparable<album>,Serializable{
	/**
	 * Field: 
	 * 		serialVersionUID: serial id, used to serialize album object
	 * 		name: anem of the album
	 * 		nop: number of photos in this album
	 * 		rip: the ArrayList hold the imgR objects
	 */
	private static final long serialVersionUID = 634773949461402058L;
	//album name
	private String name;
	//number of photos
	private int nop;
	//earliest date
	private ArrayList<imgR> rip;
	/**
	 * A constructor construct and initialize the album object 
	 * @param name The name of the album
	 */
	public album(String name){
		this.name = name;
		nop=0;
		rip = new ArrayList<imgR>();
	}
	/**
	 * This function return the whole album( ArrayList of imgR)
	 * @return An ArrayList of imgR as the container of the album
	 */
	public ArrayList<imgR> getList(){
		return rip;
	}
	/**
	 * Get the name of the album
	 * @return The name of the album
	 */
	public String getName(){
		return name;
	}
	/**
	 * Take the input string and set the name to the input strinig
	 * @param s A string of new name
	 */
	public void setName(String s){
		name = s;
	}
	/**
	 * Get the number of photos in this album
	 * @return Number of photos in this album
	 */
	public int getCount(){
		return nop;
	}
	/**
	 * Get the earliest and the latest date of all the photo
	 * @return A String represent the range of date
	 */
	public String getRange(){
		String r = "";
		if(rip.size()<=0){
			return r;
		}
		ArrayList<Date> ad = new ArrayList<Date>();
		for(int i = 0; i<rip.size(); i++){
			try{
				ad.add(rip.get(i).getDated());
			}catch(ParseException pe){
				//do nothing
			}
		}
		Collections.sort(ad);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String b = sdf.format(ad.get(0));
		String e = sdf.format(ad.get(ad.size()-1));
		r = b+" --- "+e;
		return r;
	}
	//return the index of the new added photo
	/**
	 * This function add a image reference to the album and returns the index of just added image reference(imgR object) 
	 * @param img The image reference user want to add to the album
	 * @return The index of the new added photo 
	 */
	public int addP(imgR img){
		rip.add(img);
		Collections.sort(rip);
		nop++;
		return rip.indexOf(img);
	}
	/**
	 * Remove the imgR of a certain index
	 * @param i The index of the image reference you want to remove
	 */
	public void remove(int i){
		rip.remove(i);
		nop--;
	}
	/**
	 * Get the size of the album
	 * @return The size of the album as a int
	 */
	public int getSize(){
		return rip.size();
	}
	/**
	 * Compare the album for sorting by name
	 */
	public int compareTo(album a){
		return name.compareTo(a.getName());
	}
	/**
	 * Check the equality of the album by name
	 */
	@Override
	public boolean equals(Object o){
		if(o==null||(!(o instanceof album))){
			return false;
		}
		album tmp = (album)o;
		return name.equals(tmp.getName());
	}
	/**
	 * convert album to String (name only)
	 */
	@Override
	public String toString(){
		return name;
	}
}