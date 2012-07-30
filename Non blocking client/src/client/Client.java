package client;

import static network.NetworkProtocol.log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import network.Communicable;
import network.NetworkEvent;
import network.NetworkProtocol;

public class Client implements Runnable, Communicable {
	private static final InetSocketAddress DEFAULT_ADDRESS = new InetSocketAddress(
			"localhost", 55545);
	private static final String DEFAULT_NAME = "Client";

	public static void main(String[] args) {
		try {
			new Client();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected ClientState clientState;
	private boolean connectionTerminated = false;
	private int count = 0;
	private ScheduledExecutorService executor = Executors
			.newScheduledThreadPool(1);
	protected int id;
	protected final String name;

	private ByteBuffer readData = ByteBuffer
			.allocate(NetworkProtocol.DEFAULT_BUFFER_ALLOCATION_SIZE);

	private SocketChannel socketChannel;

	public Client() throws IOException {
		this(DEFAULT_NAME, DEFAULT_ADDRESS);
	}

	public Client(String name, InetSocketAddress addr) throws IOException {
		this.name = name;
		clientState = new ClientHandshakeState(this);
		socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		log("Channel open.");

		initiateConnection(addr);
		executor.scheduleAtFixedRate(this, 0, 1, TimeUnit.MILLISECONDS);
	}

	@Override
	public NetworkEvent getEvent(byte[] data) {
		return new NetworkEvent(data);
	}

	private void initiateConnection(InetSocketAddress addr) throws IOException {

		int connectionTime = 0;
		socketChannel.connect(addr);

		while (!socketChannel.isConnected()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (socketChannel.isConnectionPending()) {
				socketChannel.finishConnect();
				break;
			}
			connectionTime++;
			if (connectionTime < 15) {
				log("Connecting...");
			} else {
				log("Connection to sever failed - time out");
				System.exit(0);
			}
		}

		log("Connected");
	}

	@Override
	public void run() {
		if (connectionTerminated) {
			return;
		}

		++count;
		if (count >= 10000) {
			count = 0;
		}

		readData.clear();

		try {
			int bytesRead = socketChannel.read(readData);
			if (bytesRead > 0) {
				clientState.handleData(readData.array());
			} else if (bytesRead == -1) {
				terminateConnection();
				return;
			}

			clientState.performDefaultWrite(count);

		} catch (IOException e) {
			System.err.println(e.getMessage());
			terminateConnection();
			return;
		}
	}

	@Override
	public void send(NetworkEvent e) throws IOException {
		socketChannel.write(ByteBuffer.wrap(e.toByteArray()));
	}

	private void terminateConnection() {
		try {
			socketChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectionTerminated = true;
		log("Connection terminated");
	}
}
