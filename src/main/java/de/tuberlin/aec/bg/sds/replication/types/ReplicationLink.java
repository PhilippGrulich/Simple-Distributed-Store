package de.tuberlin.aec.bg.sds.replication.types;

public abstract class ReplicationLink {
	public String src;

	public ReplicationLink(String src) {
		this.src = src;
	}

	public String getSrc() {
		return src;
	}

}
