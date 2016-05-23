package de.tuberlin.aec.bg.sds.replication.types;


public class ASyncReplicationLink extends ReplicationLink {
	@Override
	public String toString() {
		return "ASyncReplicationLink [src="+src+" target=" + target + "]";
	}

	public final String target;

	public ASyncReplicationLink(String src, String target) {
		super(src);
		this.target = target;
	}
	
	
}
