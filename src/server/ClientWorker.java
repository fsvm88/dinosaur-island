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
 * Implementa l'ascoltatore per i client.
 * Ascolta i comandi e gestisce le giuste risposte.
 */
class ClientWorker implements Runnable {
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
	 * Istanzia riferimento al socketAdapter.
	 */
	private SocketAdapter socketAdapter = null;
	/**
	 * La variabile che dice se il thread sta funzionando.
	 * Server per disaccoppiare i thread dal server.
	 * @uml.property name="threadIsRunning"
	 */
	private boolean threadIsRunning = false;

	/**
	 * Prende in ingresso il socket per comunicare con l'esterno.
	 * @param socket Il socket attraverso cui passa la comunicazione.
	 * @throws IOException Se ci sono problemi con la comunicazione.
	 */
	protected ClientWorker(Socket socket, SocketAdapter inSocketAdapter) throws IOException {
		mySocket = socket;
		try {
			incomingData = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			outgoingData = new PrintWriter(mySocket.getOutputStream(), true);
			socketAdapter = inSocketAdapter;
		}
		catch (IOException e) {
			terminateThreadOnIOException("[ClientWorker] Cannot initialize input/output streams!");
		}
		threadIsRunning = true;
	}
	/**
	 * Estrae il nome utente dallo scanner.
	 * @param scanner Lo scanner da cui estrarre il nome utente.
	 * @return Una stringa che contiene il nome utente.
	 */
	private String estraiUser(Scanner scanner) {
		String nextScanner = scanner.next();
		if (nextScanner.matches("(^user=)(\\w)+")) {
			return nextScanner.substring(nextScanner.indexOf('=')+1);
		}
		return null;
	}
	/**
	 * Estrae la password dallo scanner.
	 * @param scanner Lo scanner da cui estrarre la password dell'utente.
	 * @return Una stringa che contiene la password dell'utente.
	 */
	private String estraiPwd(Scanner scanner) {
		String nextScanner = scanner.next();
		if (nextScanner.matches("(^pass=)\\w+")) {
			return nextScanner.substring(nextScanner.indexOf('=')+1);
		}
		return null;
	}
	/**
	 * Estra il token dallo scanner.
	 * @param scanner Lo scanner da cui estrarre il token dell'utente.
	 * @return Una stringa che contiene il token dell'utente.
	 */
	private String getToken(Scanner scanner) {
		if (scanner.hasNext()) {
			String nextScanner = scanner.next();
			if (nextScanner.matches("(^token=)\\w+")) {
				return nextScanner.substring(nextScanner.indexOf('=')+1);
			}
			return null;
		}
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
	 * Dice se il thread sta funzionando o no.
	 * @return True se il thread sta funzionando, false altrimenti.
	 */
	private boolean isThreadRunning() { return threadIsRunning; }
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
	public void run() {
		System.out.println("[" + Thread.currentThread().getName() + "] Started ClientWorker.");
		while (isThreadRunning() && (mySocket != null)) {
			try {
				Scanner scanner = new Scanner(readLineFromInput());
				scanner.useDelimiter(",");
				if (scanner.hasNext()) {
					String comando = scanner.next();
					if (isLoginOrCreation(comando) && isThreadRunning() && (mySocket != null)) {
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
					else if (!isLoginOrCreation(comando) && isThreadRunning() && (mySocket != null)) {
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
				scanner.close();
			}
			catch (IOException e) {
				terminateThreadOnIOException("[" + Thread.currentThread().getName() + "] Unable to communicate with the client, stopping thread.");
			}
		}
		System.out.println("[" + Thread.currentThread().getName() + "] Terminated ClientWorker.");
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
	 * Imposta la variabile che ferma il thread a true.
	 * Serve per gestire i thread indipendentement dal server.
	 */
	public void stop() { threadIsRunning = false; }
	
	/** 
	 * Termina il thread su eccezione:
	 * Da' notifica con motivazione e imposta il parametro per lo stop.
	 * @param cause Stringa che contiene la causa per cui il thread e' stato fermato.
	 */
	private void terminateThreadOnIOException (String cause) {
		System.out.println(cause);
		stop();
	}
}