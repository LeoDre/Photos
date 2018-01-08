package model;

import java.io.Serializable;
/**
 * tag class is a attribute that belongs to the image(imgR)
 * @author Ran Sa, Jami Nicastro
 *
 */
public class tag implements Comparable<tag>,Serializable{
	/**
	 * Field:
	 * 		serialVersionUID: serial ID used for serialization 
	 * 		cat: the category of the tag
	 * 		value: the value of the tag
	 */
	private static final long serialVersionUID = 1121641328995673680L;
	//category of the tag
	private String cat;
	//value of the tag
	private String value;
	/**
	 * create and initialize a tag object with 2 strings
	 * @param cat The category of the tag in a string
	 * @param value The value of the tag in a string
	 */
	public tag(String cat,String value){
		this.cat = cat;
		this.value = value;
	}
	/**
	 * Get the category of the tag
	 * @return The category of the tag in a string
	 */
	public String getCat(){
		return cat;
	}
	/**
	 * Get the value of the tag
	 * @return  The value of the tag in a string
	 */
	public String getValue(){
		return value;
	}
	/**
	 * Check the equality of two tags, if the category field and the value field are both equal, then they are equal
	 */
	@Override
	public boolean equals(Object o){
		if(o==null||(!(o instanceof tag))){
			return false;
		}
		tag tmp = (tag)o;
		return (cat.equals(tmp.getCat())&&value.equals(tmp.getValue()));
	}
	/**
	 * compare two tag. category is the primary factor and value is secondary(for sorting only)
	 */
	@Override
	public int compareTo(tag t) {
		if(cat.equals(t.getCat())){
			return value.compareTo(t.getValue());
		}else{
			return cat.compareTo(t.getCat());
		}
	}
	/**
	 * Convert the tag to a string in the following format "category --- value"
	 */
	public String toString(){
		return cat+" --- "+value;
	}
}