package client.Panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import client.UserInfo;

public class UserInputPanel extends JPanel {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = 3878765106118200645L;
	private UserInfo userInfo = null;

	public UserInputPanel(UserInfo newUserInfo) {
		super(new BorderLayout());
		this.userInfo = newUserInfo;
		// Aggiungi i textfield per l'utente e la password
		JPanel usernamePanel = new UsernamePanel(userInfo);
		JPanel passwordPanel = new PasswordPanel(userInfo);
		add(usernamePanel, BorderLayout.NORTH);
		add(passwordPanel, BorderLayout.SOUTH);
	}
}
