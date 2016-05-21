package de.tuberlin.aec.bg.sds;

import java.io.IOException;

import de.tub.ise.hermes.Receiver;

public class Main {

	public static void main(String[] args) throws IOException{
		//Nodes a = new Nodes('A');
		Config c = new Config();
		System.out.println(c.configValue("A"));
		
		//Start Hermes receiver 
		 Receiver r = new Receiver(9002);
	     r.start();
	     
		
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
	
	
}
