package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer.Token;

import dinolib.Carnivoro;
import dinolib.Dinosauro;
import dinolib.Erbivoro;
import dinolib.Giocatore;
import dinolib.CommonUtils;
import dinolib.Logica;

/** 
 * Implementa l'ascoltatore per i client. Ascolta i comandi e gestisce le giuste risposte.
 */
public class ClientWorker extends Server implements Runnable {
	/**
	 * Variabile per fermare il thread.
	 * @uml.property name="stopThread"
	 */
	private boolean stopThread = false;
	/**
	 * Istanzia riferimento al socket.
	 * @uml.property name="mySocket"
	 */
	private Socket mySocket = null;
	/**
	 * Istanzia riferimento al buffer per la ricezione dei dati.
	 * @uml.property name="BufferedReader"
	 */
	private BufferedReader incomingData = null;
	/**
	 * Istanzia riferimento al buffer per l'invio dei dati.
	 * @uml.property name="outgoingData"
	 */
	private PrintWriter outgoingData = null;

	/**
	 * Costruttore pubblico e unico costruttore definito per ClientWorker.
	 * @param socket
	 * @throws IOException
	 */
	public ClientWorker(Socket socket) throws IOException {
		mySocket = socket;
		try {
			incomingData = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			outgoingData = new PrintWriter(mySocket.getOutputStream(), true);
		}
		catch (IOException e) {
			terminateThreadOnIOException("Cannot initialize input/output streams!");
		}
	}

	private void estraiUserPwd(Scanner scanner, String utenteDaParse, String passwordDaParse) {
		if (scanner.hasNext()) {
			utenteDaParse = scanner.next();
			if (scanner.hasNext()) {
				passwordDaParse = scanner.next();
			}
		}
	}

	private String getToken(Scanner scanner) {
		if (scanner.hasNext()) {
			return scanner.next();
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

	private void estraiRazzaETipo(Scanner scanner, String nomeRazza, String tipo) {
		if (scanner.hasNext()) {
			nomeRazza = scanner.next();
			if (scanner.hasNext()) {
				tipo = scanner.next();
			} 
		}
	}

	private boolean validaRazzaETipo(String nomeRazza, String tipo) {
		if ((nomeRazza != null) && (tipo != null) ) return true;
		else return false;
	}

	private boolean estraiXeY(Scanner scanner, int x, int y) {
		if (scanner.hasNext()) {
			x = scanner.nextInt();
			if (scanner.hasNext()) {
				y = scanner.nextInt();
				return true;
			}
			return false;
		}
		else return false;
	}

	/**
	 * Invia ok al client, se viene aggiunta una stringa come argomento la aggiunge.
	 * @param toAppend
	 * @throws IOException 
	 */
	private void writeOkToOutput(String...toAppend) throws IOException {
		if (toAppend != null) {
			writeLineToOutput("@ok" + "," + toAppend[0]);
		}
		else writeLineToOutput("@ok");
	}

	/**
	 * Implementazione del metodo run dall'interfaccia runnable, avvia il thread.
	 * Fa il parsing dei comandi e chiama le azioni appropriate su logica tramite gli adattatori (a<NomeFunzione>) o direttamente.
	 */
	public void run () {
		Scanner scanner = new Scanner(readLineFromInput());
		scanner.useDelimiter(",");
		if (scanner.hasNext()) {
			String comando = scanner.next();
			String bufferDaStampare = null;
			if (isLoginOrCreation(comando)) {
				String user = null;
				String pwd = null;
				estraiUserPwd(scanner, user, pwd);
				if (comando.equals("@creaUtente")) {
					socketAdaptor.saCreaUtente(user, pwd);
					writeOkToOutput();
				}
				if (comando.equals("@login")) {
					socketAdaptor.saLoginUtente(user, pwd);
					writeOkToOutput(socketAdaptor.saGetTokenUtente(user));
				}
			}
			else if (!isLoginOrCreation(comando)) {
				String token = null;
				getToken(scanner);
				if (isGoodToken(token)) {
					if (socketAdaptor.saUserIsLogged(token)) {
						/* comandi fuori partita*/
						if (comando.equals("@creaRazza")) {
							String nomeRazza = null;
							String tipoRazza = null;
							estraiRazzaETipo(scanner, nomeRazza, tipoRazza);
							if (validaRazzaETipo(nomeRazza, tipoRazza)) {
								socketAdaptor.saCreaRazzaETipo(token, nomeRazza, tipoRazza);
								writeOkToOutput();
							}
						}
						else if (comando.equals("@accessoPartita")) {
							socketAdaptor.saAccediAPartita(token);
							writeOkToOutput();
						}
						else if (comando.equals("@uscitaPartita")) {
							socketAdaptor.saEsciDallaPartita(token);
							writeOkToOutput();
						}
						else if (comando.equals("@listaGiocatori")) {
							bufferDaStampare = socketAdaptr.saListaDeiGiocatori(token);
							writeOkToOutput(bufferDaStampare);
						}
						else if (comando.equals("@classifica")) {
							bufferDaStampare = socketAdaptor.saClassifica(token);
							writeLineToOutput("@classifica" + "," + bufferDaStampare);
						}
						else if (comando.equals("@logout")) {
							socketAdaptor.saLogout(token);
							writeOkToOutput();
						}
						/* comandi in partita */
						/* comandi di informazione */
						else if (comando.equals("@mappaGenerale")) {
							bufferDaStampare = socketAdaptor.saSendMappaGenerale(token);
							writeLineToOutput("@mappaGenerale" + "," + bufferDaStampare);
						}
						else if (comando.equals("@listaDinosauri")) {
							bufferDaStampare = socketAdaptor.saSendListaDinosauri(token);
							writeLineToOutput("@listaDinosauri" + "," + bufferDaStampare);
						}
						/* comandi di turno */
						else if (comando.equals("@confermaTurno")) bufferDaStampare = socketAdaptor.saConfermaTurno(token);
						else if (comando.equals("@passaTurno")) bufferDaStampare = socketAdaptor.saPassaTurno(token);
						else if (scanner.hasNext()) {
							String idDinosauro = scanner.next();
							if (comando.equals("@vistaLocale")) {
								bufferDaStampare = socketAdaptor.saVistaLocale(token, idDinosauro);
								writeLineToOutput("@vistaLocale" + "," + bufferDaStampare);
							}
							else if (comando.equals("@statoDinosauro")) {
								bufferDaStampare = socketAdaptor.saStatoDinosauro(token, idDinosauro);
								writeLineToOutput("@statoDinosauro" + "," + bufferDaStampare);
							}
							else if (comando.equals("@cresciDinosauro")) {
								socketAdaptor.saCresciDinosauro(token, idDinosauro);
								writeOkToOutput();
							}
							else if (comando.equals("@deponiUovo")) {
								bufferDaStampare = socketAdaptor.saDeponiUovo(token, idDinosauro);
								writeOkToOutput(bufferDaStampare);
							}
							else if (comando.equals("@muoviDinosauro")) {
								int x = 0;
								int y = 0;
								if (estraiXeY(scanner, x, y)) {
									bufferDaStampare = socketAdaptor.saMuoviDinosauro(token, idDinosauro, x, y);
								}
								else writeLineToOutput("@no");
							}
						}
					}
				}
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