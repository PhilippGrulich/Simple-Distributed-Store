package de.tuberlin.aec.bg.sds;

import java.util.HashMap;

public class DataStore {
	private HashMap<Integer, String> hash = new HashMap<Integer,String>();	//HashMap key : integer, value : String
	
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
