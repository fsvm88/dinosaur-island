package client.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import client.FrontendCommunication.ClientInterface;

public class LogoutButton extends JButton implements ActionListener {
	/**
	* Default generated Serial Version ID.
	*/
	private static final long serialVersionUID = 8941460139912468868L;
	private ClientInterface myClientInterface = null;
	
	public LogoutButton(ClientInterface newClientInterface) {
		super("Logout");
		this.myClientInterface = newClientInterface;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			myClientInterface.doLogout();
	}
}
