package model;
import java.io.*;
import java.util.*;
/**
 * albumList holds a list of album object for user because user can have many albums
 * @author Ran Sa, Jami Nicastro
 *
 */
public class albumList implements Serializable {
	/**
	 * Field:
	 * 		serialVersionUID: serial id for serialization propose 
	 * 		al: an ArrayList of album, the container of albumList
	 */
	private static final long serialVersionUID = -860672176248790098L;
	ArrayList<album> al;
	/**
	 * The constructor initialize the ArrayList for albumList
	 */
	public albumList(){
		al = new ArrayList<album>();
	}
	/**
	 * Get the size of albumList(number of albums in it)
	 * @return The number of the albums in it
	 */
	public int getSize(){
		return al.size();
	}
	/**
	 * Get all the album in the ArrayList in albumList
	 * @return The ArrayList of albums in the albumList
	 */
	public ArrayList<album> getList(){
		return al;
	}
	//return 0 if already exist
	//return 1 on success (test)
	//return index of the song when added
	//return -1 if field is empty
	/**
	 * Check and add an empty album to the albumList
	 * @param name The name of the new album
	 * @param b True if user want to add this album to the list. False if user just want to test if this name is valid
	 * @return Return index of the album if successfully added. Return 1 if the name is valid(for testing the name). Return 0 if the name already exist. Return -1 if the name is empty. 
	 */
	public int add(String name, boolean b){
		if(name.equals("")){
			return -1;
		}
		album ab = new album(name);
		if(al.indexOf(ab)!=-1){
			return 0;
		}
		if(b==true){
			al.add(ab);
			Collections.sort(al);
			return al.indexOf(ab);
		}
		return 1;
	}
	/**
	 * Add an album object to the list
	 * @param ab The album object you want to add
	 */
	public void add(album ab){
		al.add(ab);
		Collections.sort(al);
	}
	/**
	 * Remove a certain album with certain index from the list
	 * @param i the index of the album you want to remove
	 */
	public void remove(int i){
		al.remove(i);
	}
	//return 0 if string is empty
	//return -1 if name already exist
	//return -2 if new name if same as old name
	/**
	 * Edit the name of the album with the input index
	 * @param newName The name user want to change to
	 * @param index The index of the album user want to rename
	 * @param b True if user want to change the name. False if user just want to test if this name is valid
	 * @return Return the index of the renamed album if rename success. Return number bigger than 0 if the new name if valid. Return 0 if the new name is empty. Return -1 if name already exist. Return -2 if the new name is same as the old name 
	 */
	public int edit(String newName, int index, boolean b){
		if(newName.equals(al.get(index).getName())){
			return -2;
		}else if(newName.equals("")){
			return 0;
		}else if(this.check(newName)!=-1){
			return -1;
		}
		if(b){
			al.get(index).setName(newName);
		}
		Collections.sort(al);
		return this.check(newName);
	}
	//check if the album is in the system
	//index of the album if yes
	//-1 if no
	/**
	 * Check if the name already exist
	 * @param name The name user want to check
	 * @return Return -1 if the name dosen't exist. index of the name if exist
	 */
	public int check(String name){ 
		int size = al.size();
		for(int i = 0; i<size;i++){
			if(name.equals(al.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
}