package network;

import java.io.IOException;


public abstract class NetworkState implements StateDataHandler {
	
	protected NetworkEvent recievedEvent;
	
	@Override
	public void handleData(byte[] data) throws IOException  {
		byte[] dataCopy = new byte[data.length];
		System.arraycopy(data, 0, dataCopy, 0, data.length);
		recievedEvent = new NetworkEvent(dataCopy);
	}
	
	@Override
	public abstract void performDefaultWrite(int count) throws IOException;
}
