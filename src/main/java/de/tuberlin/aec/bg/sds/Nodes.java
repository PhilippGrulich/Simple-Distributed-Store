package de.tuberlin.aec.bg.sds;

import java.util.HashMap;

public class Nodes {
	
	/** Properties **/
	private char id;			//ID of the node
	private HashMap<Integer, String> hash;	//HashMap key : integer, value : String
	/** Properties **/
	
	/**
	 * Constructor.
	 *
	 * @param id of the node
	 */
	public Nodes(char id){
		this.id = id;
		hash = new HashMap<Integer, String>();
	}

	/**
	 * Get Id function
	 * 
	 * @return id name (char)
	 */
	public char getId() {
		return id;
	}
	
	/**
	 * Store value
	 * 
	 * @param id (integer)
	 * @param value (String)
	 */
	public void setValue(int id, String value){
		hash.put(id, value);
	}
	
	/**
	 * Get value
	 * 
	 * @param id (integer)
	 * @return value (String) 
	 */
	public String getValue(int id){
		return hash.get(id);
	}
	
	/**
	 * Delete value
	 * 
	 * @param id (integer) 
	 */
	public void delValue(int id){
		hash.remove(id);
	}
	
	/**
	 * Get Hash Size
	 * 
	 * @return hash size (integer)
	 */
	public int getHashSize(){
		return hash.size();
	}
}


