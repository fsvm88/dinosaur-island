package dinolib;

import java.io.IOException;
import java.util.Iterator;

import dinolib.Exceptions.*;
import dinolib.Mappa.Cella;
import dinolib.Razza.Dinosauro;

public class SocketAdapter implements Adapter {
	/**
	 * Contiene la classe logica che viene istanziata da server.
	 * @uml.property  name="myLogica"
	 * @uml.associationEnd  
	 */
	private Logica myLogica = null;

	/**
	 * Costruttore pubblico, riceve come unico argomento la classe Logica che deve gestire.
	 * @param newLogica
	 */
	public SocketAdapter (Logica newLogica) {
		myLogica = newLogica;
	}

	private String returnInvalidToken() {
		return "@no,@tokenNonValido";
	}

	private String assemblaTuplaDelPunteggio(String myBuffer, String curNomeRazza, int myPunteggio, String isEstinta) {
		return myBuffer +
		"," + "{" + "," +
		curNomeRazza + "," + 
		myPunteggio + "," +
		isEstinta + "}";
	}

	/**
	 * Helper per assemblare il punteggio per un singolo giocatore.
	 * @param newBuffer
	 * @param giocatore
	 * @return
	 */
	private String assemblaPunteggio(String newBuffer, Giocatore giocatore) {
		newBuffer = assemblaBuffer(newBuffer, giocatore.getNome());
		String curPlayingRazza = null;
		if (giocatore.hasRazza()) {
			curPlayingRazza = giocatore.getRazza().getNome();
		}
		Iterator<String> itRazze = giocatore.getPunteggio().getIteratorOnRaces();
		String curRazza = null;
		while (itRazze.hasNext()) {
			curRazza = itRazze.next();
			if (curPlayingRazza != null) {
				if ((curRazza.equals(curPlayingRazza)))
					newBuffer = assemblaTuplaDelPunteggio(newBuffer, curRazza, giocatore.getPunteggio().getPunteggioDaNome(curRazza), "s");
				else
					newBuffer = assemblaTuplaDelPunteggio(newBuffer, curRazza, giocatore.getPunteggio().getPunteggioDaNome(curRazza), "n");
			}
			else
				newBuffer = assemblaTuplaDelPunteggio(newBuffer, curRazza, giocatore.getPunteggio().getPunteggioDaNome(curRazza), "n");
		}
		return newBuffer;
	}
	
	/**
	 * Assembla tutta la mappa generale in un unico buffer tramite l'iteratore sulla mappa.
	 * @param mioBuffer
	 * @return
	 */
	private String assemblaMappaGenerale(String mioBuffer) {
		Iterator<Cella> itCelle = myLogica.getMappa().iterator();
		int i = 0;
		Cella tempCella = null;
		while (itCelle.hasNext()) {
			do {
				tempCella = itCelle.next();
				mioBuffer = mioBuffer + "[" + tempCella.toString().toLowerCase().charAt(0) + "]";
				i++;
			} while (i < myLogica.getMappa().getLatoDellaMappa());
			mioBuffer = mioBuffer + ";";
			i = 0;
		}
		return mioBuffer;
	}
	
	private String getCellaDellaMappaPerBuffer(int x, int y) {
		Cella tempCella = myLogica.getMappa().getCella(x, y);
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
	 * Assembla lo stato comune del dinosauro
	 */
	private String assemblaStatoComuneDinosauro(Giocatore tempGiocatore, Dinosauro tempDinosauro) {
		return tempGiocatore.getNome() + "," +
		tempGiocatore.getRazza().getNome() + "," +
		tempGiocatore.getRazza().getTipo().toLowerCase().charAt(0) + "," +
		"{" + "," + tempDinosauro.getX() + "," + CommonUtils.translateYforClient(tempDinosauro.getY(), myLogica.getMappa().getLatoDellaMappa()) + "," + "}" + "," +
		tempDinosauro.getDimensione();
	}
	/**
	 * Helper per le funzioni che restituiscono liste
	 * @param localBuffer
	 * @param toAppend
	 */
	private String assemblaBuffer(String localBuffer, String toAppend) {
		if (localBuffer != null)
			localBuffer = localBuffer + "," + toAppend;
		else 
			localBuffer = toAppend;
		return localBuffer;
	}

	@Override
	public Object creaUtente(String nomeUtente, String passwordUtente) {
		try {
			if (myLogica.doCreaUtente(nomeUtente, passwordUtente)) return "@ok";
			else return "@no";
		}
		catch (UserExistsException e) {
			return "@no,@usernameOccupato";
		}
	}

	@Override
	public Object loginUtente(String nomeUtente, String passwordUtente) {
		try {
			if (myLogica.doLogin(nomeUtente, passwordUtente)) {
				return "@ok," + myLogica.getCMan().getToken(nomeUtente);
			}
			else return "@no";
		}
		catch (UserAuthenticationFailedException e) {
			return "@no,@autenticazioneFallita";
		}
	}

	@Override
	public Object creaRazza(String token, String nomeRazza, String tipo) {
		try {
			if (myLogica.isPlayerLogged(token)) {
				if (myLogica.doCreaRazza(token, nomeRazza, tipo)) return "@ok";
				else return "@no,@razzaGiaCreata";
			}
			else return returnInvalidToken();
		}
		catch (NomeRazzaOccupatoException e) {
			return "@no,@nomeRazzaOccupato";
		}
		catch (InvalidTokenException e) {
			return returnInvalidToken();
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		}
	}

	@Override
	public Object accessoPartita(String token) {
		try {
			if (myLogica.doAccessoPartita(token)) return "@ok";
			else return "@no";
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		} catch (InterruptedException e) {
			return "@no";
		} catch (TroppiGiocatoriException e) {
			return "@no,@troppiGiocatori";
		} catch (RazzaNonCreataException e) {
			return "@no,@razzaNonCreata";
		}
	}

	@Override
	public Object uscitaPartita(String token) {
		try {
			if (myLogica.doUscitaPartita(token)) return "@ok";
			else return "@no";
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		}
	}

	@Override
	public Object listaGiocatori(String token) {
		try {
			if (myLogica.getRRSched().hasQueuedTasks()) {
				Iterator<String> itTasks = myLogica.getRRSched().getIteratorOnTasks();
				String buffer = "@listaGiocatori";
				while (itTasks.hasNext()) {
					buffer = assemblaBuffer(buffer, myLogica.getCMan().getName(token));
				}
				return buffer;
			}
			else return "@listaGiocatori";
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		}
	}

	@Override
	public Object classifica(String token) {
		try {
			if (myLogica.isPlayerLogged(token)) {
				String buffer = "@classifica";
				Iterator<Giocatore> itGiocatori = myLogica.getPMan().getIteratorOnPlayers();
				while (itGiocatori.hasNext()) {
					buffer = assemblaPunteggio(buffer, itGiocatori.next());
				}
				return buffer;
			}
			else return returnInvalidToken();
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		}
	}

	@Override
	public Object logoutUtente(String token) {
		try {
			if (myLogica.doLogout(token)) return "@ok";
			else return "@no";
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		}
	}

	@Override
	public Object mappaGenerale(String token) {
		try {
			if (myLogica.isPlayerInGame(token)) {
				String buffer = "@mappaGenerale" + ",{" + myLogica.getMappa().getLatoDellaMappa() + "," + myLogica.getMappa().getLatoDellaMappa() + "},";
				buffer = assemblaMappaGenerale(buffer);
				return buffer;
			}
			else return "@no,@nonInPartita"; 
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		}
	}

	@Override
	public Object listaDinosauri(String token) {
		if (myLogica.isPlayerInGame(token)) {
			String buffer = null;
			if (myLogica.existsRaceForPlayer(token)) {
				Iterator<Dinosauro> itDinosauri = myLogica.getPlayerByToken(token).getRazza().iterator();
				while (itDinosauri.hasNext()) {
					buffer = assemblaBuffer(buffer, itDinosauri.next().getIdDinosauro());
				}
				return buffer;
			}
			return buffer;
		}
		else throw new NonInPartitaException();
	}

	@Override
	public Object vistaLocale(String token, String idDinosauro) {
		if (myLogica.playerHasDinosauro(token, idDinosauro)) {
			String buffer = null;
			Dinosauro tempDinosauro = myLogica.getPlayerByToken(token).getRazza().getDinosauroById(idDinosauro);
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

	@Override
	public Object statoDinosauro(String token, String idDinosauro) {
		if (myLogica.playerHasDinosauro(token, idDinosauro)) {
			Giocatore tempGiocatore = myLogica.getPlayerByToken(token);
			Dinosauro tempDinosauro = tempGiocatore.getRazza().getDinosauroById(idDinosauro);
			String buffer = null;
			buffer = "," + assemblaStatoComuneDinosauro(tempGiocatore, tempDinosauro);
			buffer = buffer + "," +
			tempDinosauro.getEnergiaAttuale() + "," +
			tempDinosauro.getTurnoDiVita();
			return buffer;
		}
		else if (!myLogica.playerHasDinosauro(token, idDinosauro)) {
			Iterator<Giocatore> itSuiGiocatori = myLogica.getIteratorOnPlayers();
			Giocatore tempGiocatore;
			Dinosauro tempDinosauro;
			while (itSuiGiocatori.hasNext()) {
				tempGiocatore = itSuiGiocatori.next();
				Iterator<Dinosauro> itDinosauri = tempGiocatore.getRazza().iterator();
				while (itDinosauri.hasNext()) {
					if (itDinosauri.next().getIdDinosauro().equals(idDinosauro)) {
						tempDinosauro = tempGiocatore.getRazza().getDinosauroById(idDinosauro);
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

	@Override
	public Object muoviDinosauro(String token, String idDinosauro, int x, int y) {
		if (myLogica.isMioTurno(token) &&
				myLogica.playerHasDinosauro(token, idDinosauro)) {

		}
		return null;
	}

	@Override
	public Object cresciDinosauro(String token, String idDinosauro) {
		if (myLogica.playerHasDinosauro(token, idDinosauro) &&
				myLogica.isMioTurno(token)) {
			myLogica.getPlayerByToken(token).getRazza().cresciDinosauro(idDinosauro);
		}
	}

	@Override
	public Object deponiUovo(String token, String idDinosauro) {
		if (myLogica.playerHasDinosauro(token, idDinosauro) &&
				myLogica.isMioTurno(token)) { // TODO aggiungere limite mosse
			return myLogica.deponiUovo(token, idDinosauro);
		}
		return null;
	}

	@Override
	public Object confermaTurno(String token) {
		if (myLogica.isMioTurno(token)) myLogica.confermaTurno();
		else return;
	}

	@Override
	public Object passaTurno(String token) {
		// TODO Auto-generated method stub
		return null;
	}
}