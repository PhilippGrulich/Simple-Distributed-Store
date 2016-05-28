package de.tuberlin.aec.bg.sds;

import java.util.Arrays;

import de.tub.ise.hermes.Receiver;
import de.tuberlin.aec.bg.sds.api.APIService;
import de.tuberlin.aec.bg.sds.api.HermesApiService;
import de.tuberlin.aec.bg.sds.replication.ReplicationService;

public class Main {

	public static void main(String[] args) throws Exception {
		// Nodes a = new Nodes('A');
		if(args.length!=2)
			throw new Exception("Must have two args. \n 1. host 2. port");
		System.out.println(Arrays.toString(args));
		String ip = args[0];
		int hermesPort = Integer.parseInt(args[1]);
		
		String nodeName = ip+":"+ hermesPort;
		
		Config c = new Config();
		System.out.println(c.configValue(nodeName));

		APIService hermesAPIService = new HermesApiService();
		
		ReplicationService replicationService = new ReplicationService(nodeName);
		
		new Node(nodeName,c,replicationService,hermesAPIService);
		
		// Start Hermes receiver
		Log.writeToFile("Start Hermes Port:" +hermesPort,nodeName);
		Receiver r = new Receiver(hermesPort);
		r.start();
		
		
		System.out.println(c.configValue(nodeName));

		
	}

}
