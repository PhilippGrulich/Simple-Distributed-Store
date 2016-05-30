package de.tuberlin.aec.bg.sds;

import java.util.List;
import java.util.stream.Collectors;

import de.tuberlin.aec.bg.sds.Operation.Type;
import de.tuberlin.aec.bg.sds.api.APIResultMessage;
import de.tuberlin.aec.bg.sds.api.APIService;
import de.tuberlin.aec.bg.sds.replication.ReplicationService;
import de.tuberlin.aec.bg.sds.replication.types.ReplicationLink;

public class Node {

	/** Properties **/
	private String nodeName; // ID of the node

	private DataStore dataStore;
	/** Properties **/

	private ReplicationService replicationService;

	private Config config;

	/**
	 * Constructor.
	 *
	 * @param id
	 *            of the node
	 */
	public Node(String nodeName, Config config, ReplicationService replicationService, APIService... apiservices) {
		this.nodeName = nodeName;
		this.config = config;
		dataStore = new DataStore();
		this.replicationService = replicationService;
		replicationService.registerReplicaCallback(replicationMessage -> {
			// we have to save the new key value pair
			
			Boolean replicationResult = this.sendReplication(replicationMessage.getOperation(), replicationMessage.getStartNode());
			if(replicationResult)
				this.dataStore.executeOperation(replicationMessage.getOperation());
			return replicationResult;
		});

		// we have to create an public api for the client. So he can start an
		// update.
		// maybe as a http rest api? so it would be cool for demonstration ?
		for (APIService apiservice : apiservices) {
			apiservice.registerOperationCallback(operation -> {				
				if (operation.operationType != Type.GET) {
					Boolean resultCode = this.sendReplication(operation, this.nodeName);
					if(resultCode){
						this.dataStore.executeOperation(operation);
					}
					return new APIResultMessage(null, resultCode);
				} else {
					String returnValue = this.dataStore.executeOperation(operation);
					return new APIResultMessage(returnValue, true);
				}
			});
		}
	}

	/**
	 * Do some forward replication to other nodes
	 * 
	 * @param replicationMessage
	 * @return
	 */
	private Boolean sendReplication(Operation operation, String startNode) {

		List<ReplicationLink> replicationLinks = config.getReplicationLinks(startNode);
		// We Select all replication Links where this node is the src
		replicationLinks = replicationLinks.stream().filter(link -> link.getSrc().equals(this.nodeName))
				.collect(Collectors.toList());
		try {
			return replicationService.sendReplicates(startNode, replicationLinks, operation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
