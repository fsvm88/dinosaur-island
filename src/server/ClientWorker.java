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
 * @author fabio
 */
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
	 * Prende in ingresso il socket per comunicare con l'esterno.
	 * @param socket Il socket attraverso cui passa la comunicazione.
	 * @throws IOException Se ci sono problemi con la comunicazione.
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
	/**
	 * Estrae il nome utente dallo scanner.
	 * @param scanner Lo scanner da cui estrarre il nome utente.
	 * @return Una stringa che contiene il nome utente.
	 */
	private String estraiUser(Scanner scanner) { return scanner.next(Pattern.compile("[^(user=)]")); }
	/**
	 * Estrae la password dallo scanner.
	 * @param scanner Lo scanner da cui estrarre la password dell'utente.
	 * @return Una stringa che contiene la password dell'utente.
	 */
	private String estraiPwd(Scanner scanner) { return scanner.next(Pattern.compile("[^(pass=)]")); }
	/**
	 * Estra il token dallo scanner.
	 * @param scanner Lo scanner da cui estrarre il token dell'utente.
	 * @return Una stringa che contiene il token dell'utente.
	 */
	private String getToken(Scanner scanner) {
		if (scanner.hasNext()) { return scanner.next(Pattern.compile("[^(token=)]")); }
		else { return null; }
	}
	/**
	 * Dice se e' un comando di login o creazione.
	 * @param comando Il comando da verificare.
	 * @return True se e' un comando di login o creazione, false altrimenti
	 */
	private boolean isLoginOrCreation(String comando) {
		if (comando.equals("@creaUtente") || comando.equals("@login")) { return true; }
		else { return false; }
	}
	/**
	 * Verifica se il token e' un token valido (non nullo).
	 * @param token Il token da verificare.
	 * @return True se il token e' buono, false altrimenti.
	 */
	private boolean isGoodToken(String token) {
		if (token != null) { return true; }
		else { return false; }
	}
	/**
	 * Estrae il nome della razza dallo scanner.
	 * @param scanner Lo scanner da cui estrarre il nome della razza.
	 * @return Una stringa che contiene il nome della razza.
	 */
	private String estraiRazza(Scanner scanner) { return scanner.next(Pattern.compile("[^(nome=)]")); }
	/**
	 * Estrae il tipo della razza dallo scanner.
	 * @param scanner Lo scanner da cui estrarre il tipo della razza.
	 * @return Un Character che contiene il tipo della razza.
	 */
	private Character estraiTipo(Scanner scanner) { String myString = scanner.next("[^(tipo=)]"); return myString.charAt(0); }
	/**
	 * Valida il nome e il tipo della razza (entrambi non devono essere nulli).
	 * @param nomeRazza Il nome della razza da validare.
	 * @param tipo Il tipo della razza da validare.
	 * @return True se il nome della razza e il tipo non sono null, false altrimenti.
	 */
	private boolean validaRazzaETipo(String nomeRazza, Character tipo) {
		if ((nomeRazza != null) && (tipo != null) ) { return true; }
		else { return false; }
	}
	/**
	 * Invia no al client, se viene aggiunta una stringa come argomento la aggiunge.
	 * @param toAppend La stringa da appendere (opzionale).
	 * @throws IOException Se ci sono errori di comunicazione.
	 */
	private void writeNoToOutput(String...toAppend) throws IOException {
		if (toAppend != null) {
			writeLineToOutput("@no" + "," + toAppend[0]);
		}
		else writeLineToOutput("@no");
	}
	
	/**
	 * Implementazione del metodo run dall'interfaccia runnable, avvia il thread.
	 * Fa il parsing dei comandi e chiama le azioni appropriate su logica tramite gli adattatori o direttamente.
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
	 * Legge una linea dal client.
	 * @return Una stringa che contiene la linea letta.
	 * @throws IOException Se ci sono errori di comunicazione.
	 */
	private String readLineFromInput () throws IOException { return incomingData.readLine(); }
	/**
	 * Manda una linea al client.
	 * @param toSend La stringa da mandare al client.
	 * @throws IOException Se ci sono errori di comunicazione.
	 */
	private void writeLineToOutput (String toSend) throws IOException {
		outgoingData.println(toSend);
	}
	/**
	 * Helper per fermare il thread, imposta stopThread a true.
	 */
	private void stopTheThread () { stopThread = true; }
	/** 
	 * Termina il thread su eccezione:
	 * Da' notifica con motivazione e imposta il parametro per lo stop.
	 * @param cause Stringa che contiene la causa per cui il thread e' stato fermato.
	 */
	private void terminateThreadOnIOException (String cause) {
		System.out.println(cause);
		System.out.println("Killing this thread!");
		stopTheThread();
	}
}