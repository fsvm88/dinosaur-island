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
	 * Ritorna la classifica di gioco.
	 * @return
	 * @throws InvalidTokenException 
	 * @throws NonInPartitaException 
	 */
	public String aClassifica(String token) throws InvalidTokenException {
		if (myLogica.existsPlayerWithToken(token) &&
				aUserIsLogged(token)) {
			String buffer = null;
			Iterator<Giocatore> itGiocatori = myLogica.getIteratorOnPlayers();
			while (itGiocatori.hasNext()) {
				assemblaPunteggio(buffer, itGiocatori.next());
			}
			return buffer;
		}
		return null;
	}
	
	/**
	 * Helper per assemblare il punteggio per un singolo giocatore.
	 * @param buffer
	 * @param giocatore
	 * @return
	 */
	private void assemblaPunteggio(String buffer, Giocatore giocatore) {
		assemblaBuffer(buffer, giocatore.getNome());
		assemblaBuffer(buffer, giocatore.getNomeRazzaDinosauri());
		buffer = buffer + "," + giocatore.getPunteggio();
		if (giocatore.isSpecieEstinta()) assemblaBuffer(buffer, "s");
		else assemblaBuffer(buffer, "n");
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
		if ((tipoCella.equals("t")) ||
				(tipoCella.equals("a"))) {
			return "[" + tipoCella.charValue() + "]";
		}
		else if ((tipoCella.equals("v")) ||
				tipoCella.equals("c")) {
			return "[" + tipoCella.charValue() + "," + tempCella.getValoreAttuale() + "]";
		}
		else if ((tipoCella.equals("d"))) {
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
	public String aVistaLocale(String token, String idDinosauro) throws InvalidTokenException, NonInPartitaException, InvalidIDException {
		if (myLogica.existsPlayerWithToken(token) &&
				myLogica.isPlayerInGame(token) &&
				myLogica.playerHasDinosauro(token, idDinosauro)) {
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

	public String aSendMappaGenerale(String token) throws InvalidTokenException, NonInPartitaException {
		if (myLogica.existsPlayerWithToken(token) &&
				myLogica.isPlayerInGame(token)) {
			String buffer = null;
			int latoDellaMappa = myLogica.getLatoDellaMappa();
			buffer = "{" + latoDellaMappa + "," + latoDellaMappa + "}" + ",";
			int i = 0;
			while (i<latoDellaMappa) {
				buffer = buffer + getRigaDellaMappa(i, 0, latoDellaMappa) + ";"; // TODO implementare il buio della mappa!! Chiedere esercitatore!
				i++;
			}
		}
		return null;
	}
	
	/**
	 * Assembla lo stato comune del dinosauro
	 */
	private void assemblaStatoComuneDinosauro(String buffer, Giocatore tempGiocatore, Dinosauro tempDinosauro) {
		buffer = tempGiocatore.getNome() + "," +
		tempGiocatore.getNomeRazzaDinosauri() + "," +
		tempGiocatore.getTipoRazza().toLowerCase().charAt(0) + "," +
		"{" + "," + tempDinosauro.getX() + "," + CommonUtils.translateYforClient(tempDinosauro.getY(), myLogica.getLatoDellaMappa()) + "," + "}" + "," +
		tempDinosauro.getDimensione();
	}

	/**
	 * Invia lo stato del dinosauro richiesto all'utente.
	 * @param scanner
	 * @throws IOException
	 */
	public String aStatoDinosauro(String token, String idDinosauro) throws InvalidTokenException, InvalidIDException, NonInPartitaException {
		if (myLogica.existsPlayerWithToken(token) && myLogica.playerHasDinosauro(token, idDinosauro)) {
			Giocatore tempGiocatore = myLogica.getPlayerByToken(token);
			Dinosauro tempDinosauro = tempGiocatore.getDinosauro(idDinosauro);
			String buffer = null;
			assemblaStatoComuneDinosauro(buffer, tempGiocatore, tempDinosauro);
			buffer = buffer + "," +
			tempDinosauro.getEnergiaAttuale() + "," +
			tempDinosauro.getTurnoDiVita();
			return buffer;
		}
		else if (myLogica.existsPlayerWithToken(token) && !myLogica.playerHasDinosauro(token, idDinosauro)) {
			Iterator<Giocatore> itSuiGiocatori = myLogica.getIteratorOnPlayers();
			while (itSuiGiocatori.hasNext()) {
				Giocatore tempGiocatore = itSuiGiocatori.next();
				Iterator<String> itSuIdDinosauri = tempGiocatore.getItIdDinosauri();
				while (itSuIdDinosauri.hasNext()) {
					if (itSuIdDinosauri.next() == idDinosauro) {
						Dinosauro tempDinosauro = tempGiocatore.getDinosauro(idDinosauro);
						String buffer = null;
						assemblaStatoComuneDinosauro(buffer, tempGiocatore, tempDinosauro);
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
	 */
	public void aCresciDinosauro(String token, String idDinosauro) throws InvalidTokenException, InvalidIDException, GenericDinosauroException, NonInPartitaException, NonIlTuoTurnoException {
		if (myLogica.existsPlayerWithToken(token) &&
				myLogica.playerHasDinosauro(token, idDinosauro) &&
				myLogica.isPlayerInGame(token) &&
				myLogica.isMioTurno(token)) {
			if (puoCrescere(myLogica.getPlayerByToken(token).getDinosauro(idDinosauro))) {
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
	public String aDeponiUovo(String token, String idDinosauro) throws IOException, InvalidTokenException, InvalidIDException, NonInPartitaException, GenericDinosauroException {
		if (myLogica.existsPlayerWithToken(token) &&
				myLogica.playerHasDinosauro(token, idDinosauro) &&
				myLogica.isPlayerInGame(token) &&
				myLogica.isMioTurno(token)) { // TODO aggiungere limite mosse
			Dinosauro curDinosauro = myLogica.getPlayerByToken(token).getDinosauro(idDinosauro);
			int x = curDinosauro.getX();
			int y = curDinosauro.getY();
			if (puoDeporreUnUovo(token, curDinosauro)) {
				String newIdDinosauro = CommonUtils.getNewToken();
				if (curDinosauro.getTipoRazza().equals("Carnivoro")) {
					Dinosauro newDinosauro = new Carnivoro(x, y);
					newDinosauro.nonSonoUsabile();
					myLogica.trySpawnOfAnEgg(token, x, y, newDinosauro, newIdDinosauro);
					return newIdDinosauro;
				}
				else if (curDinosauro.getTipoRazza().equals("Erbivoro")) {
					Dinosauro newDinosauro = new Erbivoro(x, y);
					newDinosauro.nonSonoUsabile();
					myLogica.trySpawnOfAnEgg(token, x, y, newDinosauro, newIdDinosauro);
					return newIdDinosauro;
				}
			}
		}
		return null;
	}
	
	public String aMuoviDinosauro(String token, String idDinosauro, int toX, int toY) throws InvalidTokenException, NonInPartitaException, InvalidIDException {
		if (myLogica.existsPlayerWithToken(token) &&
				myLogica.isPlayerInGame(token) &&
				myLogica.playerHasDinosauro(token, idDinosauro)) {
			
		}
		return null;
	}

	/**
	 * Verifica se il dinosauro ha abbastanza energia per deporre un uovo, altrimenti lancia eccezione con causa.
	 * @throws GenericDinosauroException 
	 */
	private boolean haAbbastanzaEnergiaPerDeporreUnUovo(Dinosauro dinosauro) throws GenericDinosauroException {
		if (dinosauro.haAbbastanzaEnergiaPerDeporreUnUovo()) return true;
		else throw new GenericDinosauroException("mortePerInedia");
	}

	/**
	 * Verifica se la specie ha già 5 dinosauri. Se la specie ha dimensione massima lancia eccezione con causa.
	 * @throws GenericDinosauroException 
	 * @throws InvalidTokenException 
	 */
	private boolean haNumeroMassimoPerSpecie(String token) throws GenericDinosauroException, InvalidTokenException {
		if (myLogica.getPlayerByToken(token).specieHaNumeroMassimo()) return true;
		else throw new GenericDinosauroException("raggiuntoNumeroMaxDinosauri");
	}

	/**
	 * Verifica se il dinosauro può deporre un uovo.
	 * @throws GenericDinosauroException 
	 * @throws InvalidTokenException 
	 */
	private boolean puoDeporreUnUovo(String token, Dinosauro dinosauro) throws GenericDinosauroException, InvalidTokenException {
		if (haAbbastanzaEnergiaPerDeporreUnUovo(dinosauro) &&
				!haNumeroMassimoPerSpecie(token)) return true;
		else return false;
	}

	/**
	 * Verifica se il dinosauro ha abbastanza energia per crescere, altrimenti lancia eccezione con causa.
	 * @throws GenericDinosauroException 
	 */
	private boolean haAbbastanzaEnergiaPerCrescere(Dinosauro dinosauro) throws GenericDinosauroException {
		if (dinosauro.haAbbastanzaEnergiaPerCrescere()) return true;
		else throw new GenericDinosauroException("mortePerInedia");
	}

	/**
	 * Verifica se la specie ha già 5 dinosauri. Se la specie ha dimensione massima lancia eccezione con causa.
	 * @throws GenericDinosauroException 
	 * @throws InvalidTokenException 
	 */
	private boolean haDimensioneMassima(Dinosauro dinosauro) throws GenericDinosauroException {
		if (dinosauro.isAtDimensioneMax()) return true;
		else throw new GenericDinosauroException("raggiuntoNumeroMaxDinosauri");
	}

	/**
	 * Verifica se il dinosauro può crescere.
	 * @throws GenericDinosauroException 
	 */
	private boolean puoCrescere(Dinosauro dinosauro) throws GenericDinosauroException {
		if (haAbbastanzaEnergiaPerCrescere(dinosauro) &&
				!haDimensioneMassima(dinosauro)) return true;
		else return false;
	}
	
	/**
	 * Metodo per gestire il logout dell'utente
	 * @throws NonInPartitaException 
	 */
	public void aLogout(String token) throws InvalidTokenException, NonInPartitaException {
		if (myLogica.existsPlayerWithToken(token)) {
			myLogica.doLogout(token);
		}
	}

	/**
	 * Spedisce la lista dei dinosauri all'utente.
	 * @throws IOException
	 */
	public String aSendListaDinosauri(String token) throws InvalidTokenException, NonInPartitaException {
		if (myLogica.existsPlayerWithToken(token) && myLogica.isPlayerInGame(token)) {
			String buffer = null;
			if (myLogica.getPlayerByToken(token).hasRazza()) {
				Iterator<String> itIdDinosauri = myLogica.getPlayerByToken(token).getItIdDinosauri();
				while (itIdDinosauri.hasNext()) {
					assemblaBuffer(buffer, itIdDinosauri.next());
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
	 * @throws InvalidTokenException
	 */
	public void aEsciDallaPartita(String token) throws InvalidTokenException {
		if (myLogica.existsPlayerWithToken(token)) myLogica.doEsciDallaPartita(token);
	}

	/**
	 * Helper per le funzioni che restituiscono liste
	 * @param buffer
	 * @param toAppend
	 */
	private void assemblaBuffer(String buffer, String toAppend) {
		if (buffer != null) 
			buffer = buffer + "," + toAppend;
		else 
			buffer = toAppend;
	}
	/**
	 * Adattatore del comando per restituire la lista dei giocatori al client.
	 * @throws IOException 
	 */
	public String aListaDeiGiocatori(String token) throws InvalidTokenException {
		if (myLogica.existsPlayerWithToken(token)) {
			Iterator<String> itNomiGiocatori = myLogica.getIteratorOnPNames();
			String buffer = null;
			while (itNomiGiocatori.hasNext()) {
				String curNomeGiocatore = itNomiGiocatori.next();
				if (myLogica.getPlayerByName(curNomeGiocatore).isLogged() &&
						myLogica.getPlayerByName(curNomeGiocatore).isInGame()) {
					assemblaBuffer(buffer, curNomeGiocatore);
				}
			}
			return buffer;
		}
		return null;
	}
	/**
	 * Implementa l'accesso alla partita.
	 * @throws NonInPartitaException 
	 */
	public void aAccediAPartita(String token) throws TroppiGiocatoriException, RazzaNonCreataException, InvalidTokenException, NonInPartitaException {
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
	 * Crea una nuova razza di dinosauri per l'utente.
	 */
	public void aCreaRazzaETipo(String token, String nomeRazza, String tipoRazza) throws RaceAlreadyCreatedException, RaceNameExistsException, InvalidTokenException {
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
	 * Helper per restituire il token dell'utente richiesto.
	 * @param user
	 * @return
	 * @throws InvalidTokenException 
	 */
	public String aGetTokenUtente(String user) throws InvalidTokenException {
		if (myLogica.isPlayerConnected(user)) {
			return myLogica.getPlayerToken(user);
		}
		throw new InvalidTokenException();
	}
	
	/**
	 * Adattatore per loggare l'utente
	 * @param user
	 * @param pwd
	 * @throws UserExistsException
	 */
	public void aLoginUtente(String user, String pwd) throws AuthenticationFailedException, UserExistsException {
		if (myLogica.existsUserWithName(user)) {
			Giocatore tempGiocatore = myLogica.getPlayerByName(user);
			if (tempGiocatore.passwordIsValid(pwd)) {
				myLogica.addPlayerToConnTable(CommonUtils.getNewToken(), user);
				tempGiocatore.iAmLogged();
				return;
			}
		}
		throw new AuthenticationFailedException();
	}
	
	/**
	 * Adattatore per creare l'utente
	 * @param user
	 * @param pwd
	 * @throws UserExistsException
	 */
	public void aCreaUtente(String user, String pwd) throws UserExistsException {
		if (!myLogica.existsUserWithName(user)) {
			myLogica.doCreaUtente(user, pwd);
			return;
		}
	}
	
	public void aConfermaTurno(String token) {
		
	}
	public void aPassaTurno(String token) {
		
	}
	/**
	 * Helper per verificare che l'utente sia effettivamente loggato.
	 * @return 
	 * @throws InvalidTokenException 
	 */
	public boolean aUserIsLogged(String token) throws InvalidTokenException {
		if (myLogica.existsPlayerWithToken(token)) {
			if (myLogica.getPlayerByToken(token).isLogged()) return true;
			else return false;
		}
		else throw new InvalidTokenException();
	}
}
