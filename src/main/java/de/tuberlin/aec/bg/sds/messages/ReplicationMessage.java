package de.tuberlin.aec.bg.sds.messages;

import java.io.Serializable;

public class ReplicationMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Operation operation;
	private String restReplicaRoute;

	public ReplicationMessage(Operation operation, String restReplicaRoute) {
		super();
		this.operation = operation;
		this.restReplicaRoute = restReplicaRoute;
	}


	public String getRestReplicaRoute() {
		return restReplicaRoute;
	}


	public Operation getOperation() {
		return operation;
	}
	
	
	
}
