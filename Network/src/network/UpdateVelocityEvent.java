package network;

import static network.NetworkProtocol.*;

import java.nio.ByteBuffer;

public class UpdateVelocityEvent extends NetworkEvent {
	
	public UpdateVelocityEvent(byte[] data) {
		super(data);
		
		ByteBuffer b = ByteBuffer.wrap(data);
		byte firstByte = b.get();
		byte secondByte = b.get();
		
		if (firstByte != UPDATE || secondByte != POSITION) {
			throw new UnsupportedOperationException();
		}
		
		super.data = b.array();
		id = b.getShort();
		dx = b.getShort();
		dy = b.getShort();
	}
	
	public UpdateVelocityEvent(int id, int dx, int dy) {
		super(UPDATE, VELOCITY);
		this.id = id;
		this.dx = dx;
		this.dy = dy;
		
		ByteBuffer b = ByteBuffer.allocate(UPDATE_POSITION_SIZE);
		b.put(UPDATE);
		b.put(VELOCITY);
		b.putShort((short) id);
		b.putShort((short) dx);
		b.putShort((short) dy);
		
		super.data = b.array();
	}
	
	public UpdateVelocityEvent(NetworkEvent e) {
		super(e);
		
		ByteBuffer b = ByteBuffer.wrap(data);
		byte firstByte = b.get();
		byte secondByte = b.get();
		
		if (firstByte != UPDATE || secondByte != VELOCITY) {
			throw new UnsupportedOperationException();
		}
		
		id = b.getShort();
		dx = b.getShort();
		dy = b.getShort();
		super.data = b.array();
	}
	
	public final int id;
	public final int dx;
	public final int dy;
	//[update-request]-[update-position]-[[player-id]]-[[x position (two brackets mean 2 bytes)]]-[[y position]] = 8 bytes
	
	public static void main(String[] args) {
		UpdateVelocityEvent uve = new UpdateVelocityEvent(900, 3, 44444);
		byte[] recievedData = uve.toByteArray();
		
		NetworkEvent someEvent = new NetworkEvent(recievedData);
		if (someEvent.isUpdateVelocityEvent()) {
			System.out.println("yea I AM AN update VELOCITY EVENT");
			UpdateVelocityEvent uve2 = new UpdateVelocityEvent(someEvent);
			System.out.println("id : " + uve2.id + " dx " + uve2.dx + " dy " + uve2.dy);
		}
	}
}
