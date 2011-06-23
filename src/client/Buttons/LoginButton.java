package client.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import client.ClientInterface;

public class LoginButton extends JButton implements ActionListener {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -1814625966216966410L;
	private ClientInterface myClientInterface = null;
	
	public LoginButton(ClientInterface newClientInterface) {
		super("Login");
		this.myClientInterface = newClientInterface;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			myClientInterface.doLogin();
	}
}
