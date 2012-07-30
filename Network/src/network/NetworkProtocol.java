package network;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class NetworkProtocol {
	public static final byte ALIVE = 0; //[update-request]-[update-state]-[[player-id]]-[alive]; // 5 bytes
		public static final byte DEAD = 1; //[update-request]-[update-state]-[[player-id]]-[dead]; // 5 bytes
		public static final int DEFAULT_BUFFER_ALLOCATION_SIZE = 256;
		
	// Category Commands
	public static final byte HANDSHAKE = 0; 
		public static final int HANDSHAKE_RESPONSE_SIZE = 4;
		// Attribute commands
		public static final byte INITIATE = 0; // [handshake]-[handshake-init]-[name-string-size]-[string... ("..." means size is stated)] = can get over 20 bytes
		public static final byte POSITION = 0; //[update-request]-[update-position]-[[player-id]]-[[x position (two brackets mean 2 bytes)]]-[[y position]] = 8 bytes 
			public static final byte RESPONSE = 1; // [handshake]-[handshake-response]-[[player-id]] = 4 bytes
			public static final byte STATE = 3;
			
		
	public static final byte UPDATE = 1;
	public static final byte VELOCITY = 2; //[update-request]-[update-velocity]-[[player-id]]-[[x velocity (two brackets mean 2 bytes)]]-[[y velocity]] = 8 bytes
	
	
	public static void log(String s) {
		System.out.println(Thread.currentThread().getName() + " : " + s);
	}
	
	public static String readString(ByteBuffer b) {
		byte len = b.get();
		
		byte[] stringInByteArray = new byte[len];
		
		for (int i = 0; i < len; ++i) {
			stringInByteArray[i] = b.get();
		}
		
		try {
			return new String(stringInByteArray, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	public static void writeString(String s, ByteBuffer b) {
		byte[] byteArr = null;
		try {
			byteArr = s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (byteArr.length > Byte.MAX_VALUE) {
			throw new UnsupportedOperationException("String too long");
		}
		byte len = (byte) byteArr.length;
		b.put(len);
		b.put(byteArr);
	}
}
