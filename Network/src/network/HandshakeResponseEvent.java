package network;

import static network.NetworkProtocol.HANDSHAKE;
import static network.NetworkProtocol.HANDSHAKE_RESPONSE_SIZE;
import static network.NetworkProtocol.RESPONSE;

import java.nio.ByteBuffer;

public class HandshakeResponseEvent extends NetworkEvent {
	public static int interpertId(byte[] data) {
		int id;
		ByteBuffer b = ByteBuffer.wrap(data);
		byte firstByte = b.get();
		byte secondByte = b.get();
		if (firstByte != HANDSHAKE || secondByte != RESPONSE) {
			throw new UnsupportedOperationException();
		}
		id = b.getShort();
		return id;
	}
	
	public final int id;
	
	public HandshakeResponseEvent(byte[] data) {
		super(data);
		id = interpertId(this.data);
	}
	
	public HandshakeResponseEvent(int id) {
		super(NetworkProtocol.HANDSHAKE, NetworkProtocol.RESPONSE);
		this.id = id;
		ByteBuffer b = ByteBuffer.allocate(HANDSHAKE_RESPONSE_SIZE);
		b.put(HANDSHAKE);
		b.put(RESPONSE);
		b.putShort((short) id);
		data = b.array();
	}
	
	public HandshakeResponseEvent(NetworkEvent e) {
		super(e);
		id = interpertId(data);
	}
}
