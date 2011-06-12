package dinolib;

import java.util.Iterator;
import java.util.NoSuchElementException;

import dinolib.Exceptions.*;
import dinolib.Mappa.Cella;
import dinolib.Mappa.Coord;
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
	 * Aggiunge al buffer una singola cella della mappa.
	 * Non include informazioni addizionali se questa è vegetazione o carogna.
	 * @param tmpBuf
	 * @param myChar
	 * @return
	 */
	private String aggiungiCellaSingolaSenzaInfoAddizionali(String tmpBuf, Character myChar) {
		return tmpBuf + "[" + myChar.charValue() + "]";
	}

	/**
	 * Assembla tutta la mappa generale in un unico buffer tramite l'iteratore sulla mappa.
	 * @param mioBuffer
	 * @return
	 */
	private String assemblaMappaGenerale(String mioBuffer, String token) throws InvalidTokenException {
		Iterator<Cella> itCelle = myLogica.getMappa().iterator();
		int i = 0;
		Cella tempCella = null;
		Character tipoCella = null;
		while (itCelle.hasNext()) {
			do {
				tempCella = itCelle.next();
				tipoCella = null;
				if (tempCella.isUserPassed(myLogica.getCMan().getName(token))) {
					tipoCella = tempCella.toString().toLowerCase().charAt(0);
					if (tipoCella.equals('d')) {
						tipoCella = tempCella.getCellaSuCuiSiTrova().toString().toLowerCase().charAt(0);
					}
					if ((tipoCella.equals('t')) ||
							(tipoCella.equals('a')) ||
							(tipoCella.equals('v'))) {
						mioBuffer = aggiungiCellaSingolaSenzaInfoAddizionali(mioBuffer, tempCella.toString().toLowerCase().charAt(0));
					}
					else if (tipoCella.equals('c')) {
						mioBuffer = aggiungiCellaSingolaSenzaInfoAddizionali(mioBuffer, 't');
					}
				}
				else {
					mioBuffer = aggiungiCellaSingolaSenzaInfoAddizionali(mioBuffer, 'b');
				}
				i++;
			} while (i < myLogica.getMappa().getLatoDellaMappa());
			mioBuffer = mioBuffer + ";";
			i = 0;
		}
		return mioBuffer;
	}

	/**
	 * Aggiunge al buffer una singola cella della mappa.
	 * Aggiunge anche informazioni addizionali se si tratta di carogna, vegetazione o dinosauro.
	 * @param tmpBuf
	 * @param myChar
	 * @param myInfo
	 * @return
	 */
	private String aggiungiCellaSingolaConInfoAddizionali(String tmpBuf, Character myChar, Object myInfo) {
		return tmpBuf + "[" + myChar.charValue() + "," + myInfo.toString() + "]";
	}

	/**
	 * Aggiunge al buffer una cella, viene chiamata solo da vista locale.
	 * @param buffer
	 * @param miaCella
	 * @return
	 */
	private String assemblaBufferCellaSingolaPerVistaLocale(String buffer, Cella miaCella) {
		Character tipoCella = miaCella.toString().toLowerCase().charAt(0);
		if (tipoCella.equals('t') ||
				tipoCella.equals('a')) return aggiungiCellaSingolaSenzaInfoAddizionali(buffer, tipoCella);
		else if (tipoCella.equals('v') ||
				tipoCella.equals('c')) return aggiungiCellaSingolaConInfoAddizionali(buffer, tipoCella, miaCella.getValoreAttuale());
		else if (tipoCella.equals('d')) return aggiungiCellaSingolaConInfoAddizionali(buffer, tipoCella, miaCella.getIdDelDinosauro());
		return null;
	}

	/**
	 * Assembla lo stato del dinosauro comune a entrambi gli if/else di statoDinosauro.
	 */
	private String assemblaStatoComuneDinosauro(Giocatore tempGiocatore, String idDinosauro) {
		return tempGiocatore.getNome() + "," +
		tempGiocatore.getRazza().getNome() + "," +
		tempGiocatore.getRazza().getTipo().toLowerCase().charAt(0) + ",{," +
		tempGiocatore.getRazza().getDinosauroById(idDinosauro).getCoord().getX() + "," + tempGiocatore.getRazza().getDinosauroById(idDinosauro).getCoord().getY() + ",}," +
		tempGiocatore.getRazza().getDinosauroById(idDinosauro).getDimensione();
	}
	/**
	 * Helper per alcune delle funzioni che restituiscono liste.
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
				buffer = assemblaMappaGenerale(buffer, token);
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
		try {
			if (myLogica.isPlayerInGame(token)) {
				String buffer = "@listaDinosauri";
				if (myLogica.getPlayerByToken(token).hasRazza()) {
					Iterator<Dinosauro> itDinosauri = myLogica.getPlayerByToken(token).getRazza().iterator();
					while (itDinosauri.hasNext()) {
						buffer = assemblaBuffer(buffer, itDinosauri.next().getIdDinosauro());
					}
				}
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
	public Object vistaLocale(String token, String idDinosauro) {
		try {
			if (myLogica.isPlayerInGame(token)) {
				if (myLogica.getPlayerByToken(token).getRazza().existsDinosauroWithId(idDinosauro)) {
					String buffer = "@vistaLocale";
					Dinosauro tempDinosauro = myLogica.getPlayerByToken(token).getRazza().getDinosauroById(idDinosauro);
					int rangeVista = tempDinosauro.getRangeVista();
					int leftCornerX = myLogica.doSubtraction(tempDinosauro.getCoord().getX(), rangeVista);
					int bottomCornerY = myLogica.doSubtraction(tempDinosauro.getCoord().getY(), rangeVista);
					int rightCornerX = myLogica.doAddition(tempDinosauro.getCoord().getX(), rangeVista);
					int topCornerY = myLogica.doAddition(tempDinosauro.getCoord().getY(), rangeVista);
					buffer = buffer + ",{" + leftCornerX + "," + bottomCornerY + "},{"
					+ (topCornerY-bottomCornerY) + "," + (rightCornerX-leftCornerX) + "},";
					Iterator<Cella> subItCelle = myLogica.getMappa().subIterator(new Coord(leftCornerX, bottomCornerY), (topCornerY-bottomCornerY), (rightCornerX-leftCornerX));
					Cella tmpCella = null;
					int row = bottomCornerY;
					int col = leftCornerX;
					do {
						do {
							if (subItCelle.hasNext()) {
								tmpCella = subItCelle.next();
								buffer = assemblaBufferCellaSingolaPerVistaLocale(buffer, tmpCella);
								col++;
							}
							else throw new NoSuchElementException();
						} while (col <= rightCornerX);
						buffer = buffer + ";";
						row++;
						col = leftCornerX;
					} while (row <= topCornerY);
					return buffer;
				}
				else return "@no,@idNonValido";
			}
			else return "@no,@nonInPartita";
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		}
	}

	@Override
	public Object statoDinosauro(String token, String idDinosauro) {
		try {
			if (myLogica.isPlayerInGame(token)) {
				if (myLogica.existsDinosauroWithId(idDinosauro)) {
					String buffer = "@statoDinosauro,";
					if (myLogica.getPlayerByToken(token).getRazza().existsDinosauroWithId(idDinosauro)) {
						buffer = assemblaStatoComuneDinosauro(myLogica.getPlayerByToken(token), idDinosauro) +
						myLogica.getPlayerByToken(token).getRazza().getDinosauroById(idDinosauro).getEnergiaAttuale() +
						myLogica.getPlayerByToken(token).getRazza().getDinosauroById(idDinosauro).getTurnoDiVita();
					}
					else if (!myLogica.getPlayerByToken(token).getRazza().existsDinosauroWithId(idDinosauro)) {
						buffer = assemblaStatoComuneDinosauro(myLogica.getPlayerByIdDinosauro(idDinosauro), idDinosauro);
					}
					return buffer;
				}
				else return "@no,@idNonValido";
			}
			else return "@no,@nonInPartita";
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		}
	}

	@Override
	public Object muoviDinosauro(String token, String idDinosauro, Coord newCoord) {
		try {
			if (myLogica.isMioTurno(token)) {
				if (myLogica.getPlayerByToken(token).getRazza().existsDinosauroWithId(idDinosauro)) {
					String ret = myLogica.doMuoviDinosauro(token, idDinosauro, newCoord);
					if (ret.equals("v")) return "@combattimento,v";
					else if (ret.equals("p")) return "@combattimento,p";
					else if (ret.equals("@ok")) return "@ok";
					else return "@no";
				}
				else return "@no,@idNonValido";
			}
			else return "@no,@nonIlTuoTurno";
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		} catch (NonInPartitaException e) {
			return "@no,@nonInPartita";
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		}
	}

	@Override
	public Object cresciDinosauro(String token, String idDinosauro) {
		try {
			if (myLogica.isMioTurno(token)) {
				if ( myLogica.getPlayerByToken(token).getRazza().existsDinosauroWithId(idDinosauro)) {
					myLogica.getPlayerByToken(token).getRazza().cresciDinosauro(idDinosauro);
					return "@ok";
				}
				return "@no,@idNonValido";
			}
			else return "@no,@nonIlTuoTurno";
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		} catch (NonInPartitaException e) {
			return "@no,@nonInPartita";
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		} catch (GenericDinosauroException e) {
			if (e.getMessage().equals("mortePerInedia")) return "@no,@mortePerInedia";
			if (e.getMessage().equals("raggiuntaDimensioneMax")) return "@no,@raggiuntaDimensioneMax";
		}
		return "@no";
	}

	@Override
	public Object deponiUovo(String token, String idDinosauro) {
		try {
			if (myLogica.isMioTurno(token)) {
				if ( myLogica.getPlayerByToken(token).getRazza().existsDinosauroWithId(idDinosauro)) {
					String ret = myLogica.doDeponiUovo(token, idDinosauro);
					if (ret != null) { return "@ok," + ret; }
				}
				return "@no,@idNonValido";
			}
			else return "@no,@nonIlTuoTurno";
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		} catch (NonInPartitaException e) {
			return "@no,@nonInPartita";
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		} catch (GenericDinosauroException e) {
			if (e.getMessage().equals("mortePerInedia")) return "@no,@mortePerInedia";
			if (e.getMessage().equals("raggiuntaDimensioneMax")) return "@no,@raggiuntaDimensioneMax";
		}
		return "@no";
	}

	@Override
	public Object confermaTurno(String token) {
		try {
			if (myLogica.isMioTurno(token)) {
				myLogica.doConfermaTurno();
				return "@ok";
			}
			return "@no";
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		} catch (NonInPartitaException e) {
			return "@no,@nonInPartita";
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		}
	}

	@Override
	public Object passaTurno(String token) {
		try {
			if (myLogica.isMioTurno(token)) {
				myLogica.doPassaTurno();
				return "@ok";
			}
			return "@no";
		} catch (InvalidTokenException e) {
			return returnInvalidToken();
		} catch (NonInPartitaException e) {
			return "@no,@nonInPartita";
		} catch (NonAutenticatoException e) {
			return returnInvalidToken();
		}
	}
}