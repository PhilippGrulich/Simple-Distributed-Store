package de.tuberlin.aec.bg.sds.replication.types;

import de.tub.ise.hermes.Request;

public abstract class ReplicationLink {
	public String src;

	public ReplicationLink(String src) {
		this.src = src;
	}

	public String getSrc() {
		return src;
	}
	
	public abstract Boolean startReplication(Request req, String startNode) throws Exception;

}
