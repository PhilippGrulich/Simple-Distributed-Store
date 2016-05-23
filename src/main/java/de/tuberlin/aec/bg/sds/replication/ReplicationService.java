package de.tuberlin.aec.bg.sds.replication;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

import javax.security.auth.callback.Callback;

import de.tub.ise.hermes.AsyncCallbackRecipient;
import de.tub.ise.hermes.IRequestHandler;
import de.tub.ise.hermes.Receiver;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.RequestHandlerRegistry;
import de.tub.ise.hermes.Response;
import de.tub.ise.hermes.Sender;
import de.tub.ise.hermes.handlers.EchoRequestHandler;
import de.tuberlin.aec.bg.sds.Operation;
import de.tuberlin.aec.bg.sds.replication.types.ASyncReplicationLink;
import de.tuberlin.aec.bg.sds.replication.types.ReplicationLink;
import de.tuberlin.aec.bg.sds.replication.types.SyncReplicationLink;

/**
 * 
 * This Service provides all functions which are necessary for the Replication.
 * It also handles the differences between the single replication protocols. 
 *
 */
public class ReplicationService {
	
	private Function<ReplicationMessage,Boolean> callback;

	public ReplicationService() throws IOException {
		System.out.println("Create Replication Service");
		RequestHandlerRegistry.getInstance().registerHandler("replication",new ReplicationRequestHandler()   );
       
	}
	
	public boolean sendReplicates(String startNode, List<ReplicationLink> replicationLinks, Operation operation ) throws Exception{
		boolean returnValue = true;
		for (ReplicationLink replicationLink: replicationLinks){
			returnValue = returnValue && true == sendData(startNode,replicationLink,operation);	
		};		
		return returnValue;
		
	}
	
	public void registerReplicaCallback(Function<ReplicationMessage,Boolean> callback){
		this.callback = callback;
	}
	
	/**
	 * Sending the data
	 * 
	 * @param type : 0 is synchronous, 1 is asynchronous, 2 is quorum
	 * @param Nodename : target node
	 * @param keyValue : key:value
	 * @param port : target port
	 * @return 
	 * @throws Exception 
	 * 
	 */
	private boolean sendData(String startNode, ReplicationLink replicationLink, Operation operation) throws Exception{
		ReplicationMessage replicationMessage = new ReplicationMessage(operation, startNode);
        Request req = new Request(replicationMessage,"replica","replicaResult");
        // Get node host and port by name
        String[] splited = replicationLink.src.split(":");
        if(splited.length!=2)
        	throw new Exception("Node Name is not right formatted :"+ replicationLink);
        String nodeHost = splited[0];
        String nodePort = splited[1];
        
		Sender s = new Sender(nodeHost, Integer.parseInt(nodePort));
		
		if(replicationLink instanceof ASyncReplicationLink){
			AsyncCallback echoAsyncCallback = new AsyncCallback();
            return s.sendMessageAsync(req, echoAsyncCallback);
		}else if(replicationLink instanceof SyncReplicationLink){
			Response received = s.sendMessage(req, 3000);
            return received.responseCode();
		}
		return false;
	}
	
	/**
	 * 
	 * Class for handling callback
	 *
	 */
	private class AsyncCallback implements AsyncCallbackRecipient {

    	public boolean isEchoSuccessful() {
            return echoSuccessful;
        }

        public void setEchoSuccessful(boolean echoSuccessful) {
            this.echoSuccessful = echoSuccessful;
        }

        private boolean echoSuccessful;

        public Response getResponse() {
        	
            return response;
        }

        public void setResponse(Response response) {
            this.response = response;
        }

        private Response response;

     
        public void callback(Response resp) {
            setResponse(resp);
            
            setEchoSuccessful(resp.responseCode());
        }
    }
	
	// This handler handles the ReplicationRequest and transfers them to the Node
	private class ReplicationRequestHandler implements IRequestHandler {

		public Response handleRequest(Request req) {
			Response r;
			if (req.getItems().get(0) instanceof ReplicationMessage) {
				ReplicationMessage replicationMessage = (ReplicationMessage) req.getItems().get(0);
				Boolean replicaResult = callback.apply(replicationMessage);
				// we have to response with the result of the replication
				r = new Response("ok", true, req, replicaResult);
			}
			else
				r = new Response("fail",false,req,false);			
			
			return r;
		}

		public boolean requiresResponse() {
			return true;
		}

	}
}
