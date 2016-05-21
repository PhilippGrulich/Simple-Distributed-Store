package de.tuberlin.aec.bg.sds;

import java.beans.XMLDecoder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import de.tuberlin.aec.bg.sds.replication.types.ASyncReplicationLink;
import de.tuberlin.aec.bg.sds.replication.types.ReplicationLink;
import de.tuberlin.aec.bg.sds.replication.types.QuorumReplicationNode;
import de.tuberlin.aec.bg.sds.replication.types.SyncReplicationLink;

public class Config {

	private Document doc;

	public Config() throws IOException {
		File f = new File("config.txt");
		doc = Jsoup.parse(f, "UTF-8");
	}

	/**
	 * Get Configuration value
	 * 
	 * @return array configuration (String)
	 */
	public List<ReplicationLink> configValue(String Nodesid) {

		String x = readConfig();
		x = getElement(x, "path", "start", Nodesid);
		return convertValue(x, "link");
	}

	/**
	 * Reading the configuration file
	 * 
	 * @return html (String)
	 */
	private String readConfig() {
		String line; // String for reading config.txt
		String html = "";

		try {
			BufferedReader in = new BufferedReader(new FileReader("config.txt"));
			while ((line = in.readLine()) != null) {
				html += "\n" + line;
			}
			in.close();
		} catch (Exception ex) {
			System.out.println("Error : " + ex.getMessage());
		}

		return html;
	}

	public List<ReplicationLink> getReplicationLinks(String startNode) {

		List<Node> nodes = doc.childNodes();
		String x = readConfig();
		x = getElement(x, "path", "start", startNode);
		return convertValue(x, "link");
	}

	/**
	 * Get element of html string
	 * 
	 * @param html
	 *            : HTML String
	 * @param element
	 *            : Element that we need to get
	 * @param attribute
	 *            : Attribute that we choose for comparation
	 * @param node
	 *            : node id that we need to compare with attribute value
	 * 
	 * @return html (String)
	 */
	private String getElement(String html, String element, String attribute,
			String node) {
		Document doc = Jsoup.parse(html); // Parse string using jsoup api
		Elements link = doc.select(element); // Read each element

		for (int i = 0; i < link.size(); i++) {
			if (link.get(i).attr(attribute).equals(node)) {
				html = link.get(i).toString();
			}
		}

		return html;
	}

	/**
	 * Get value of each element
	 * 
	 * @param html
	 *            : HTML String
	 * @param element
	 *            : element that we choose
	 * 
	 * @return value of element (Array of String)
	 */
	private List<ReplicationLink> convertValue(String html, String element) {
		Document doc = Jsoup.parse(html); // Parse string using jsoup api
		Elements link = doc.select(element); // Read each element

		List<ReplicationLink> arr = new ArrayList<ReplicationLink>();

		// Extract child
		for (int i = 0; i < link.size(); i++) {
			String type = link.get(i).attr("type");
			String src = link.get(i).attr("src");

			if (type.equals("quorum")) {
				QuorumReplicationNode qrn = new QuorumReplicationNode(src,
						Integer.parseInt(link.get(i).attr("qsize")));

				// If it is quorum then get the participants
				Elements quorum = doc.select("qparticipant"); // Read each
																// element
				for (int j = 0; j < quorum.size(); j++) {
					qrn.addQPartiqparticipant(quorum.get(j).attr("name"));
				}

				arr.add(qrn);
				// If it is quorum then get the participants
			} else if (type.equals("sync")) {
				arr.add(new SyncReplicationLink(src, link.get(i).attr("target")));
			} else {
				arr.add(new ASyncReplicationLink(src, link.get(i)
						.attr("target")));
			}
		}
		return arr;
	}
}
