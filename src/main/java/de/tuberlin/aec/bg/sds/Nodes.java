package de.tuberlin.aec.bg.sds;

import java.io.IOException;
import java.util.HashMap;

import de.tub.ise.hermes.AsyncCallbackRecipient;
import de.tub.ise.hermes.Receiver;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.RequestHandlerRegistry;
import de.tub.ise.hermes.Response;
import de.tub.ise.hermes.Sender;
import de.tuberlin.aec.*;
import de.tub.ise.hermes.handlers.EchoRequestHandler;

public class Nodes {
	
	/** Properties **/
	private char id;			//ID of the node
	
	private int[] port = { 9001, 9002, 9003 };
	
	private DataStore dataStore;
	/** Properties **/
	
	
	/**
	 * Constructor.
	 *
	 * @param id of the node
	 */
	public Nodes(char id){
		this.id = id;
		dataStore = new DataStore();
	}
	


	/**
	 * Get Id function
	 * 
	 * @return id name (char)
	 */
	public char getId() {
		return id;
	}
		
	
	/**
	 * Start the listener
	 * 
	 */
	public void doStartNode() throws IOException{
		RequestHandlerRegistry.getInstance().registerHandler(""+getId(),
                new EchoRequestHandler());
        Receiver r = new Receiver(9002);
        r.start();
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
}


