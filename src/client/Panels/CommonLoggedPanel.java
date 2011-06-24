package client.Panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;

import client.Buttons.ClassificaButton;
import client.Buttons.ListaGiocatoriButton;
import client.Buttons.LogoutButton;
import client.FrontendCommunication.ClientInterface;
import client.FrontendCommunication.UserInfo;

public class CommonLoggedPanel extends JPanel {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -7171399343852621955L;
	private ClientInterface clientInterface = null;
	private UserInfo userInfo = null;
	
	public CommonLoggedPanel(ClientInterface newClientInterface, UserInfo newUserInfo) {
		super(new GridLayout(0, 1));
		this.clientInterface = newClientInterface;
		this.userInfo = newUserInfo;
		JButton bListaGiocatori = new ListaGiocatoriButton(clientInterface);
		JButton bClassifica = new ClassificaButton(clientInterface);
		JButton bLogout = new LogoutButton(clientInterface);
		ButtonGroup myGroup = new ButtonGroup();
		myGroup.add(bClassifica);
		myGroup.add(bListaGiocatori);
		myGroup.add(bLogout);
		add(bClassifica);
		add(bListaGiocatori);
		add(bLogout);
	}
}
