package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import client.Buttons.LoginButton;
import client.Panels.ParametersPanel;
import client.Panels.UserInputPanel;

public class Selector extends JFrame implements ActionListener {
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
		labelPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		getContentPane().add(labelPanel, BorderLayout.NORTH);

		// Crea un pannello per gestire la metà inferiore della finestra
		JPanel lowerPanel = new JPanel(new BorderLayout());
		
		// Crea un pannello di scelta con RadioButton. Usa Layout Grid e aggiungi i bottoni a un gruppo di modo che siano mutuamente escludenti
		JPanel choicePanel = new JPanel(new GridLayout(0, 1));
		ButtonGroup buttonGroup = new ButtonGroup();
		JRadioButton rmiButton = new JRadioButton("RMI");
		JRadioButton socketButton = new JRadioButton("Socket");
		JRadioButton localeButton = new JRadioButton("Locale");
		buttonGroup.add(rmiButton);
		buttonGroup.add(socketButton);
		buttonGroup.add(localeButton);

		// Aggiungi i bottoni al pannello di scelta e crea un bordo uniforme attorno di 20 pixel
		JPanel leftLowerPanel = new JPanel(new BorderLayout());
		choicePanel.add(rmiButton);
		choicePanel.add(socketButton);
		choicePanel.add(localeButton);
		choicePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		leftLowerPanel.add(choicePanel, BorderLayout.WEST);
		lowerPanel.add(leftLowerPanel, BorderLayout.WEST);
		
		// Crea Pannello inferiore destro
		JPanel rightLowerPanel = new JPanel(new BorderLayout());
		rightLowerPanel.add(new ParametersPanel(clientInterface), BorderLayout.NORTH);
		
		// Aggiungi i textfield con controllo dell'input
		rightLowerPanel.add(new UserInputPanel(userInfo), BorderLayout.SOUTH);
		rightLowerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));	
		lowerPanel.add(rightLowerPanel, BorderLayout.CENTER);
		
		// Aggiungi il bottone Login
		lowerPanel.add(new LoginButton(clientInterface), BorderLayout.EAST);
		lowerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		add(lowerPanel, BorderLayout.SOUTH);
		// Aggiungi il pannello di scelta alla finestra principale e fallo comparire a sinistra
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
