package server;

import java.io.IOException;

public class ConnectionHeartbeatState extends ConnectionState {

	protected ConnectionHeartbeatState(Connection connection) {
		super(connection);
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
