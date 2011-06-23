package client.Panels;

import java.awt.BorderLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.ClientInterface;

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
		JPanel hostPanel = new JPanel(new BorderLayout());
		hostPanel.add(new JLabel("Hostname:"), BorderLayout.NORTH);
		//JTextField hostField = new JTextField(clientInterface.getHost(), 20);
		JFormattedTextField hostField = new JFormattedTextField();
		hostPanel.add(hostField, BorderLayout.SOUTH);
		add(hostPanel, BorderLayout.NORTH);
		JPanel portPanel = new JPanel(new BorderLayout());
		portPanel.add(new JLabel("Porta:"), BorderLayout.NORTH);
		JTextField portField = new JTextField(clientInterface.getPort().toString(), 6);
		portPanel.add(portField, BorderLayout.SOUTH);
		add(portPanel, BorderLayout.SOUTH);
		// TODO Implementare verifica dell'input!
	}
}
