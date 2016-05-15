package de.tuberlin.aec.bg.sds;

import java.io.BufferedReader;
import java.io.FileReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Config {
	
	/**
	 * Get Configuration value
	 * 
	 * @return array configuration (String)
	 */
	public String[] configValue(String Nodesid){
		
		String x = readConfig();
		x = getElement(x, "path", "start", Nodesid);
		String[] arr = convertValue(x, "link");
		
		return arr;
	}
	
	/**
	 * Reading the configuration file
	 * 
	 * @return html (String)
	 */
	private String readConfig(){
		String line;			//String for reading config.txt
		String html = "";
		
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("config.txt"));
			while((line = in.readLine()) != null)
			{
				html += "\n" + line;
			}
			in.close();
		}
		catch (Exception ex)
		{
			System.out.println("Error : " + ex.getMessage());
		}
		
		return html;
	}
	
	/**
	 * Get element of html string
	 * 
	 * @param html : HTML String
	 * @param element : Element that we need to get
	 * @param attribute : Attribute that we choose for comparation
	 * @param node : node id that we need to compare with attribute value
	 * 
	 * @return html (String)
	 */
	private String getElement(String html, String element, String attribute , String node){
		Document doc = Jsoup.parse(html);		//Parse string using jsoup api
		Elements link = doc.select(element);		//Read each element
		
		for(int i = 0; i < link.size(); i++){
			if(link.get(i).attr(attribute).equals(node)){
				html = link.get(i).toString();
			}
		}
		
		return html;
	}
	
	/**
	 * Get value of each element
	 * 
	 * @param html : HTML String
	 * @param element : element that we choose
	 * 
	 * @return value of element (Array of String)
	 */
	private String[] convertValue(String html, String element){
		Document doc = Jsoup.parse(html);			//Parse string using jsoup api
		Elements link = doc.select(element);		//Read each element
		
		String[] arr = new String[link.size()];
		
		//Extract child
		for(int i = 0; i < link.size(); i++){
			if(link.get(i).attr("type").equals("quorum")){
				arr[i] = link.get(i).attr("src") + "|" + link.get(i).attr("type") + "|" + link.get(i).attr("qsize") + "-";
				
				//If it is quorum then get the participants
				Elements quorum = doc.select("qparticipant");		//Read each element
				for(int j = 0; j < quorum.size(); j++){
					arr[i] += quorum.get(j).attr("name") + (j == (quorum.size() - 1)?"":"|");
				}
				//If it is quorum then get the participants
			} else {
				arr[i] = link.get(i).attr("src") + "|" + link.get(i).attr("type") + "|" + link.get(i).attr("target");
			}
		}
		
		return arr;
	}
}
