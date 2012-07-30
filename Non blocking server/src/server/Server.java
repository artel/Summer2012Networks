package server;

import static network.NetworkProtocol.log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {
	private static final InetSocketAddress DEFAULT_PORT = new InetSocketAddress(55545);
	public static void main(String[] args) {
		try {
			new Server();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private List<Connection> connections = new ArrayList<Connection>(30);
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1); 
	
	private ServerSocketChannel serverSocketChannel;
	
	public Server() throws IOException {
		this(DEFAULT_PORT);
	}

	public Server(InetSocketAddress addr) throws IOException {
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		log("Channel open");
		serverSocketChannel.socket().bind(addr);
		log("Bound to address " + addr);
		executor.scheduleAtFixedRate(this, 0, 1, TimeUnit.MILLISECONDS);
	}
	
	private void dropConnectionWhileIterating(Connection c, Iterator<Connection> connectionIterator, IOException e) {
		System.err.println("Connection with : " + c.ID + " " + c.name + " dropped. " + e.getMessage());
		// drop connection
		c.dropConnection();
		connectionIterator.remove();
	}
	
	private void establishNewConnection() throws IOException {
		SocketChannel sc = serverSocketChannel.accept();
		if (sc != null) {
			sc.configureBlocking(false);
			Connection connection = new Connection(this, sc);
			connections.add(connection);
			log("Connection established!");
		}
	}

	@Override
	public void run() {
		try {
			// accept connections
			establishNewConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Iterator<Connection> connectionIterator = connections.iterator();
		while (connectionIterator.hasNext()) {
			Connection c = connectionIterator.next();
			
			c.performActivity();
			
			try {
				if (!c.performReadActivity()) {
					throw new IOException("Drop this connection");
				}
			} catch (IOException e) {
				dropConnectionWhileIterating(c, connectionIterator, e);
			}
		}
		
		connectionIterator = connections.iterator();
		while (connectionIterator.hasNext()) {
			Connection c = connectionIterator.next();
			try {
				c.performWriteActivity();
			} catch (IOException e) {
				dropConnectionWhileIterating(c, connectionIterator, e);
			}
		}
	}
	
}
