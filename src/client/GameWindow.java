package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class GameWindow extends JFrame implements ActionListener {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -4796195331011696053L;
	private ClientInterface clientInterface = null;
	private UserInfo userInfo = null;
	
	public GameWindow(ClientInterface newClientInterface, UserInfo newUserInfo) {
		this.clientInterface = newClientInterface;
		this.userInfo = newUserInfo;
		
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
