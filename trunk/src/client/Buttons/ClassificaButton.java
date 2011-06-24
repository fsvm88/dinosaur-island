package client.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import client.FrontendCommunication.ClientInterface;

public class ClassificaButton extends JButton implements ActionListener {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -8930290644109116843L;
	private ClientInterface myClientInterface = null;
	
	public ClassificaButton(ClientInterface newClientInterface) {
		super("Classifica");
		this.myClientInterface = newClientInterface;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			myClientInterface.doClassifica();
	}
}
