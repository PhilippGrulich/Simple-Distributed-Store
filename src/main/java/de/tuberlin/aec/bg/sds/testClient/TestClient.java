package de.tuberlin.aec.bg.sds.testClient;

import java.io.IOException;

public class TestClient {

	public static void main(String[] args) throws IOException {
		
		DatabaseClient db = new DatabaseClient("localhost", 9002);
		System.out.println(db.put("TestKey", "TestValue"));
		System.out.println(db.get("TestKey"));
		System.out.println(db.delete("TestKey"));
		System.out.println(db.get("TestKey"));
	}
	
	
}
