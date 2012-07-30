package network;

import java.io.IOException;

public interface StateDataHandler {
	
	abstract void handleData(byte[] data) throws IOException;

	abstract void performDefaultWrite(int count) throws IOException;
}
