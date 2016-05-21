package de.tuberlin.aec.bg.sds.testClient;

import java.util.ArrayList;
import java.util.List;

import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.Response;
import de.tub.ise.hermes.Sender;
import de.tuberlin.aec.bg.sds.Operation;

public class TestClient {
	public static void main(String[] args) {
		Operation operation = new Operation("Hallo", "Hallo", Operation.Type.SET);
        Request req = new Request(operation,"api","api");
		Sender s = new Sender("localhost", 10);
		Response res = s.sendMessage(req, 3000);
		System.out.println(res);
	}
}
