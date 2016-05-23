package de.tuberlin.aec.bg.sds;

import java.util.HashMap;

public class DataStore {
	private HashMap<String, String> hash = new HashMap<String, String>(); // HashMap
																			// key
																			// :
																			// integer,
																			// value
																			// :
																			// String

	/**
	 * Store value
	 * 
	 * @param id
	 *            (integer)
	 * @param value
	 *            (String)
	 */
	public void setValue(String id, String value) {
		hash.put(id, value);
	}

	/**
	 * Get value
	 * 
	 * @param id
	 *            (integer)
	 * @return value (String)
	 */
	public String getValue(String id) {
		return hash.get(id);
	}

	/**
	 * Delete value
	 * 
	 * @param id
	 *            (integer)
	 */
	public void delValue(String id) {
		hash.remove(id);
	}

	/**
	 * Get Hash Size
	 * 
	 * @return hash size (integer)
	 */
	public int getHashSize() {
		return hash.size();
	}

	public String executeOperation(Operation operation) {
		System.out.println("Data Store: "+ operation);
		switch (operation.operationType) {
		case GET:
			
			return this.getValue(operation.key);
		case PUT:
			this.setValue(operation.key, operation.value);
			break;
		case DELETE:
			this.delValue(operation.key);
			break;
		}
		return null;
	}
}
