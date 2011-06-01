package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import dinolib.*;

/** 
 * Implementa l'ascoltatore per i client. Ascolta i comandi e gestisce le giuste risposte.
 */
class ClientWorker extends Server implements Runnable {
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
	protected ClientWorker(Socket socket) throws IOException {
		mySocket = socket;
		try {
			incomingData = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			outgoingData = new PrintWriter(mySocket.getOutputStream(), true);
		}
		catch (IOException e) {
			terminateThreadOnIOException("Cannot initialize input/output streams!");
		}
	}

	private String estraiUser(Scanner scanner) {
		return scanner.next();
	}

	private String estraiPwd(Scanner scanner) {
		return scanner.next();
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

	private String estraiRazza(Scanner scanner) {
		return scanner.next();
	}

	private String estraiTipo(Scanner scanner) {
		return scanner.next();
	}

	private boolean validaRazzaETipo(String nomeRazza, String tipo) {
		if ((nomeRazza != null) && (tipo != null) ) return true;
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
					String bufferDaStampare = null;
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
							socketAdaptor.saCreaUtente(user, pwd);
							writeOkToOutput(bufferDaStampare);
						}
						else if (comando.equals("@login")) {
							bufferDaStampare = socketAdaptor.saLoginUtente(user, pwd);
							writeOkToOutput(bufferDaStampare);
						}
						else writeNoToOutput();
					}
					else if (!isLoginOrCreation(comando)) {
						String token = getToken(scanner);
						if (isGoodToken(token)) {
							if (socketAdaptor.saUserIsLogged(token)) {
								/* comandi fuori partita*/
								if (comando.equals("@creaRazza")) {
									String nomeRazza = null;
									String tipoRazza = null;
									if (scanner.hasNext()) {
										nomeRazza = estraiRazza(scanner);
										if (scanner.hasNext()) {
											tipoRazza = estraiTipo(scanner);
										}
										else writeNoToOutput();
									}
									else writeNoToOutput();
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
									bufferDaStampare = socketAdaptor.saListaDeiGiocatori(token);
									writeOkToOutput(bufferDaStampare);
								}
								else if (comando.equals("@classifica")) {
									bufferDaStampare = socketAdaptor.saClassifica(token);
									writeLineToOutput(comando + "," + bufferDaStampare);
								}
								else if (comando.equals("@logout")) {
									socketAdaptor.saLogout(token);
									writeOkToOutput();
								}
								/* comandi in partita */
								/* comandi di informazione */
								else if (comando.equals("@mappaGenerale")) {
									bufferDaStampare = socketAdaptor.saSendMappaGenerale(token);
									writeLineToOutput(comando + "," + bufferDaStampare);
								}
								else if (comando.equals("@listaDinosauri")) {
									bufferDaStampare = socketAdaptor.saSendListaDinosauri(token);
									writeLineToOutput(comando + "," + bufferDaStampare);
								}
								/* comandi di turno */
								else if (comando.equals("@confermaTurno")) {
									socketAdaptor.saConfermaTurno(token);
									writeOkToOutput();
								}
								else if (comando.equals("@passaTurno")) {
									socketAdaptor.saPassaTurno(token);
									writeOkToOutput();
								}
								else if (scanner.hasNext()) {
									String idDinosauro = scanner.next();
									if (comando.equals("@vistaLocale")) {
										bufferDaStampare = socketAdaptor.saVistaLocale(token, idDinosauro);
										writeLineToOutput(comando + "," + bufferDaStampare);
									}
									else if (comando.equals("@statoDinosauro")) {
										bufferDaStampare = socketAdaptor.saStatoDinosauro(token, idDinosauro);
										writeLineToOutput(comando + "," + bufferDaStampare);
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
										if (scanner.hasNextInt()) {
											x = scanner.nextInt();
											if (scanner.hasNextInt()) {
												y = scanner.nextInt();
												bufferDaStampare = socketAdaptor.saMuoviDinosauro(token, idDinosauro, x, y);
											}
											else writeLineToOutput("@no");
										}
										else writeLineToOutput("@no");
									}
								}
							}
						}
					}
				}
			}
			catch (IOException e) {
				terminateThreadOnIOException("Unable to communicate with the client, stopping thread");
			}
			catch (UserAuthenticationFailedException e) {
				try {
					writeNoToOutput("@autenticazioneFallita");
				} catch (IOException e1) {
					terminateThreadOnIOException("Unable to communicate with the client, stopping thread");
				}
			}
			catch (GenericDinosauroException e) {
				try {
					writeNoToOutput(e.getMessage());
				} catch (IOException e1) {
					terminateThreadOnIOException("Unable to communicate with the client, stopping thread");
				}
			}
			catch (InvalidIDException e) {
				try {
					writeNoToOutput("@idNonValido");
				} catch (IOException e1) {
					terminateThreadOnIOException("Unable to communicate with the client, stopping thread");
				}
			}
			catch (InvalidTokenException e) {
				try {
					writeNoToOutput("@tokenNonValido");
				} catch (IOException e1) {
					terminateThreadOnIOException("Unable to communicate with the client, stopping thread");
				}
			}
			catch (NomeRazzaOccupatoException e) {
				try {
					writeNoToOutput("@nomeRazzaOccupato");
				} catch (IOException e1) {
					terminateThreadOnIOException("Unable to communicate with the client, stopping thread");
				}
			}
			catch (NonInPartitaException e) {
				try {
					writeNoToOutput("@nonInPartita");
				} catch (IOException e1) {
					terminateThreadOnIOException("Unable to communicate with the client, stopping thread");
				}
			}
			catch (RazzaGiaCreataException e) {
				try {
					writeNoToOutput("@razzaGiaCreata");
				} catch (IOException e1) {
					terminateThreadOnIOException("Unable to communicate with the client, stopping thread");
				}
			}
			catch (RazzaNonCreataException e) {
				try {
					writeNoToOutput("@razzaNonCreata");
				} catch (IOException e1) {
					terminateThreadOnIOException("Unable to communicate with the client, stopping thread");
				}
			}
			catch (TroppiGiocatoriException e) {
				try {
					writeNoToOutput("@troppiGiocatori");
				} catch (IOException e1) {
					terminateThreadOnIOException("Unable to communicate with the client, stopping thread");
				}
			}
			catch (UserExistsException e) {
				try {
					writeNoToOutput("@usernameOccupato");
				} catch (IOException e1) {
					terminateThreadOnIOException("Unable to communicate with the client, stopping thread");
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