package model;
import java.io.*;
import java.nio.file.*;
/**
 * user is an object represent the user. It have a list of album that stores all its photos
 * @author Ran Sa, Jami Nicastro
 *
 */
public class user implements Serializable, Comparable<user>{
	/**
	 * Field:
	 * 		serialVersionUID: serial id is used for serialization
	 * 		name:name of the user
	 * 		al: a list of album stores all user's photos
	 * 		imagePool: the path of the folder that stores all user's photos
	 */
	private static final long serialVersionUID = 8559011478590691714L;
	private String name;
	private albumList al;
	private String imagePool;
	/**
	 * create and initialize a new user by name
	 * @param name The name of the new user
	 */
	public user(String name){
		this.name = name;
		al = new albumList();
		imagePool = "./imagePool/"+name;
		try{
			Files.createDirectories(Paths.get(imagePool));
		}catch(IOException ioe){
			
		}
	}
	/**
	 * Get the list of album of this user
	 * @return The list of album of this user in albumList type
	 */
	public albumList getAlbumList(){
		return al;
	}
	/**
	 * get the name of the user
	 * @return The name of the user in a string
	 */
	public String getName(){
		return name;
	}
	/**
	 * Get the path of the user's photo folder
	 * @return path of the folder that stores all user's photos (in a string format)
	 */
	public String getPool(){
		return imagePool;
	}
	/**
	 * Check equaily for user by their user name
	 */
	@Override
	public boolean equals(Object o){
		if(o==null||(!(o instanceof user))){
			return false;
		}
		user tmp = (user)o;
		return name.equals(tmp.getName());
	}
	/**
	 * convert the user to a string(name only)
	 */
	@Override
	public String toString(){
		return name;
	}
	/**
	 * compare two user by their name(for sorting only)
	 */
	public int compareTo(user u){
		return name.compareTo(u.getName());
	}
}