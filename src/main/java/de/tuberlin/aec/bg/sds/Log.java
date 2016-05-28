package de.tuberlin.aec.bg.sds;

import java.io.BufferedWriter;
import java.io.*;

public class Log {
	
	/**
	 * The function for writing to text file
	 * 
	 * @param text :
	 * 					The text that we want to log
	 */
	public static void writeToFile(String text, String nodeSrc){
		System.out.println("Log: "+nodeSrc+":"+text);
		try(FileWriter fw = new FileWriter("log.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw)){
			    
			out.println(nodeSrc + " : " + text);
			
		} catch (IOException e) {
			System.out.println("Error writing log : " + e.getMessage());
		}
	}
}
