package client.Panels;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.ClientInterface;
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
		JPanel userInputPanel = new JPanel(new BorderLayout());
		userInputPanel.add(new JLabel("Username:"), BorderLayout.NORTH);
		JTextField userField = new JTextField("il tuo nome utente");
		userInputPanel.add(userField, BorderLayout.SOUTH);
		JPanel passwordInputPanel = new JPanel(new BorderLayout());
		passwordInputPanel.add(new JLabel("Password:"), BorderLayout.NORTH);
		JPasswordField passwordField = new JPasswordField("la tua password", 20);
		passwordInputPanel.add(passwordField, BorderLayout.SOUTH);
		add(userInputPanel, BorderLayout.NORTH);
		add(passwordInputPanel, BorderLayout.SOUTH);
		
		// TODO Implementare verifica dell'input!
	}
}
