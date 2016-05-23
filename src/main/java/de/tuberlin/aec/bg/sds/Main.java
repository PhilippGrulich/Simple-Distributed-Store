package de.tuberlin.aec.bg.sds;

import java.io.IOException;
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
		ReplicationService replicationService = new ReplicationService();
		
		Node node = new Node("NodeID",c,replicationService,hermesAPIService);
		
		// Start Hermes receiver
		System.out.println("Start Hermes Port:" +hermesPort);
		Receiver r = new Receiver(hermesPort);
		r.start();

		/*
		 * if(path.length != 0){ for(String x : path){
		 * 
		 * String[] conf = x.split("\\|");
		 * 
		 * String src = conf[0]; String type = conf[1]; String target = conf[2];
		 * 
		 * 
		 * try { if(type == "sync"){ a.doStartNode(target, "Tes|3",0); //Start
		 * syncronous } else if (type == "async"){ a.doStartNode(target,
		 * "Tes|3",1); //Start asyncronous } } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } } }
		 */

		/*
		*/
	}

}
