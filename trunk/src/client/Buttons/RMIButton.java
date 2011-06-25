package client.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import client.FrontendCommunication.ClientInterface;

public class RMIButton extends JRadioButton implements ActionListener {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = 7198158243063400206L;
	private ClientInterface myClientInterface = null;

	public RMIButton(ClientInterface newClientInterface) {
		super("RMI");
		this.myClientInterface = newClientInterface;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		myClientInterface.setConnType(ClientInterface.CONN_RMI);
		System.out.println("[RMIButton] Selected RMI connection.");
	}
}