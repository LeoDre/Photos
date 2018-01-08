package model;
import java.io.*;
import java.util.*;
/**
 * userlist is a list of all the users
 * @author Ran Sa, Jami Nicastro
 *
 */
public class userlist implements Serializable{
	/**
	 * Field:
	 * 		serialVersionUID: serial id for serialization
	 * 		ul: an arraylist that contains all the user
	 */
	private static final long serialVersionUID = 5024004452512589713L;
	private ArrayList<user> ul;
	/**
	 * create and initialize a userlist by initializing the arraylist of user
	 */
	public userlist(){
		ul = new ArrayList<user>();
		user admin = new user("admin");
		ul.add(admin);
	}
	/**
	 * Get the arraylist that contains all the users
	 * @return The arraylist that contains all the users
	 */
	public ArrayList<user> getList(){
		return ul;
	}
	//return 0 if already exist
	//return 1 on success (test)
	//return index of the song when added
	//return -1 if field is empty
	/**
	 * add a user to the list of all users
	 * @param name The name of the new user
	 * @param b True if user  want to add the new user to the list. False if user just want to test if the new user is valid
	 * @return Return the index of the new added user on success. Return 0 if the user already exist. Return 1 if the user is valid to add. Return -1 if the name field is empty
	 */
	public int add(String name, boolean b){
		if(name.equals("")){
			return -1;
		}
		user u = new user(name);
		if(ul.indexOf(u)!=-1){
			return 0;
		}
		if(b==true){
			ul.add(u);
			Collections.sort(ul);
			return ul.indexOf(u);
		}
		return 1;
	}
	//check if the album is in the system
	//index of the album if yes
	//-1 if no
	/**
	 * check if the user is in the list
	 * @param name name of the user that needed to be checked
	 * @return Return -1 if teh user is not in the list. Return the index of user if it is in the list
	 */
	public int check(String name){ 
		int size = ul.size();
		for(int i = 0; i<size;i++){
			if(name.equals(ul.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
	/**
	 * remove a user from the list
	 * @param i the index of the user that need to be deleted
	 */
	public void remove(int i){
		ul.remove(i);
	}
	/**
	 * Get a user from a ceertain index
	 * @param index The index of the user you want to get 
	 * @return The user on that index
	 */
	public user getUser(int index){
		return ul.get(index);
	}
	/**
	 * Serialize and write the userlist to the disk
	 * @param l The userlist need to be write to the disk
	 * @throws IOException
	 */
	public static void writeData(userlist l)throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(
		new FileOutputStream("./src/model/userlist.data"));
		oos.writeObject(l);
		oos.close();
	}
	/**
	 * Load the serialized file from the disk
	 * @return The list of user that stored in the dat file 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static userlist loadData()throws IOException, ClassNotFoundException {
		File f = new File("./src/model/userlist.data");
		if(f.length()<=0){
			return null;
		}
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/model/userlist.data"));
		userlist l = (userlist)ois.readObject();
		ois.close();
		return l;
	}
}
