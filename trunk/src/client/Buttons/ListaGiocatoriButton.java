package client.Buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import client.FrontendCommunication.ClientInterface;

public class ListaGiocatoriButton extends JButton implements ActionListener {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -2712837566694152504L;
	private ClientInterface myClientInterface = null;
	
	public ListaGiocatoriButton(ClientInterface newClientInterface) {
		super("Lista Giocatori");
		this.myClientInterface = newClientInterface;
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			myClientInterface.doListaGiocatori();
	}
}
