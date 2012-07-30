package server;

import network.NetworkState;
public abstract class ConnectionState extends NetworkState {
	
	protected Connection connection;
	
	protected ConnectionState(Connection connection) {
		this.connection = connection;
	}
}
