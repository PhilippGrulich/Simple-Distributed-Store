package de.tuberlin.aec.bg.sds;

import java.io.IOException;

import de.tub.ise.hermes.AsyncCallbackRecipient;
import de.tub.ise.hermes.Receiver;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.RequestHandlerRegistry;
import de.tub.ise.hermes.Response;
import de.tub.ise.hermes.Sender;
import de.tub.ise.hermes.handlers.EchoRequestHandler;
import de.tuberlin.aec.bg.sds.replication.ReplicationService;

public class Nodes {
	
	/** Properties **/
	private char id;			//ID of the node
	
	private int[] port = { 9001, 9002, 9003 };
	
	private DataStore dataStore;
	/** Properties **/

	private ReplicationService replicationService;
	
	
	/**
	 * Constructor.
	 *
	 * @param id of the node
	 */
	public Nodes(char id){
		this.id = id;
		dataStore = new DataStore();
		try {
			replicationService = new ReplicationService();
			replicationService.recieveReplica(replicationMessage -> {
				
				// we have to save the new key value pair
				// we have to replicate it to the other nodes
				// we have to check if the replication successes 
				return true;
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// we have to create an public api for the client. So he can start an update. 
		// maybe as a http rest api? so it would be cool for demonstration ? 
		
		
	}
	


	/**
	 * Get Id function
	 * 
	 * @return id name (char)
	 */
	public char getId() {
		return id;
	}
		
	

}


