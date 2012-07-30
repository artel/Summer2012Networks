package client;

import java.io.IOException;

public class ClientHeartbeatState extends ClientState {
	int dy, dx, x, y;

	public ClientHeartbeatState(Client client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleData(byte[] data) throws IOException {
		super.handleData(data);
	}

	@Override
	public void performDefaultWrite(int count) throws IOException {
		if (count % 1000 == 0) {

		}
	}

}
