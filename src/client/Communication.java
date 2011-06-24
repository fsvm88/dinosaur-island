package client;

interface Communication {
	boolean doLogin();
	boolean doLogout();
	Object doClassifica(); // TODO definire il tipo di oggetto!
	Object doListaGiocatori(); // TODO definire il tipo di oggetto!
	boolean doAccediPartita();
}
