package client.Panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import client.FrontendCommunication.ClientInterface;

public class ParametersPanel extends JPanel {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -4572329194186434115L;
	private ClientInterface clientInterface = null;

	public ParametersPanel(ClientInterface newClientInterface) {
		super(new BorderLayout());
		this.clientInterface = newClientInterface;
		// Aggiungi i textfield per l'host e la porta
		add(new HostPanel(clientInterface), BorderLayout.NORTH);
		add(new PortPanel(clientInterface), BorderLayout.WEST);
	}
}
