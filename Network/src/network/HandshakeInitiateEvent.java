package network;

import static network.NetworkProtocol.HANDSHAKE;
import static network.NetworkProtocol.INITIATE;
import static network.NetworkProtocol.readString;
import static network.NetworkProtocol.writeString;

import java.nio.ByteBuffer;

public class HandshakeInitiateEvent extends NetworkEvent {	
	private static String interpertName(byte[] data) {
		String name;
		ByteBuffer b = ByteBuffer.wrap(data);
		byte firstByte = b.get();
		byte secondByte = b.get();
		if (firstByte != HANDSHAKE || secondByte != INITIATE) {
			throw new UnsupportedOperationException();
		}
		name = readString(b);
		return name;
	}
	
	public final String name;
	
	public HandshakeInitiateEvent(byte[] data) {
		super(data);
		name = interpertName(this.data);
	}

	public HandshakeInitiateEvent(NetworkEvent recieved) {
		super(recieved);
		name = interpertName(data);
	}

	public HandshakeInitiateEvent(String name) {
		super(NetworkProtocol.HANDSHAKE, NetworkProtocol.INITIATE);
		ByteBuffer b = ByteBuffer.allocate(NetworkProtocol.DEFAULT_BUFFER_ALLOCATION_SIZE);
		b.put(HANDSHAKE);
		b.put(INITIATE);
		writeString(name, b);
		data = b.array();
		this.name = name;
	}
}
