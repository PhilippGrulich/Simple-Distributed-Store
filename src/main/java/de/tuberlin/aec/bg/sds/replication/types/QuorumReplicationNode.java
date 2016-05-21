package de.tuberlin.aec.bg.sds.replication.types;

import java.util.List;

public class QuorumReplicationNode extends ReplicationLink {
	List<String> qpartiqparticipant;
	int qSize;
	public QuorumReplicationNode(String src, int qSize) {
		super(src);
		this.qpartiqparticipant = qpartiqparticipant;
		this.qSize = qSize;
	}
	
	public void addQPartiqparticipant(String qpartiqparticipant){
		this.qpartiqparticipant.add(qpartiqparticipant);		
	}

	@Override
	public String toString() {
		return "QuorumReplicationNode [src="+src+" qpartiqparticipant="
				+ qpartiqparticipant + ", qSize=" + qSize + "]";
	}	
}
