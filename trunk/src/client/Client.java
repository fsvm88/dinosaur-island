package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;

import client.Client;


public class Client {
	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main (String[] args) {
		Client client = new Client();
	}
	
	public Client() {
		createSelectorGUI();
	}
	
	private static void createSelectorGUI() {
		// Imposta il look and feel nativo
		setNativeLookAndFeel();
		JFrame.setDefaultLookAndFeelDecorated(true);
		// Titolo della finestra
		JFrame mainFrame = new JFrame("Dino Island");
		// Non permettere di ridimensionarla
		mainFrame.setResizable(false);
		// Imposta il layout border
		mainFrame.setLayout(new BorderLayout());
		// Imposta l'operazione di default sul close
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Imposta l'etichetta di default da far comparire sul bordo in alto
		JLabel selLabel = new JLabel("Benvenuto su Dino Island!");
		JLabel selLabel2 = new JLabel("Scegli il metodo di connessione e se necessario immetti hostname e porta se diversi da quelli di default.");
		JPanel labelPanel = new JPanel(new GridLayout(0,1));
		labelPanel.add(selLabel);
		labelPanel.add(selLabel2);
		labelPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
		mainFrame.getContentPane().add(labelPanel, BorderLayout.NORTH);
		
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
		choicePanel.add(rmiButton);
		choicePanel.add(socketButton);
		choicePanel.add(localeButton);
		choicePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		// Aggiungi il pannello di scelta alla finestra principale e fallo comparire a sinistra
		mainFrame.add(choicePanel, BorderLayout.WEST);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	private static void setNativeLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) { System.out.println("Problema con Look and Feel nativo" + e); }
	}
}