package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;

import client.Buttons.ClassificaButton;
import client.Buttons.ListaGiocatoriButton;
import client.Buttons.LogoutButton;

public class CommonLoggedMenu extends JPanel {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -7171399343852621955L;
	private ClientInterface clientInterface = null;
	private UserInfo userInfo = null;
	
	JButton bListaGiocatori = new ListaGiocatoriButton(clientInterface);
	JButton bClassifica = new ClassificaButton(clientInterface);
	JButton bLogout = new LogoutButton(clientInterface);
	
	public CommonLoggedMenu(ClientInterface newClientInterface, UserInfo newUserInfo) {
		this.clientInterface = newClientInterface;
		this.userInfo = newUserInfo;
		JPanel myPanel = new JPanel(new GridLayout(0, 1));
		myPanel.add(bClassifica);
		myPanel.add(bListaGiocatori);
		myPanel.add(bLogout);
		ButtonGroup myGroup = new ButtonGroup();
		myGroup.add(bClassifica);
		myGroup.add(bListaGiocatori);
		myGroup.add(bLogout);
		add(myPanel, BorderLayout.NORTH);
	}
}
