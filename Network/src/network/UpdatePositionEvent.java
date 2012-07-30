package network;

import static network.NetworkProtocol.*;

import java.nio.ByteBuffer;

public class UpdatePositionEvent extends NetworkEvent {
	
	public UpdatePositionEvent(byte[] data) {
		super(data);
		
		ByteBuffer b = ByteBuffer.wrap(data);
		byte firstByte = b.get();
		byte secondByte = b.get();
		
		if (firstByte != UPDATE || secondByte != POSITION) {
			throw new UnsupportedOperationException();
		}
		
		super.data = b.array();
		id = b.getShort();
		x = b.getShort();
		y = b.getShort();
	}
	
	public UpdatePositionEvent(int id, int x, int y) {
		super(UPDATE, POSITION);
		this.id = id;
		this.x = x;
		this.y = y;
		
		ByteBuffer b = ByteBuffer.allocate(UPDATE_POSITION_SIZE);
		b.put(UPDATE);
		b.put(POSITION);
		b.putShort((short) id);
		b.putShort((short) x);
		b.putShort((short) y);
		
		super.data = b.array();
	}
	
	public UpdatePositionEvent(NetworkEvent e) {
		super(e);
		
		ByteBuffer b = ByteBuffer.wrap(data);
		byte firstByte = b.get();
		byte secondByte = b.get();
		
		if (firstByte != UPDATE || secondByte != POSITION) {
			throw new UnsupportedOperationException();
		}
		
		id = b.getShort();
		x = b.getShort();
		y = b.getShort();
		super.data = b.array();
	}
	
	public final int id;
	public final int x;
	public final int y;
	//[update-request]-[update-position]-[[player-id]]-[[x position (two brackets mean 2 bytes)]]-[[y position]] = 8 bytes
}
