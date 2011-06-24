package client.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;

import client.Exceptions.GenericConnectionException;
import client.FrontendCommunication.ClientInterface;

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
		try {
			myClientInterface.doLogin();
		}
		catch (GenericConnectionException e1) {
			// TODO implementare JDialog di errores
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
