package de.tuberlin.aec.bg.sds.replication;

import java.io.Serializable;

import de.tuberlin.aec.bg.sds.Operation;

public class ReplicationMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Operation operation;
	private String startNode;

	public ReplicationMessage(Operation operation, String startNode) {
		super();
		this.operation = operation;
		this.startNode = startNode;
	}


	public String getStartNode() {
		return startNode;
	}


	public Operation getOperation() {
		return operation;
	}


	@Override
	public String toString() {
		return "ReplicationMessage [operation=" + operation + ", startNode="
				+ startNode + "]";
	}
	
	
	
	
	
}
