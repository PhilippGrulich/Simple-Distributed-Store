package de.tuberlin.aec.bg.sds.replication.types;

import de.tub.ise.hermes.AsyncCallbackRecipient;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.Response;
import de.tub.ise.hermes.Sender;
import de.tuberlin.aec.bg.sds.Log;

public class ASyncReplicationLink extends ReplicationLink {
	@Override
	public String toString() {
		return "ASyncReplicationLink [src=" + src + " target=" + target + "]";
	}

	public final String target;

	public ASyncReplicationLink(String src, String target) {
		super(src);
		this.target = target;
	}

	@Override
	public Boolean startReplication(Request req, String startNode) throws Exception {
		// Get node host and port by name

		String[] splited = this.target.split(":");

		if (splited.length != 2)
			throw new Exception("Node Name is not right formatted :" + this);

		String nodeHost = splited[0];
		String nodePort = splited[1];

		System.out.println("Replicate ASync to " + this.target);
		Log.writeToFile("Replicate ASync to " + this.target, startNode);

		Sender s = new Sender(nodeHost, Integer.parseInt(nodePort));
		return s.sendMessageAsync(req, new AsyncCallbackRecipient() {

			@Override
			public void callback(Response resp) {

			}
		});
	}

}
