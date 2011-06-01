package dinolib;

import java.io.IOException;
import java.util.Iterator;

public class SocketAdaptor {
	/**
	 * Contiene la classe logica che viene istanziata da server.
	 * @uml.property name="myLogica"
	 */
	private Logica myLogica = null;

	/**
	 * Costruttore pubblico, riceve come unico argomento la classe Logica che deve gestire.
	 * @param newLogica
	 */
	public SocketAdaptor (Logica newLogica) {
		myLogica = newLogica;
	}

	/**
	 * Crea una nuova razza di dinosauri per l'utente.
	 * @throws RazzaGiaCreataException 
	 * @throws NomeRazzaOccupatoException 
	 * @throws InvalidTokenException 
	 */
	public void saCreaRazzaETipo(String token, String nomeRazza, String tipoRazza) throws InvalidTokenException, NomeRazzaOccupatoException, RazzaGiaCreataException {
		if (myLogica.existsPlayerWithToken(token) && !myLogica.existsRaceWithName(nomeRazza)) {
			if (!myLogica.existsRaceForPlayer(token)) {
				Dinosauro tempDinosauro;
				if (tipoRazza.equals("c")) {
					tempDinosauro = new Carnivoro(CommonUtils.getNewRandomIntValueOnMyMap(myLogica.getLatoDellaMappa()), CommonUtils.getNewRandomIntValueOnMyMap(myLogica.getLatoDellaMappa()));
					myLogica.createNewRaceForPlayer(token, nomeRazza, tempDinosauro);
				}
				else if (tipoRazza.equals("e")) {
					tempDinosauro = new Erbivoro(CommonUtils.getNewRandomIntValueOnMyMap(myLogica.getLatoDellaMappa()), CommonUtils.getNewRandomIntValueOnMyMap(myLogica.getLatoDellaMappa()));
					myLogica.createNewRaceForPlayer(token, nomeRazza, tempDinosauro);
				}
			}
		}
	}

	/**
	 * Adattatore per loggare l'utente
	 * @param user
	 * @param pwd
	 * @return 
	 * @throws AuthenticationFailedException 
	 * @throws UserExistsException
	 */
	public String saLoginUtente(String user, String pwd) throws AuthenticationFailedException {
		if (myLogica.existsUserWithName(user)) {
			Giocatore tempGiocatore = myLogica.getPlayerByName(user);
			if (tempGiocatore.passwordIsValid(pwd)) {
				String newToken = CommonUtils.getNewToken();
				myLogica.addPlayerToConnTable(newToken, user);
				tempGiocatore.logged();
				return newToken;
			}
			else throw new AuthenticationFailedException();
		}
		throw new AuthenticationFailedException();
	}

	/**
	 * Ritorna la classifica di gioco.
	 * @return
	 * @throws InvalidTokenException 
	 * @throws NonInPartitaException 
	 */
	public String saClassifica(String token) throws InvalidTokenException {
		if (myLogica.existsPlayerWithToken(token) &&
				saUserIsLogged(token)) {
			String buffer = null;
			Iterator<Giocatore> itGiocatori = myLogica.getIteratorOnPlayers();
			while (itGiocatori.hasNext()) {
				buffer = assemblaPunteggio(buffer, itGiocatori.next());
			}
			return buffer;
		}
		return null;
	}

	/**
	 * Helper per assemblare il punteggio per un singolo giocatore.
	 * @param newBuffer
	 * @param giocatore
	 * @return
	 */
	private String assemblaPunteggio(String newBuffer, Giocatore giocatore) {
		newBuffer = assemblaBuffer(newBuffer, giocatore.getNome());
		newBuffer = assemblaBuffer(newBuffer, giocatore.getNomeRazza());
		newBuffer = newBuffer + "," + giocatore.getPunteggio();
		if (giocatore.isSpecieEstinta()) newBuffer = assemblaBuffer(newBuffer, "s");
		else newBuffer = assemblaBuffer(newBuffer, "n");
		return newBuffer;
	}
	/**
	 * Fa la sottrazione per le coordinate, che rimangano in range.
	 * @param x
	 * @param y
	 * @return
	 */
	private String getCellaDellaMappaPerBuffer(int x, int y) {
		Cella tempCella = myLogica.getCella(x, y);
		Character tipoCella = tempCella.getTipoCella(x, y).toLowerCase().charAt(0);
		if ((tipoCella.charValue() == 't' ) ||
				(tipoCella.charValue() == 'a' )) {
			return "[" + tipoCella.charValue() + "]";
		}
		else if ((tipoCella.charValue() == 'v' ) ||
				(tipoCella.charValue() == 'c')) {
			return "[" + tipoCella.charValue() + "," + tempCella.getValoreAttuale() + "]";
		}
		else if (tipoCella.charValue() == 'd' ) {
			return "[" + tipoCella.charValue() + "," + tempCella.getIdDelDinosauro() + "]";
		}
		return null;
	}

	/**
	 * Restituisce una riga intera della mappa formattata.
	 * @param yRiga
	 * @param fromX
	 * @param toX
	 * @return
	 */
	private String getRigaDellaMappa(int yRiga, int fromX, int toX) {
		String tmpBuf = null;
		int i = fromX;
		if (fromX == toX) {
			return getCellaDellaMappaPerBuffer(fromX, yRiga);
		}
		else {
			while ((i<toX)) {
				tmpBuf = getCellaDellaMappaPerBuffer(i, yRiga) + " ";
				i++;
			}
			tmpBuf = getCellaDellaMappaPerBuffer(toX, yRiga);
			return tmpBuf;
		}
	}

	/**
	 * Invia la vista locale del dinosauro richiesto all'utente.
	 * @param token
	 * @param idDinosauro
	 * @return
	 * @throws InvalidTokenException
	 * @throws NonInPartitaException
	 * @throws InvalidIDException
	 */
	public String saVistaLocale(String token, String idDinosauro) throws InvalidTokenException, NonInPartitaException, InvalidIDException {
		if (myLogica.playerHasDinosauro(token, idDinosauro)) {
			String buffer = null;
			Dinosauro tempDinosauro = myLogica.getPlayerByToken(token).getDinosauro(idDinosauro);
			int rangeVista = tempDinosauro.getRangeVista();
			int leftCornerX = myLogica.doSubtraction(tempDinosauro.getX(), rangeVista);
			int bottomCornerY = myLogica.doSubtraction(tempDinosauro.getY(), rangeVista);
			int rightCornerX = myLogica.doAddition(tempDinosauro.getX(), rangeVista);
			int topCornerY = myLogica.doAddition(tempDinosauro.getY(), rangeVista);
			buffer = "{" + leftCornerX + "," + CommonUtils.translateYforClient(bottomCornerY, myLogica.getLatoDellaMappa()) + "}";
			buffer = buffer + "," + "{" + (topCornerY-bottomCornerY) + "," + (rightCornerX-leftCornerX) + "}" + ",";
			int j = bottomCornerY;
			do {
				buffer = getRigaDellaMappa(j, leftCornerX, rightCornerX) + ";";
				j++;
			} while (j<topCornerY);
			return buffer;
		}
		return null;
	}

	public String saSendMappaGenerale(String token) throws InvalidTokenException, NonInPartitaException {
		if (myLogica.isPlayerInGame(token)) {
			String buffer = null;
			int latoDellaMappa = myLogica.getLatoDellaMappa();
			buffer = "{" + latoDellaMappa + "," + latoDellaMappa + "}" + ",";
			int i = 0;
			while (i<latoDellaMappa) {
				buffer = buffer + getRigaDellaMappa(i, 0, latoDellaMappa) + ";"; // TODO implementare il buio della mappa!! Chiedere esercitatore!
				i++;
			}
			return buffer;
		}
		return null;
	}

	/**
	 * Assembla lo stato comune del dinosauro
	 */
	private String assemblaStatoComuneDinosauro(Giocatore tempGiocatore, Dinosauro tempDinosauro) {
		return tempGiocatore.getNome() + "," +
		tempGiocatore.getNomeRazza() + "," +
		tempGiocatore.getTipoRazza().toLowerCase().charAt(0) + "," +
		"{" + "," + tempDinosauro.getX() + "," + CommonUtils.translateYforClient(tempDinosauro.getY(), myLogica.getLatoDellaMappa()) + "," + "}" + "," +
		tempDinosauro.getDimensione();
	}

	/**
	 * Invia lo stato del dinosauro richiesto all'utente.
	 * @param scanner
	 * @throws InvalidIDException 
	 * @throws InvalidTokenException 
	 * @throws IOException
	 */
	public String saStatoDinosauro(String token, String idDinosauro) throws InvalidTokenException, InvalidIDException {
		if (myLogica.playerHasDinosauro(token, idDinosauro)) {
			Giocatore tempGiocatore = myLogica.getPlayerByToken(token);
			Dinosauro tempDinosauro = tempGiocatore.getDinosauro(idDinosauro);
			String buffer = null;
			buffer = "," + assemblaStatoComuneDinosauro(tempGiocatore, tempDinosauro);
			buffer = buffer + "," +
			tempDinosauro.getEnergiaAttuale() + "," +
			tempDinosauro.getTurnoDiVita();
			return buffer;
		}
		else if (!myLogica.playerHasDinosauro(token, idDinosauro)) {
			Iterator<Giocatore> itSuiGiocatori = myLogica.getIteratorOnPlayers();
			while (itSuiGiocatori.hasNext()) {
				Giocatore tempGiocatore = itSuiGiocatori.next();
				Iterator<String> itSuIdDinosauri = tempGiocatore.getItIdDinosauri();
				while (itSuIdDinosauri.hasNext()) {
					if (itSuIdDinosauri.next().equals(idDinosauro)) {
						Dinosauro tempDinosauro = tempGiocatore.getDinosauro(idDinosauro);
						String buffer = null;
						buffer = "," + assemblaStatoComuneDinosauro(tempGiocatore, tempDinosauro);
						buffer = buffer + "," +
						tempDinosauro.getEnergiaAttuale() + "," +
						tempDinosauro.getTurnoDiVita();
						return buffer;
					}

				}
			}
		}
		else throw new InvalidIDException();
		return null;
	}
	/**
	 * Chiama la funzione di crescita del dinosauro.
	 * @throws NonInPartitaException 
	 * @throws InvalidIDException 
	 * @throws InvalidTokenException 
	 * @throws GenericDinosauroException 
	 */
	public void saCresciDinosauro(String token, String idDinosauro) throws InvalidTokenException, InvalidIDException, NonInPartitaException, GenericDinosauroException {
		if (myLogica.playerHasDinosauro(token, idDinosauro) &&
				myLogica.isPlayerInGame(token) &&
				myLogica.isMioTurno(token)) {
			if (myLogica.puoCrescere(myLogica.getPlayerByToken(token).getDinosauro(idDinosauro))) {
				myLogica.getPlayerByToken(token).getDinosauro(idDinosauro).cresci();
			}
		}
	}

	/**
	 * Gestisce il comando che depone l'uovo.
	 * @param scanner
	 * @throws IOException
	 * @throws GenericDinosauroException 
	 * @throws NonInPartitaException 
	 * @throws InvalidIDException 
	 * @throws InvalidTokenException 
	 */
	public String saDeponiUovo(String token, String idDinosauro) throws InvalidTokenException, InvalidIDException, NonInPartitaException, GenericDinosauroException {
		if (myLogica.playerHasDinosauro(token, idDinosauro) &&
				myLogica.isPlayerInGame(token) &&
				myLogica.isMioTurno(token)) { // TODO aggiungere limite mosse
			Dinosauro curDinosauro = myLogica.getPlayerByToken(token).getDinosauro(idDinosauro);
			int x = curDinosauro.getX();
			int y = curDinosauro.getY();
			if (myLogica.puoDeporreUnUovo(token, curDinosauro)) {
				String newIdDinosauro = CommonUtils.getNewToken();
				if (curDinosauro.getTipoRazza().equals("Carnivoro")) {
					Dinosauro newDinosauro = new Carnivoro(x, y);
					newDinosauro.nonUsabile();
					myLogica.trySpawnOfAnEgg(token, x, y, newDinosauro, newIdDinosauro);
					return newIdDinosauro;
				}
				else if (curDinosauro.getTipoRazza().equals("Erbivoro")) {
					Dinosauro newDinosauro = new Erbivoro(x, y);
					newDinosauro.nonUsabile();
					myLogica.trySpawnOfAnEgg(token, x, y, newDinosauro, newIdDinosauro);
					return newIdDinosauro;
				}
			}
		}
		return null;
	}

	public String saMuoviDinosauro(String token, String idDinosauro, int toX, int toY) throws InvalidTokenException, NonInPartitaException, InvalidIDException {
		if (myLogica.isPlayerInGame(token) &&
				myLogica.playerHasDinosauro(token, idDinosauro)) {

		}
		return null;
	}

	/**
	 * Metodo per gestire il logout dell'utente
	 * @throws InvalidTokenException 
	 * @throws NonInPartitaException 
	 */
	public void saLogout(String token) throws InvalidTokenException, NonInPartitaException {
		myLogica.doLogout(token);
	}

	/**
	 * Spedisce la lista dei dinosauri all'utente.
	 * @throws RazzaGiaCreataException 
	 * @throws InvalidTokenException 
	 * @throws NonInPartitaException 
	 * @throws IOException
	 */
	public String saSendListaDinosauri(String token) throws InvalidTokenException, RazzaGiaCreataException, NonInPartitaException {
		if (myLogica.isPlayerInGame(token)) {
			String buffer = null;
			if (myLogica.existsRaceForPlayer(token)) {
				Iterator<String> itIdDinosauri = myLogica.getPlayerByToken(token).getItIdDinosauri();
				while (itIdDinosauri.hasNext()) {
					buffer = assemblaBuffer(buffer, itIdDinosauri.next());
				}
				return buffer;
			}
			return buffer;
		}
		return null;
	}
	/**
	 * Adattatore per l'uscita dalla partita.
	 * @param token
	 * @throws NonInPartitaException 
	 * @throws InvalidTokenException
	 */
	public void saEsciDallaPartita(String token) throws InvalidTokenException, NonInPartitaException {
		if (myLogica.isPlayerInGame(token)) myLogica.doEsciDallaPartita(token);
	}

	/**
	 * Helper per le funzioni che restituiscono liste
	 * @param buffer
	 * @param toAppend
	 */
	private String assemblaBuffer(String buffer, String toAppend) {
		if (buffer != null) 
			buffer = buffer + "," + toAppend;
		else 
			buffer = toAppend;
		return buffer;
	}
	/**
	 * Adattatore del comando per restituire la lista dei giocatori al client.
	 * @throws InvalidTokenException 
	 * @throws IOException 
	 */
	public String saListaDeiGiocatori(String token) throws InvalidTokenException {
		if (myLogica.existsPlayerWithToken(token)) {
			Iterator<String> itNomiGiocatori = myLogica.getIteratorOnPNames();
			String buffer = null;
			while (itNomiGiocatori.hasNext()) {
				String curNomeGiocatore = itNomiGiocatori.next();
				if (myLogica.getPlayerByName(curNomeGiocatore).isLogged() &&
						myLogica.getPlayerByName(curNomeGiocatore).isInGame()) {
					buffer = assemblaBuffer(buffer, curNomeGiocatore);
				}
			}
			return buffer;
		}
		return null;
	}
	/**
	 * Implementa l'accesso alla partita.
	 * @throws InvalidTokenException 
	 * @throws RazzaNonCreataException 
	 * @throws TroppiGiocatoriException 
	 * @throws NonInPartitaException 
	 */
	public void saAccediAPartita(String token) throws InvalidTokenException, NonInPartitaException, TroppiGiocatoriException, RazzaNonCreataException {
		Giocatore tempGiocatore = myLogica.getPlayerByToken(token);
		if (myLogica.isPlayerInGame(token) &&
				!myLogica.isMaxPlayersInGame()) {
			if (tempGiocatore.hasRazza()) {
				if (!myLogica.isSomeonePlaying()) myLogica.someoneIsPlaying();
				myLogica.inserisciDinosauriNellaMappa(token);
			}
			else throw new RazzaNonCreataException();
		}
		else return;
	}

	/**
	 * Adattatore per creare l'utente
	 * @param user
	 * @param pwd
	 * @throws UserExistsException
	 */
	public void saCreaUtente(String user, String pwd) throws UserExistsException {
		if (!myLogica.existsUserWithName(user)) {
			myLogica.doCreaUtente(user, pwd);
			return;
		}
	}

	public void saConfermaTurno(String token) {

	}
	public void saPassaTurno(String token) {

	}
	/**
	 * Helper per verificare che l'utente sia effettivamente loggato.
	 * @return 
	 * @throws InvalidTokenException 
	 */
	public boolean saUserIsLogged(String token) throws InvalidTokenException {
		if (myLogica.existsPlayerWithToken(token)) {
			if (myLogica.getPlayerByToken(token).isLogged()) return true;
			else return false;
		}
		else throw new InvalidTokenException();
	}
}
