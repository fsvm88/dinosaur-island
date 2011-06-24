package client.Panels;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.FrontendCommunication.ClientInterface;
import client.FrontendCommunication.UserInfo;
import client.InputVerifiers.AlphanumericInput;
import client.InputVerifiers.AlphanumericPlusDotInput;

public class HostPanel extends JPanel implements FocusListener{
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = 1740446437579289546L;
	private ClientInterface clientInterface = null;

	public HostPanel(ClientInterface newClientInterface) {
		super(new BorderLayout());
		this.clientInterface = newClientInterface;
		add(new JLabel("Hostname:"), BorderLayout.NORTH);
		JFormattedTextField hostField = new JFormattedTextField(clientInterface.getHost());
		hostField.setInputVerifier(new AlphanumericPlusDotInput());
		add(hostField, BorderLayout.SOUTH);
	}

	@Override
	public void focusGained(FocusEvent e) { }

	@Override
	public void focusLost(FocusEvent e) {
		JTextField myTextField = (JTextField) e.getSource();
		clientInterface.setHost(myTextField.getText().toString());
		System.out.println("[HostPanel] Copied hostname to ClientInterface.");
	}
}
