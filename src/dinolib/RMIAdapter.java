package dinolib;

import dinolib.CommunicationObjects.Classifica;
import dinolib.CommunicationObjects.ListaGiocatori;
import dinolib.Exceptions.*;
import dinolib.GameObjects.Coord;

public class RMIAdapter implements Adapter {
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
	public RMIAdapter (Logica newLogica) {
		myLogica = newLogica;
	}

	@Override
	public Object creaUtente(String nomeUtente, String passwordUtente) throws UserExistsException {
		return myLogica.doCreaUtente(nomeUtente, passwordUtente);
	}

	@Override
	public Object loginUtente(String nomeUtente, String passwordUtente) throws UserAuthenticationFailedException {
		if (myLogica.doLogin(nomeUtente, passwordUtente)) { return myLogica.getCMan().getToken(nomeUtente); }
		else { return null; }
	}

	@Override
	public Object creaRazza(String token, String nomeRazza, Character tipo) throws NomeRazzaOccupatoException, InvalidTokenException {
		return myLogica.doCreaRazza(token, nomeRazza, tipo);
	}

	@Override
	public Object accessoPartita(String token) throws InvalidTokenException, TroppiGiocatoriException, RazzaNonCreataException, InterruptedException {
		return myLogica.doAccessoPartita(token);
	}

	@Override
	public Object uscitaPartita(String token) throws InvalidTokenException {
		return myLogica.doUscitaPartita(token);
	}

	@Override
	public Object listaGiocatori(String token) throws InvalidTokenException, NonInPartitaException {
		if (myLogica.isPlayerInGame(token)) {
			if (myLogica.getPlayerByToken(token).hasRazza()) {
				return new ListaGiocatori(myLogica.getPMan());
			}
		}
		throw new NonInPartitaException();
	}

	@Override
	public Object classifica(String token) throws InvalidTokenException, NonInPartitaException {
		if (myLogica.isPlayerLogged(token)) {
			return new Classifica(myLogica);
		}
		throw new NonInPartitaException();
	}

	@Override
	public Object logoutUtente(String token) throws InvalidTokenException {
		return myLogica.doLogout(token);
	}

	@Override
	public Object mappaGenerale(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object listaDinosauri(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object vistaLocale(String token, String idDinosauro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object statoDinosauro(String token, String idDinosauro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object muoviDinosauro(String token, String idDinosauro, Coord newCoord) throws InvalidTokenException, GenericDinosauroException {
		return myLogica.doMuoviDinosauro(token, idDinosauro, newCoord);
	}

	@Override
	public Object cresciDinosauro(String token, String idDinosauro) throws InvalidTokenException, GenericDinosauroException {
		return myLogica.doCresciDinosauro(token, idDinosauro);
	}

	@Override
	public Object deponiUovo(String token, String idDinosauro) throws GenericDinosauroException, InvalidTokenException {
		return myLogica.doDeponiUovo(token, idDinosauro);
	}

	@Override
	public Object confermaTurno(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object passaTurno(String token) {
		// TODO Auto-generated method stub
		return null;
	}
}