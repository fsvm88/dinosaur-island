package client.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import client.FrontendCommunication.ClientInterface;

public class SocketButton extends JRadioButton implements ActionListener {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -4822032013149929748L;
	private ClientInterface myClientInterface = null;

	public SocketButton(ClientInterface newClientInterface) {
		super("Socket");
		this.myClientInterface = newClientInterface;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		myClientInterface.setConnType(ClientInterface.CONN_SOCKET);
		System.out.println("[SocketButton] Selected Socket connection.");
	}
}