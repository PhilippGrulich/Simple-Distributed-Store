package de.tuberlin.aec.bg.sds.testClient;

import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.Sender;
import de.tuberlin.aec.bg.sds.Operation;

public class TestClient {
	public static void main(String[] args) {
        Request req = new Request(new Operation("Hallo", "Hallo", Operation.Type.SET),"api");
		Sender s = new Sender("localhost", 10);
	}
}
