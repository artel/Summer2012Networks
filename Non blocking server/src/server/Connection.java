package server;

import static network.NetworkProtocol.log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import network.Communicable;
import network.NetworkEvent;
import network.NetworkProtocol;

public class Connection implements Communicable {
	private static short instances = 0;
	protected ConnectionState connectionState;
	private int count = 0;
	protected final int ID = ++instances;
	protected String name;
	protected ByteBuffer readData = ByteBuffer.allocate(NetworkProtocol.DEFAULT_BUFFER_ALLOCATION_SIZE);
	protected Server server;
	
	private final SocketChannel socketChannel;
	// TEMP VARS
	protected int x, y, dy, dx;
	
	protected Connection(Server server, SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		this.server = server;
		connectionState = new ConnectionHandshakeState(this);
	}
	
	protected void dropConnection() {
		try {
			socketChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public NetworkEvent getEvent(byte[] data) {
		return new NetworkEvent(data);
	}
	
	protected void performActivity() {
		++count;
		
		if (count >= 10000) {
			count = 0;
		}
		
		//temp processing
		if (count % 1000 == 0) { // every second
			x += dx;
			y += dy;
		}
	}

	protected boolean performReadActivity() throws IOException {
		readData.clear();
		int bytesRead = socketChannel.read(readData);
		if (bytesRead > 0) {
			log("Bytesread " + bytesRead);
			connectionState.handleData(readData.array());
		}
		else if (bytesRead == -1) {
			return false;
		}
		return true;
	}

	protected void performWriteActivity() throws IOException {
		connectionState.performDefaultWrite(count);
	}

	@Override
	public void send(NetworkEvent e) throws IOException {
		socketChannel.write(ByteBuffer.wrap(e.toByteArray()));
	}

}
