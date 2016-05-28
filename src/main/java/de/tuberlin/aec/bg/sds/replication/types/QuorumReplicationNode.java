package de.tuberlin.aec.bg.sds.replication.types;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javax.security.auth.callback.Callback;

import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.Response;
import de.tub.ise.hermes.Sender;
import de.tuberlin.aec.bg.sds.Log;

public class QuorumReplicationNode extends ReplicationLink {
	public List<String> qpartiqparticipants = new ArrayList<String>();
	public int qSize;

	public QuorumReplicationNode(String src, int qSize) {
		super(src);
		this.qSize = qSize;
	}

	public void addQPartiqparticipant(String qpartiqparticipant) {
		this.qpartiqparticipants.add(qpartiqparticipant);
	}

	@Override
	public String toString() {
		return "QuorumReplicationNode [src=" + src + " qpartiqparticipant=" + qpartiqparticipants + ", qSize=" + qSize
				+ "]";
	}

	@Override
	public Boolean startReplication(Request req, String startNode) throws Exception {

		CountDownLatch countDown = new CountDownLatch(qSize);
		ExecutorService executer = Executors.newFixedThreadPool(qpartiqparticipants.size());
		
		qpartiqparticipants.forEach(qpartiqparticipant->{		
			Callable<Boolean> callable = sendReplicaRequest(countDown,qpartiqparticipant,startNode,req);
			executer.submit(callable);
		});		
		
		return countDown.await(3, TimeUnit.SECONDS);
	}

	private Callable<Boolean> sendReplicaRequest(CountDownLatch counter, String qpartiqparticipant, String startNode,
			Request req) {
		return () -> {
			String[] splited = qpartiqparticipant.split(":");
			String nodeHost = splited[0];
			String nodePort = splited[1];

			Log.writeToFile("Replicate starts Quorum to " + qpartiqparticipant, startNode);
			Sender s = new Sender(nodeHost, Integer.parseInt(nodePort));

			Log.writeToFile("Replicate finished Quorum to " + qpartiqparticipant, startNode);
			Response received = s.sendMessage(req, 3000);
			if (received.responseCode() == true) {
				counter.countDown();
			}
			return received.responseCode();			
		};

	}
}
