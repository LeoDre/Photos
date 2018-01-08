package model;

import java.io.Serializable;
import java.util.*;
/**
 * taglist contains a list of tag and imgR can access its tag from the taglist
 * @author Ran Sa, Jami Nicastro
 *
 */
public class taglist implements Serializable{
	/**
	 * Field:
	 * 		serialVersionUID: serial id used for serialization
	 * 		tl: an ArrayList of tag
	 */
	private static final long serialVersionUID = -775486837625305536L;
	ArrayList<tag> tl;
	/**
	 * the constructor create and initialize an taglist object by initialize the ArrayList of tag
	 */
	public taglist(){
		tl = new ArrayList<tag>();
	}
	/**
	 * get the ArrayList of the tag 
	 * @return an ArrayList of tag
	 */
	public ArrayList<tag> getList(){
		return tl;
	}
	/**
	 * Get the size of the taglist(number of tags in the taglist)
	 * @return
	 */
	public int getSize(){
		return tl.size();
	}
	//return 0 if string is empty
	//return -1 if already exist
	//return 1 if it is addable
	//return index after add
	/**
	 * Add a tag to the taglist
	 * @param cat the category of the new tag
	 * @param value the value of the new tag
	 * @param b True if user want to add this tag to the list. False if user only just want to check the tag they want to create if valid
	 * @return Return index of the tag if it is successfully added. Return 0 if any of the cat or value field is empty. Return -1 if the tag already exist. Return 1 if the tag is valid to add.
	 */
	public int add(String cat,String value, boolean b){
		if(cat.equals("")||value.equals("")){
			return 0;
		}
		tag t = new tag(cat,value);
		if(tl.indexOf(t)>=0){
			return -1;
		}
		if(b){
			tl.add(t);
			Collections.sort(tl);
			return tl.indexOf(t);
		}
			return 1;
	}
	/**
	 * Remove a certain tag from the list
	 * @param i the index number of the tag user want to delete
	 */
	public void remove(int i){
		tl.remove(i);
	}
}