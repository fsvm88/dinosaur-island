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
	public static void main (String[] args) { Client client = new Client(); }
	
	private ClientInterface clientInterface = null;
	private UserInfo userInfo = null;
	
	private Selector selectorMenu = null;
	private StartLoggedMenu startLoggedMenu = null;
	private GameWindow gameWindow = null;
	
	public Client() {
		// Set native look and feel
		setNativeLookAndFeel();
		
		System.out.println("[Client] Initializing data structures...");
		userInfo = new UserInfo();
		clientInterface = new ClientInterface(userInfo);
		System.out.println("[Client] Data structures initialized, threading ClientInterface...");
		Thread threadedCI = new Thread(clientInterface);
		System.out.println("[Client] ClientInterface threaded, starting...");
		threadedCI.start();
		
		selectorMenu = new Selector(clientInterface, userInfo);
		startLoggedMenu = new StartLoggedMenu(clientInterface, userInfo);
		gameWindow = new GameWindow(clientInterface, userInfo);
		System.out.println("[Client] Data structures initialized, going GUI...");
		while (true) {
			// Attendi il login, poi procedi oltre.
			if (!userInfo.isLogged()) { selectorMenu.setVisible(true); }
			while (!userInfo.isLogged()) { try { Thread.sleep(250); } catch (InterruptedException e) { e.printStackTrace();} }
			selectorMenu.setVisible(false);
			// Se il login ha avuto successo aggiungo un hook di shutdown di modo da ricordarmi di sloggarmi dal server (implementa anche l'uscita dalla partita).
			System.out.println("[Client] Adding logout shutdown hook...");
			// TODO implementa shutdown hook!
			System.out.println("[Client] Shutdown logout hook successfully added.");
			// Attendi l'entrata in partita, poi procedi oltre.
			if (userInfo.isLogged() && !userInfo.isInGame()) { startLoggedMenu.setVisible(true); }
			while (userInfo.isLogged() && !userInfo.isInGame()) { try { Thread.sleep(250); } catch (InterruptedException e) { e.printStackTrace();} }
			startLoggedMenu.setVisible(false);
			// Fintanto che sei in partita, attendi e lascia giocare l'utente.
			if (userInfo.isLogged() && userInfo.isInGame()) { gameWindow.setVisible(true); }
			while (userInfo.isLogged() && userInfo.isInGame()) { try { Thread.sleep(250); } catch (InterruptedException e) { e.printStackTrace();} }
			gameWindow.setVisible(false);
		}
	}
	
	private static void setNativeLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) { System.out.println("Problema con Look and Feel nativo" + e); }
	}
}