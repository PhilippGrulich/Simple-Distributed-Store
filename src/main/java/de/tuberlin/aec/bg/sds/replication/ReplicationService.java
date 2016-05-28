package de.tuberlin.aec.bg.sds.replication;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import de.tub.ise.hermes.IRequestHandler;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.RequestHandlerRegistry;
import de.tub.ise.hermes.Response;
import de.tuberlin.aec.bg.sds.Log;
import de.tuberlin.aec.bg.sds.Operation;
import de.tuberlin.aec.bg.sds.replication.types.ReplicationLink;

/**
 * 
 * This Service provides all functions which are necessary for the Replication.
 * It also handles the differences between the single replication protocols.
 *
 */
public class ReplicationService {

	private Function<ReplicationMessage, Boolean> callback;
	String nodeName;

	public ReplicationService(String nodeName) throws IOException {
		this.nodeName = nodeName;
		Log.writeToFile("Create Replication Service", nodeName);
		RequestHandlerRegistry.getInstance().registerHandler("replication", new ReplicationRequestHandler());
	}

	public boolean sendReplicates(String startNode, List<ReplicationLink> replicationLinks, Operation operation)
			throws Exception {

		boolean returnValue = true;
		for (ReplicationLink replicationLink : replicationLinks) {
			returnValue = returnValue && true == sendReplicate(startNode, replicationLink, operation);
		};
		return returnValue;

	}
	
	private boolean sendReplicate(String startNode, ReplicationLink replicationLink, Operation operation) throws Exception {
		ReplicationMessage replicationMessage = new ReplicationMessage(operation, startNode);
		Request req = new Request(replicationMessage, "replication", "replicationResult");		
		return replicationLink.startReplication(req, startNode);
	}


	public void registerReplicaCallback(Function<ReplicationMessage, Boolean> callback) {
		this.callback = callback;
	}



	// This handler handles the ReplicationRequest and transfers them to the
	// Node
	private class ReplicationRequestHandler implements IRequestHandler {

		public Response handleRequest(Request req) {
			
			Log.writeToFile("Got a replication message", nodeName);

			if (req.getItems().get(0) instanceof ReplicationMessage) {
				ReplicationMessage replicationMessage = (ReplicationMessage) req.getItems().get(0);
				Boolean replicaResult = callback.apply(replicationMessage);
				// we have to response with the result of the replication
				return new Response("ok", replicaResult, req, replicaResult);
			} else
				return new Response("fail", false, req, false);

		}

		public boolean requiresResponse() {
			return true;
		}

	}
}
