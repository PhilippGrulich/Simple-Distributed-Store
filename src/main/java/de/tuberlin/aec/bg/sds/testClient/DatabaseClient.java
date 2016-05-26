package de.tuberlin.aec.bg.sds.testClient;

import java.io.IOException;

import de.tub.ise.hermes.Receiver;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.Response;
import de.tub.ise.hermes.Sender;
import de.tuberlin.aec.bg.sds.Operation;

public class DatabaseClient {

	private Sender connection;

	public DatabaseClient(String host, int port) throws IOException {
		connection = new Sender(host, port);
		Receiver r = new Receiver(0);
		r.start();
	}

	public String get(String key) {

		Operation operation = new Operation(key, null, Operation.Type.GET);
		Response res = sendOperation(operation);
		return (String)res.getItems().get(0);
	}

	public boolean put(String key, String value) {
		Operation operation = new Operation(key,value,
				Operation.Type.PUT);
		Response res = sendOperation(operation);
		return res.responseCode();
	}

	public boolean delete(String key) {
		Operation operation = new Operation(key, null, Operation.Type.DELETE);
		Response res = sendOperation(operation);
		return res.responseCode();
	}

	private Response sendOperation(Operation operation) {
		Request req = new Request(operation, "api", "apiResponse");
		return connection.sendMessage(req, 3000);
		
	}
}
