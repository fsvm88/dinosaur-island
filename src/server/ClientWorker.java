package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

import dinolib.*;
import dinolib.Mappa.Coord;

/** 
 * Implementa l'ascoltatore per i client. Ascolta i comandi e gestisce le giuste risposte.
 */
class ClientWorker extends Server implements Runnable {
	/**
	 * Variabile per fermare il thread.
	 * @uml.property  name="stopThread"
	 */
	private boolean stopThread = false;
	/**
	 * Istanzia riferimento al socket.
	 * @uml.property  name="mySocket"
	 */
	private Socket mySocket = null;
	/**
	 * Istanzia riferimento al buffer per la ricezione dei dati.
	 * @uml.property  name="BufferedReader"
	 */
	private BufferedReader incomingData = null;
	/**
	 * Istanzia riferimento al buffer per l'invio dei dati.
	 * @uml.property  name="outgoingData"
	 */
	private PrintWriter outgoingData = null;
	/**
	 * Istanzia riferimento all'adattatore per il socket.
	 * @uml.property name="socketAdapter"
	 */
	private SocketAdapter socketAdapter = null; 

	/**
	 * Costruttore pubblico e unico costruttore definito per ClientWorker.
	 * @param socket
	 * @throws IOException
	 */
	protected ClientWorker(Socket socket) throws IOException {
		mySocket = socket;
		try {
			incomingData = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			outgoingData = new PrintWriter(mySocket.getOutputStream(), true);
			socketAdapter = new SocketAdapter(servLogica);
		}
		catch (IOException e) {
			terminateThreadOnIOException("Cannot initialize input/output streams!");
		}
	}

	private String estraiUser(Scanner scanner) {
		return scanner.next(Pattern.compile("[^(user=)]"));
	}

	private String estraiPwd(Scanner scanner) {
		return scanner.next(Pattern.compile("[^(pass=)]"));
	}

	private String getToken(Scanner scanner) {
		if (scanner.hasNext()) {
			return scanner.next(Pattern.compile("[^(token=)]"))	;
		}
		else return null;
	}

	private boolean isLoginOrCreation(String comando) {
		if (comando.equals("@creaUtente") || comando.equals("@login")) return true;
		else return false;
	}

	private boolean isGoodToken(String token) {
		if (token != null) return true;
		else return false;
	}

	private String estraiRazza(Scanner scanner) {
		return scanner.next(Pattern.compile("[^(nome=)]"));
	}

	private Character estraiTipo(Scanner scanner) {
		String myString = scanner.next("[^(tipo=)]");
		return myString.charAt(0);
	}

	private boolean validaRazzaETipo(String nomeRazza, Character tipo) {
		if ((nomeRazza != null) && (tipo != null) ) return true;
		else return false;
	}

	/**
	 * Invia no al client, se viene aggiunta una stringa come argomento la aggiunge.
	 * @param toAppend
	 * @throws IOException 
	 */
	private void writeNoToOutput(String...toAppend) throws IOException {
		if (toAppend != null) {
			writeLineToOutput("@no" + "," + toAppend[0]);
		}
		else writeLineToOutput("@no");
	}

	/**
	 * Implementazione del metodo run dall'interfaccia runnable, avvia il thread.
	 * Fa il parsing dei comandi e chiama le azioni appropriate su logica tramite gli adattatori (a<NomeFunzione>) o direttamente.
	 */
	public void run () {
		while (isServerRunning() && !stopThread) {
			try {
				Scanner scanner = new Scanner(readLineFromInput());
				scanner.useDelimiter(",");
				if (scanner.hasNext()) {
					String comando = scanner.next();
					if (isLoginOrCreation(comando)) {
						String user = null;
						String pwd = null;
						if (scanner.hasNext()) {
							user = estraiUser(scanner);
							if (scanner.hasNext()) {
								pwd = estraiPwd(scanner);
							}
							else writeNoToOutput();
						}
						else writeNoToOutput();
						if (comando.equals("@creaUtente")) {
							writeLineToOutput((String) socketAdapter.creaUtente(user, pwd));
						}
						else if (comando.equals("@login")) {
							writeLineToOutput((String) socketAdapter.loginUtente(user, pwd));
						}
						else writeNoToOutput();
					}
					else if (!isLoginOrCreation(comando)) {
						String token = getToken(scanner);
						if (isGoodToken(token)) {
							/* comandi fuori partita*/
							if (comando.equals("@creaRazza")) {
								String nomeRazza = null;
								Character tipoRazza = null;
								if (scanner.hasNext()) {
									nomeRazza = estraiRazza(scanner);
									if (scanner.hasNext()) {
										tipoRazza = estraiTipo(scanner);
									}
									else writeNoToOutput();
								}
								else writeNoToOutput();
								if (validaRazzaETipo(nomeRazza, tipoRazza)) {
									writeLineToOutput((String) socketAdapter.creaRazza(token, nomeRazza, tipoRazza));
								}
							}
							else if (comando.equals("@accessoPartita")) {
								writeLineToOutput((String) socketAdapter.accessoPartita(token));
							}
							else if (comando.equals("@uscitaPartita")) {
								writeLineToOutput((String) socketAdapter.uscitaPartita(token));
							}
							else if (comando.equals("@listaGiocatori")) {
								writeLineToOutput((String) socketAdapter.listaGiocatori(token));
							}
							else if (comando.equals("@classifica")) {
								writeLineToOutput((String) socketAdapter.classifica(token));
							}
							else if (comando.equals("@logout")) {
								writeLineToOutput((String) socketAdapter.logoutUtente(token));
							}
							/* comandi in partita */
							/* comandi di informazione */
							else if (comando.equals("@mappaGenerale")) {
								writeLineToOutput((String) socketAdapter.mappaGenerale(token));
							}
							else if (comando.equals("@listaDinosauri")) {
								writeLineToOutput((String) socketAdapter.listaDinosauri(token));
							}
							/* comandi di turno */
							else if (comando.equals("@confermaTurno")) {
								writeLineToOutput((String) socketAdapter.confermaTurno(token));
							}
							else if (comando.equals("@passaTurno")) {
								writeLineToOutput((String) socketAdapter.passaTurno(token));
							}
							else if (scanner.hasNext()) {
								String idDinosauro = scanner.next(Pattern.compile("[^(idDino=)]"));
								if (comando.equals("@vistaLocale")) {
									writeLineToOutput((String) socketAdapter.vistaLocale(token, idDinosauro));
								}
								else if (comando.equals("@statoDinosauro")) {
									writeLineToOutput((String) socketAdapter.statoDinosauro(token, idDinosauro));
								}
								else if (comando.equals("@cresciDinosauro")) {
									writeLineToOutput((String) socketAdapter.cresciDinosauro(token, idDinosauro));
								}
								else if (comando.equals("@deponiUovo")) {
									writeLineToOutput((String) socketAdapter.deponiUovo(token, idDinosauro));
								}
								else if (comando.equals("@muoviDinosauro")) {
									int x = 0;
									int y = 0;
									if (scanner.hasNextInt()) {
										x = scanner.nextInt();
										if (scanner.hasNextInt()) {
											y = scanner.nextInt();
											writeLineToOutput((String) socketAdapter.muoviDinosauro(token, idDinosauro, new Coord(x, y)));
										}
										else writeNoToOutput();
									}
									else writeNoToOutput();
								}
							}
						}
					}
				}
			}
			catch (IOException e) {
				terminateThreadOnIOException("Unable to communicate with the client, stopping thread");
			}
		}
	}

	/* Quattro helper molto generici di cui due per l'IO per il client e due per fermare il thread. */
	/**
	 * Helper per ricevere comandi dal client
	 * @return
	 * @throws IOException
	 */
	private String readLineFromInput () throws IOException {
		return incomingData.readLine();
	}

	/**
	 * Helper per rispondere ai comandi del client

	 * @param toSend
	 * @throws IOException
	 */
	private void writeLineToOutput (String...toSend) throws IOException {
		outgoingData.println(toSend[0]);
	}

	/**
	 * Helper per fermare il thread, imposta stopThread a true.
	 */
	public void stopTheThread () {
		stopThread = true;
	}

	/** 
	 * Termina il thread su eccezione:
	 * dà notifica con motivazione e imposta il parametro per lo stop.
	 * @param cause 
	 */
	private void terminateThreadOnIOException (String cause) {
		System.out.println(cause);
		System.out.println("Killing this thread!");
		stopTheThread();
	}
}