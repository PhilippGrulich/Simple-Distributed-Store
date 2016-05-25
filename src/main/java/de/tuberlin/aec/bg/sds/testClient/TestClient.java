package de.tuberlin.aec.bg.sds.testClient;

import java.io.IOException;

import de.tub.ise.hermes.Receiver;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.Response;
import de.tub.ise.hermes.Sender;
import de.tuberlin.aec.bg.sds.Operation;

public class TestClient {
	private static Sender s;

	public static void main(String[] args) throws IOException {
		 s = new Sender("localhost", 9002);
		Receiver r = new Receiver(7777);
		r.start();
		
		Operation operation = new Operation("TestKey", "TestValue", Operation.Type.PUT);
        sendOperation(operation);
        
        operation = new Operation("TestKey", null, Operation.Type.GET);
        sendOperation(operation);
        
        operation = new Operation("TestKey", null, Operation.Type.DELETE);
        sendOperation(operation);
        
        operation = new Operation("TestKey", null, Operation.Type.GET);
        sendOperation(operation);
	}
	
	private static void sendOperation(Operation operation){		
        Request req = new Request(operation,"api","api");		
		Response res = s.sendMessage(req, 3000);
		System.out.println(operation);
		System.out.println(res);
	}
}
