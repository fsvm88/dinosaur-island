package client.Panels;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.FrontendCommunication.UserInfo;
import client.InputVerifiers.AlphanumericInput;

public class PasswordPanel extends JPanel implements FocusListener{
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = 1594678875399601555L;
	private UserInfo userInfo = null;

	public PasswordPanel(UserInfo newUserInfo) {
		super(new BorderLayout());
		this.userInfo = newUserInfo;
		add(new JLabel("Password:"), BorderLayout.NORTH);
		JPasswordField passwordField = new JPasswordField("la tua password", 20);
		passwordField.setInputVerifier(new AlphanumericInput());
		passwordField.addFocusListener(this);
		add(passwordField, BorderLayout.SOUTH);
	}

	@Override
	public void focusGained(FocusEvent e) { }

	@Override
	public void focusLost(FocusEvent e) {
		JTextField myTextField = (JTextField) e.getSource();
		userInfo.setPwd(myTextField.getText().toString());
		System.out.println("[PasswordPanel] Copied password to UserInfo.");
	}
}
