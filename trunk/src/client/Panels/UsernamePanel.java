package client.Panels;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.FrontendCommunication.UserInfo;
import client.InputVerifiers.AlphanumericInput;

public class UsernamePanel extends JPanel implements FocusListener{
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = 7132721055132162577L;
	private UserInfo userInfo = null;

	public UsernamePanel(UserInfo newUserInfo) {
		super(new BorderLayout());
		this.userInfo = newUserInfo;
		add(new JLabel("Username:"), BorderLayout.NORTH);
		JTextField userField = new JTextField("il tuo nome utente");
		userField.setInputVerifier(new AlphanumericInput());
		userField.addFocusListener(this);
		add(userField, BorderLayout.SOUTH);
	}

	@Override
	public void focusGained(FocusEvent e) { }

	@Override
	public void focusLost(FocusEvent e) {
		JTextField myTextField = (JTextField) e.getSource();
		userInfo.setNome(myTextField.getText().toString());
		System.out.println("[UsernamePanel] Copied username to UserInfo.");
	}
}
