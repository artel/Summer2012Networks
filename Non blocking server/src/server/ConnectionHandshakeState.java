package server;

import static network.NetworkProtocol.log;

import java.io.IOException;

import network.HandshakeInitiateEvent;
import network.HandshakeResponseEvent;

public class ConnectionHandshakeState extends ConnectionState {
	private boolean recievedHandshake = false;
	private boolean sentHandshake = false;
	protected ConnectionHandshakeState(Connection connection) {
		super(connection);
	}
	
	@Override
	public void handleData(byte[] data) throws IOException {
		super.handleData(data);
		
		if (!recievedHandshake) {
			if (recievedEvent.isHandshakeInitiateEvent()) {
				connection.name = new HandshakeInitiateEvent(recievedEvent).name;
				log("Recieved handshake from : " + connection.name);
				recievedHandshake = true;
			}
		}
	}

	@Override
	public void performDefaultWrite(int count) throws IOException {
		if (recievedHandshake && !sentHandshake) {
			HandshakeResponseEvent response = new HandshakeResponseEvent(connection.ID);
			log("Sent handshake to client with " + connection.ID);
			connection.send(response);
			connection.connectionState = new ConnectionHeartbeatState(connection);
			sentHandshake = true;
		}
	}

}
