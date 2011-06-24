package client.Panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import client.ClientInterface;
import client.Buttons.LocaleButton;
import client.Buttons.RMIButton;
import client.Buttons.SocketButton;

public class ConnectionPanel extends JPanel {
	/**
	 * Default generated Serial Version ID.
	 */
	private static final long serialVersionUID = -370084363061140656L;
	private ClientInterface clientInterface = null;

	public ConnectionPanel(ClientInterface newClientInterface) {
		super(new BorderLayout());
		this.clientInterface = newClientInterface;
		
		JPanel choicePanel = new JPanel(new GridLayout(0, 1));
		JRadioButton rmiButton = new RMIButton(clientInterface);
		JRadioButton socketButton = new SocketButton(clientInterface);
		JRadioButton localeButton = new LocaleButton(clientInterface);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rmiButton);
		buttonGroup.add(socketButton);
		buttonGroup.add(localeButton);
		
		choicePanel.add(rmiButton);
		choicePanel.add(socketButton);
		choicePanel.add(localeButton);
		choicePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(choicePanel, BorderLayout.WEST);
	}
}
