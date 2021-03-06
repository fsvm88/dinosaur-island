package client.MainFrames;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import client.Buttons.AccediAPartitaButton;
import client.FrontendCommunication.ClientInterface;
import client.FrontendCommunication.UserInfo;
import client.Panels.CommonLoggedPanel;

public class StartLoggedMenu extends JFrame implements ActionListener {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = 5255375846104173894L;
	private ClientInterface clientInterface = null;
	private UserInfo userInfo = null;
	
	JButton bAccediAPartita = new JButton("Accesso alla partita!");
	
	public StartLoggedMenu(ClientInterface newClientInterface, UserInfo newUserInfo) {
		super("Dino Island - Logged");
		this.clientInterface = newClientInterface;
		this.userInfo = newUserInfo;
		setResizable(false);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel loggedPanel = new JPanel(new GridLayout(0, 1));
		loggedPanel.add(new AccediAPartitaButton(clientInterface, userInfo));
		loggedPanel.add(new CommonLoggedPanel(clientInterface, userInfo));
		add(loggedPanel);
		
		pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
