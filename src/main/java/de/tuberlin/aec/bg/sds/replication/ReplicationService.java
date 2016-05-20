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
import de.tuberlin.aec.bg.sds.messages.ReplicationMessage;

/**
 * 
 * This Service provides all functions which are necessary for the Replication.
 * It also handles the differences between the single replication protocols. 
 *
 */
public class ReplicationService {
	
	private Receiver r;
	
	private Function<ReplicationMessage,Boolean> callback;

	public ReplicationService() throws IOException {
		RequestHandlerRegistry.getInstance().registerHandler("replication",new ReplicationRequestHandler()   );
        r = new Receiver(9002);
        r.start();
	}
	
	public boolean sendReplicate(String Nodename, String keyValue, int type, int port){
		sendData(Nodename,keyValue,type,port);
		return true;
		
	}
	
	public void recieveReplica(Function<ReplicationMessage,Boolean> callback){
		this.callback = callback;
	}
	
	/**
	 * Sending the data
	 * 
	 * @param type : 0 is synchronous, 1 is asynchronous, 2 is quorum
	 * @param Nodename : target node
	 * @param keyValue : key:value
	 * @param port : target port
	 * 
	 */
	private void sendData(String Nodename, String keyValue, int type, int port){
		Sender s = new Sender("localhost", port);
        Request req = new Request(Nodename, keyValue);
        
        //Type 1 is Asynchronous
        if(type == 1){
        	AsyncCallback echoAsyncCallback = new AsyncCallback();
            boolean received = s.sendMessageAsync(req, echoAsyncCallback);
        //Type 2 is Quorum
        } else if(type == 2){
        	
        }
        //Else is Syncronous*/
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
