package de.tuberlin.aec.bg.sds;

import java.io.Serializable;

public class Operation implements Serializable {
	
	public enum Type{
		GET,
		DELETE,
		PUT
	}
	
	public final String key, value;
	public final Type operationType;
	
	public Operation(String key, String value, Type operationType) {
		super();
		this.key = key;
		this.value = value;
		this.operationType = operationType;
	}

	@Override
	public String toString() {
		return "Operation [key=" + key + ", value=" + value
				+ ", operationType=" + operationType + "]";
	}
	
	

}
