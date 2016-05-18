/**
 *
 */
package de.tub.ise.hermes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * receives messages over the network and distributes them to the appropriate
 * handlers
 *
 * @author David Bermbach
 *         <p>
 *         created on: 24.04.2012
 */
public class Receiver extends Thread {

	private static final Logger log = LoggerFactory.getLogger(Receiver.class);

	/* success and error messages */
	private static final String NO_HANDLER_FOUND = "The specified handler is unknown.";
	private static final String SUCCESS = "Auto OK, generated by Receiver.";

	/**
	 * server side socket
	 */
	private ServerSocket serverSocket;

	/** termination flag for the Receiver */
	private boolean running = true;

	/** thread pool */
	private final ExecutorService threadpool = Executors.newCachedThreadPool();

	/**
	 * @param port
	 *            where shall the receiver listen for requests
	 * @throws IOException
	 *             if e.g., the port was already taken (if an I/O error occurs
	 *             when opening the socket)
	 */
	public Receiver(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		log.info("Receiver created at port " + port);
	}

	@Override
	public void run() {
		log.info("Receiver is now running.");
		RequestHandlerRegistry.getInstance().printHandlers();
		try {
			serverSocket.setSoTimeout(50);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		while (!isInterrupted() && running) {
			try {
				Socket s = serverSocket.accept();
				log.debug("new connection created to "
						+ s.getInetAddress().getHostAddress() + " at port "
						+ s.getLocalPort() + ":" + System.currentTimeMillis());
				SocketPackage soc = new SocketPackage();
				soc.socket = s;
				soc.out = new ObjectOutputStream(new BufferedOutputStream(
						s.getOutputStream()));
				// log.debug("got the output stream");
				soc.out.flush();
				// log.debug("output stream flushed");
				soc.in = new ObjectInputStream(new BufferedInputStream(
						s.getInputStream()));
				// log.debug("got the input stream");
				threadpool.submit(new RequestHandlerThread(soc));
				// log.debug("Socket has been enqueued.");
			} catch (Exception e) {
				// log.debug("Some error:", e);
			}
		}
	}

	/**
	 * terminates the receiver as soon as all current requests have been
	 * processed
	 */
	public void terminate() {
		running = false;
		threadpool.shutdown();
	}

	/**
	 * invokes the handlers
	 *
	 * @author David Bermbach
	 *         <p>
	 *         created on: 24.04.2012
	 */
	class RequestHandlerThread implements Runnable {

		private SocketPackage soc;

		/**
		 * @param soc2
		 */
		public RequestHandlerThread(SocketPackage soc2) {
			soc = soc2;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			if (soc == null)
				return;
			if (soc.socket != null) {
				// log.debug("Got a socket with streams, now reading.");
				String connectionAddress = soc.socket.getInetAddress()
						.getHostAddress() + " (conn.id=" + soc.hashCode() + ")";
				log.debug("Start reading from connection " + connectionAddress
						+ " at " + new Date().getTime());
				Request req = readFromSocket();
				if (req == null) {
					closeSession();
					return;
				}
				log.debug("Created request with timestamp "
						+ req.getTimestamp().getTime() + " (req.id="
						+ req.getRequestId() + ", conn.id=" + soc.hashCode()
						+ ")");
				// log.debug("Finished reading request.");
				// check whether response is possible right away
				IRequestHandler handler = RequestHandlerRegistry.getInstance()
						.getHandlerForID(req.getTarget());
				if (handler == null) {
					// target unknown
					respond(new Response(NO_HANDLER_FOUND, false, req));
					log.info("No handler was found for request target "
							+ req.getTarget());
					return;
				}
				if (!handler.requiresResponse()) {
					// respond right away with an o.k.
					respond(new Response(SUCCESS, true, req));
					// log.debug("Sending success right away as no response is required.");
					return;
				}
				// response is not possible right away but we got a handler

				Response resp;
				try {
					resp = handler.handleRequest(req);
					if (resp == null) {
						// handler implementation breaks the contract of
						// only (but always) returning null when
						// requiresResponse() returns false!!!
						// -> should never happen, do nothing but logging
						log.error("Class " + handler.getClass().getSimpleName()
								+ " breaks the contract specified in the"
								+ " interface IRequestHandler");
					}
				} catch (Exception e) {
					e.printStackTrace();
					resp = new Response("An error occurred ("
							+ e.getClass().getSimpleName() + "): "
							+ e.getMessage(), false, req);
				}
				// we have a guaranteed not null response
				respond(resp);

			}
		}

		/**
		 * @return null if an error occurred
		 */
		private Request readFromSocket() {
			try {
				Request req = null;
				int n = (Integer) soc.in.readObject(); // number of items
				// log.debug("Read number of items:" + n);
				String target = (String) soc.in.readObject(); // target handler
				// log.debug("Read target:" + target);
				String messageid = (String) soc.in.readObject(); // message id
				String originator = (String) soc.in.readObject(); // originator
				List<Serializable> res = new ArrayList<Serializable>(n);
				// log.debug("Finished reading header.");
				if (n == 0) {
					req = new Request(res, target, messageid, null);
				} else {
					// read items
					for (int i = 0; i < n; i++) {
						Object o = soc.in.readObject();
						res.add((Serializable) o);
					}
					req = new Request(res, target, messageid, null);
					req.setOriginator(originator);
				}
				// log.debug("Finished reading body, enqueueing socket package.");
				// log.debug("Request was:\n" + req);
				return req;
			} catch (Exception e) {
				e.printStackTrace();
				closeSession(); // drop socket if not able to read request
				return null;
			}
		}

		/**
		 * responds to the client and closes the current session
		 *
		 * @param resp
		 */
		private void respond(Response resp) {
			String connectionAddress = "";
			try {
				connectionAddress = soc.socket.getInetAddress()
						.getHostAddress()
						+ " (req.id="
						+ resp.getRequestId()
						+ ", conn.id=" + soc.hashCode() + ")";
				log.debug("Starting response on connection "
						+ connectionAddress + " at " + new Date().getTime());

				soc.out.writeObject(resp.getItems().size());
				soc.out.flush();
				soc.out.writeObject(resp.responseCode());
				soc.out.flush();
				soc.out.writeObject(resp.getResponseMessage());
				soc.out.flush();
				soc.out.writeObject(resp.getRequestId());
				soc.out.flush();
				for (Serializable s : resp.getItems()) {
					soc.out.writeObject(s);
					soc.out.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeSession();
				log.debug("Completed response on connection "
						+ connectionAddress + " at " + new Date().getTime());
			}
		}

		/**
		 * closes the current session, will never throw an exception
		 */
		private void closeSession() {
			try {
				soc.socket.close();
			} catch (Exception e) {
			}
			soc.socket = null;
			soc.in = null;
			soc.out = null;
			soc = null;
			// log.debug("Session closed.");
		}

	}

}
