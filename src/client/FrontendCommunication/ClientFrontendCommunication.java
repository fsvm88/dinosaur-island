package client.FrontendCommunication;

import java.io.IOException;

import client.Exceptions.GenericConnectionException;

interface ClientFrontendCommunication {
	boolean doLogin() throws GenericConnectionException, IOException;
	boolean doLogout();
	Object doClassifica(); // TODO definire il tipo di oggetto!
	Object doListaGiocatori(); // TODO definire il tipo di oggetto!
	boolean doAccediPartita();
}
