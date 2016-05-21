package de.tuberlin.aec.bg.sds;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import de.tuberlin.aec.bg.sds.replication.ReplicationMessage;
import de.tuberlin.aec.bg.sds.replication.ReplicationService;
import de.tuberlin.aec.bg.sds.replication.types.ReplicationLink;

public class Nodes {

	/** Properties **/
	private String nodeName; // ID of the node

	private int[] port = { 9001, 9002, 9003 };

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
	public Nodes(String nodeName, Config config) {
		this.nodeName = nodeName;
		this.config = config;
		dataStore = new DataStore();
		try {
			replicationService = new ReplicationService();
			replicationService.registerReplicaCallback(this::replicationHandler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// we have to create an public api for the client. So he can start an
		// update.
		// maybe as a http rest api? so it would be cool for demonstration ?

	}

	/**
	 * Do some forward replication to other nodes
	 * 
	 * @param replicationMessage
	 * @return
	 */
	private Boolean replicationHandler(ReplicationMessage replicationMessage) {
		// we have to save the new key value pair
		dataStore.executeOperation(replicationMessage.getOperation());

		// we have to replicate it to the other nodes
		String startNodeName = replicationMessage.getStartNode();
		List<ReplicationLink> replicationLinks = config
				.getReplicationLinks(startNodeName);
		// We Select all replication Links where this node is the src
		replicationLinks = replicationLinks.stream()
				.filter(link -> link.getSrc().equals(this.nodeName))
				.collect(Collectors.toList());
		boolean result = replicationService.sendReplicates(startNodeName,
				replicationLinks, replicationMessage.getOperation());
		return result;
	}

}
