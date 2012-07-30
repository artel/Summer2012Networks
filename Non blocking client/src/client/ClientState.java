package client;

import network.NetworkState;

public abstract class ClientState extends NetworkState {

	protected Client client;

	public ClientState(Client client) {
		this.client = client;
	}

}