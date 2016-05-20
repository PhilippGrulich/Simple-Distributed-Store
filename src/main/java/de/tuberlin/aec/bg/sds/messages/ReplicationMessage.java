package de.tuberlin.aec.bg.sds.messages;

import java.io.Serializable;

public class ReplicationMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String key;
	private String value;	
	private String restReplicaRoute;

	public ReplicationMessage(String key, String value, String restReplicaRoute) {
		super();
		this.key = key;
		this.value = value;
		this.restReplicaRoute = restReplicaRoute;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public String getRestReplicaRoute() {
		return restReplicaRoute;
	}

	
	
	
	
	
}
