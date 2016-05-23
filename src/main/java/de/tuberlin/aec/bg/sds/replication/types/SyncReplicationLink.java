package de.tuberlin.aec.bg.sds.replication.types;


public class SyncReplicationLink extends ReplicationLink {
	public final String target;

	public SyncReplicationLink(String src, String target) {
		super(src);
		this.target = target;
	}

	@Override
	public String toString() {
		return "SyncReplicationLink [src="+src+" target=" + target + "]";
	}
	
	
}
