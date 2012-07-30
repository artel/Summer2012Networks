package network;

import java.io.IOException;

public interface Communicable {
	public NetworkEvent getEvent(byte[] data);
	
	public void send(NetworkEvent e) throws IOException;
}
