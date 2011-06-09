package dinolib;

import java.io.IOException;
import java.util.Iterator;

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

	/**
	 * Helper per assemblare il punteggio per un singolo giocatore.
	 * @param newBuffer
	 * @param giocatore
	 * @return
	 */
	private String assemblaPunteggio(String newBuffer, Giocatore giocatore) {
		newBuffer = assemblaBuffer(newBuffer, giocatore.getNome());
		newBuffer = assemblaBuffer(newBuffer, giocatore.getRazza().getNome());
		newBuffer = newBuffer + "," + giocatore.getPunteggio();
		if (giocatore.getRazza().isEmpty()) newBuffer = assemblaBuffer(newBuffer, "s");
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
	 * Assembla lo stato comune del dinosauro
	 */
	private String assemblaStatoComuneDinosauro(Giocatore tempGiocatore, Dinosauro tempDinosauro) {
		return tempGiocatore.getNome() + "," +
		tempGiocatore.getRazza().getNome() + "," +
		tempGiocatore.getRazza().getTipo().toLowerCase().charAt(0) + "," +
		"{" + "," + tempDinosauro.getX() + "," + CommonUtils.translateYforClient(tempDinosauro.getY(), myLogica.getLatoDellaMappa()) + "," + "}" + "," +
		tempDinosauro.getDimensione();
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

	@Override
	public Object creaUtente(String nomeUtente, String passwordUtente) {
		if (!myLogica.existsPlayerWithName(user)) {
			myLogica.doCreaUtente(user, pwd);
			return;
		}
	}

	@Override
	public Object loginUtente(String nomeUtente, String passwordUtente) {
		if (myLogica.existsPlayerWithName(user)) {
			Giocatore tempGiocatore = myLogica.getPlayerByName(user);
			if (tempGiocatore.passwordIsValid(pwd)) {
				return myLogica.doLoginUtente(tempGiocatore);
			}
			else throw new UserAuthenticationFailedException();
		}
		throw new UserAuthenticationFailedException();
	}

	@Override
	public Object creaRazza(String token, String nomeRazza, String tipo) {
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

	@Override
	public Object accessoPartita(String token) {
		myLogica.accediAPartita(token);
	}

	@Override
	public Object uscitaPartita(String token) {
		if (myLogica.isPlayerInGame(token)) myLogica.doEsciDallaPartita(token);
		else throw new NonInPartitaException();
	}

	@Override
	public Object listaGiocatori(String token) {
		if (myLogica.existsPlayerWithToken(token)) {
			Iterator<Giocatore> itGiocatori = myLogica.getIteratorOnPlayers();
			String buffer = null;
			while (itGiocatori.hasNext()) {
				String curNomeGiocatore = itGiocatori.next().getNome();
				if (myLogica.getPlayerByName(curNomeGiocatore).isLogged() &&
						myLogica.getPlayerByName(curNomeGiocatore).isInGame()) {
					buffer = assemblaBuffer(buffer, curNomeGiocatore);
				}
			}
			return buffer;
		}
		return null;
	}

	@Override
	public Object classifica(String token) {
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

	@Override
	public Object logoutUtente(String token) {
		myLogica.doLogout(token);
	}

	@Override
	public Object mappaGenerale(String token) {
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
		else throw new NonInPartitaException();
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