package de.tuberlin.aec.bg.sds.replication.types;

import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.Response;
import de.tub.ise.hermes.Sender;
import de.tuberlin.aec.bg.sds.Log;

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

	@Override
	public Boolean startReplication(Request req, String startNode) throws Exception {

		String[] splited = this.target.split(":");
		if (splited.length != 2)
			throw new Exception("Node Name is not right formatted :" + this);
		String nodeHost = splited[0];
		String nodePort = splited[1];

		System.out.println("Replicate Sync to " + this.target);
		Log.writeToFile("Replicate Sync to " + this.target, startNode);

		Sender s = new Sender(nodeHost, Integer.parseInt(nodePort));
		Response received = s.sendMessage(req, 3000);

		return received.responseCode();
	}
	
	
	
	
}
