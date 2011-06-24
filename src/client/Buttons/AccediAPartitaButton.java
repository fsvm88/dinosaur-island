package client.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import client.ClientInterface;
import client.UserInfo;

public class AccediAPartitaButton extends JButton implements ActionListener {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -4771177521681799330L;
	private ClientInterface myClientInterface = null;
	private UserInfo myUserInfo = null;
	
	public AccediAPartitaButton(ClientInterface newClientInterface, UserInfo newUserInfo) {
		super("Accedi alla partita");
		this.myClientInterface = newClientInterface;
		this.myUserInfo = newUserInfo;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			myClientInterface.doAccediPartita();
	}
}
