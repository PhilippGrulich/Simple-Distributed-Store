package de.tuberlin.aec.bg.sds;

public class Operation {
	
	public enum Type{
		GET,
		DELETE,
		SET
	}
	
	public final String key, value;
	public final Type operationType;
	
	public Operation(String key, String value, Type operationType) {
		super();
		this.key = key;
		this.value = value;
		this.operationType = operationType;
	}

}
