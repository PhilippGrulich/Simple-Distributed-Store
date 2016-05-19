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
	private HashMap<Integer, String> hash;	//HashMap key : integer, value : String
	private int[] port = { 9001, 9002, 9003 };
	/** Properties **/
	
	public static void main(String[] args){
		
		Nodes a = new Nodes('A');
		Config c = new Config();
		String[] path = c.configValue(""+a.getId());
		
		try {
			a.doStartNode();
			//a.sendData("B", "Tes|3",1, port[2]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		if(path.length != 0){
			for(String x : path){
				
				String[] conf = x.split("\\|");
				
				String src = conf[0];
				String type = conf[1];
				String target = conf[2];
				

				try {
					if(type == "sync"){
						a.doStartNode(target, "Tes|3",0);	//Start syncronous
					} else if (type == "async"){
						a.doStartNode(target, "Tes|3",1);	//Start asyncronous
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}*/
		
		/*
		*/
	}
	
	/**
	 * Constructor.
	 *
	 * @param id of the node
	 */
	public Nodes(char id){
		this.id = id;
		hash = new HashMap<Integer, String>();
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
	 * Store value
	 * 
	 * @param id (integer)
	 * @param value (String)
	 */
	public void setValue(int id, String value){
		hash.put(id, value);
	}
	
	/**
	 * Get value
	 * 
	 * @param id (integer)
	 * @return value (String) 
	 */
	public String getValue(int id){
		return hash.get(id);
	}
	
	/**
	 * Delete value
	 * 
	 * @param id (integer) 
	 */
	public void delValue(int id){
		hash.remove(id);
	}
	
	/**
	 * Get Hash Size
	 * 
	 * @return hash size (integer)
	 */
	public int getHashSize(){
		return hash.size();
	}
	
	
	/**
	 * Start the listener
	 * 
	 */
	private void doStartNode() throws IOException{
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

        @Override
        public void callback(Response resp) {
            setResponse(resp);
            setEchoSuccessful(resp.responseCode());
        }
    }
}


