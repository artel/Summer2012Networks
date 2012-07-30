package client;

import static network.NetworkProtocol.log;

import java.io.IOException;

import network.HandshakeInitiateEvent;
import network.HandshakeResponseEvent;

public class ClientHandshakeState extends ClientState {
	private boolean dataRecieved = false;
	private boolean sentData = false;

	public ClientHandshakeState(Client client) {
		super(client);
	}

	@Override
	public void handleData(byte[] data) throws IOException {
		super.handleData(data);

		if (sentData && !dataRecieved) {
			if (recievedEvent.isHandshakeResponseEvent()) {
				client.id = new HandshakeResponseEvent(recievedEvent).id;
				log("Response recieved. ID is " + client.id);
				client.clientState = new ClientHeartbeatState(client);
			}
		}
	}

	@Override
	public void performDefaultWrite(int count) throws IOException {
		if (!sentData && !dataRecieved) {
			HandshakeInitiateEvent hie = new HandshakeInitiateEvent(client.name);
			client.send(hie);
			sentData = true;
		}
	}

}
