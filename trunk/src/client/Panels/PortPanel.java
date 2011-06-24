package client.Panels;

import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.FrontendCommunication.ClientInterface;
import client.InputVerifiers.NumericInput;

public class PortPanel extends JPanel implements FocusListener{
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -1936866410764892717L;
	private ClientInterface clientInterface = null;

	public PortPanel(ClientInterface newClientInterface) {
		super(new FlowLayout());
		this.clientInterface = newClientInterface;
		add(new JLabel("Porta:"));
		JTextField portField = new JFormattedTextField(clientInterface.getPort().toString());
		portField.setInputVerifier(new NumericInput());
		add(portField);
	}

	@Override
	public void focusGained(FocusEvent e) { }

	@Override
	public void focusLost(FocusEvent e) {
		JTextField myTextField = (JFormattedTextField) e.getSource();
		clientInterface.setPort(Integer.valueOf(myTextField.getText()));
		System.out.println("[PortPanel] Copied port to ClientInterface.");
	}
}
