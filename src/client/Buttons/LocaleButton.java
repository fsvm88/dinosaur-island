package client.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import client.FrontendCommunication.ClientInterface;

public class LocaleButton extends JRadioButton implements ActionListener {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -4878163371634553764L;
	private ClientInterface myClientInterface = null;

	public LocaleButton(ClientInterface newClientInterface) {
		super("Locale");
		this.myClientInterface = newClientInterface;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		myClientInterface.setConnType(ClientInterface.CONN_LOCAL);
		System.out.println("[LocaleButton] Selected Local connection.");
	}
}