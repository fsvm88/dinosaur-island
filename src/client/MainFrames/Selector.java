package client.MainFrames;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.Buttons.LoginButton;
import client.FrontendCommunication.ClientInterface;
import client.FrontendCommunication.UserInfo;
import client.Panels.ConnectionPanel;
import client.Panels.ParametersPanel;
import client.Panels.UserInputPanel;

public class Selector extends JFrame {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -932741274463554786L;
	private ClientInterface clientInterface = null;
	private UserInfo userInfo = null;

	public Selector(ClientInterface newClientInterface, UserInfo newUserInfo) {
		// Titolo della finestra
		super("Dino Island");
		this.clientInterface = newClientInterface;
		this.userInfo = newUserInfo;
		// Non permettere di ridimensionarla
		setResizable(false);
		// Imposta il layout border
		setLayout(new BorderLayout());
		// Imposta l'operazione di default sul close
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Imposta l'etichetta di default da far comparire sul bordo in alto
		JPanel labelPanel = new JPanel(new GridLayout(0,1));
		labelPanel.add(new JLabel("Benvenuto su Dino Island!"));
		labelPanel.add(new JLabel("Scegli il metodo di connessione."));
		labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		getContentPane().add(labelPanel, BorderLayout.NORTH);

		// Crea un pannello per gestire la metà inferiore della finestra
		JPanel lowerPanel = new JPanel(new BorderLayout());
		// Aggiungi il pannello per selezionare il tipo di connessione
		lowerPanel.add(new ConnectionPanel(clientInterface), BorderLayout.WEST);
		
		// Crea Pannello inferiore destro
		JPanel rightLowerPanel = new JPanel(new BorderLayout());
		rightLowerPanel.add(new ParametersPanel(clientInterface), BorderLayout.NORTH);
		
		// Aggiungi i textfield con controllo dell'input
		rightLowerPanel.add(new UserInputPanel(userInfo), BorderLayout.SOUTH);
		rightLowerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));	
		lowerPanel.add(rightLowerPanel, BorderLayout.CENTER);
		
		// Aggiungi il bottone Login
		lowerPanel.add(new LoginButton(clientInterface), BorderLayout.EAST);
		lowerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		add(lowerPanel, BorderLayout.SOUTH);
		pack();
	}
}
