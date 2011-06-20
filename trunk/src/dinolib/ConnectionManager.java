package dinolib;

import java.util.Hashtable;
import java.util.Iterator;

import dinolib.Exceptions.InvalidTokenException;
/**
 * @author fabio
 */
/**
 * Classe che gestisce le connessioni.
 */
class ConnectionManager {
	/**
	 * Istanzia il riferimento all'hashtable che contiene le coppie token-chiave.
	 * @uml.property name="connTable"
	 */
	private Hashtable<String, String> connTable = null;

	/**
	 * Inizializza l'hashtable.
	 */
	public ConnectionManager() { connTable = new Hashtable<String, String>(); } // Testato
	/**
	 * Controlla se esiste il token nella collezione.
	 * @param token Il token di cui verificare la validita'.
	 * @return True se il token esiste nella collezione, false altrimenti.
	 */
	protected boolean existsToken(String token) { // Testato
		if (token != null) {
			if (connTable.containsKey(token)) { 
				return true;
			}
		}
		return false;
	}
	/**
	 * Controlla se esiste il giocatore nella lista.
	 * @param nomeGiocatore Il nome del giocatore di cui verificare la validita'.
	 * @return True se il nome esiste nella collezione, false altrimenti.
	 */
	protected boolean existsName(String nomeGiocatore) { // Testato
		if (nomeGiocatore != null) {
			if (connTable.containsValue(nomeGiocatore)) return true;
		}
		return false;
	}
	/**
	 * Aggiunge un giocatore alla collezione.
	 * @param nomeGiocatore Il nome del giocatore da aggiungere.
	 * @param token Il token del giocatore da aggiungere.
	 * @return True se il giocatore e' stato aggiunto, false se il giocatore non e' stato aggiunto.
	 */
	protected boolean collega(String nomeGiocatore, String token) { // Testato
		if (connTable.put(token, nomeGiocatore) == null) return true;
		else return false;
	}
	/**
	 * Rimuove un giocatore dalla lista delle connessioni.
	 * @param token Il token del giocatore da scollegare.
	 * @return True se l'utente e' stato scollegato, false altrimenti.
	 */
	protected boolean scollega(String token) { // Testato
		if (connTable.remove(token) != null) return true;
		else return false;
	}
	/**
	 * Restituisce un iteratore sui token presenti nella collezione.
	 * @return Un iteratore sui token presenti nella collezione.
	 */
	protected Iterator<String> getIteratorOnTokens() { // Testato
		if (!connTable.isEmpty()) return connTable.keySet().iterator();
		else return null;
	}
	/**
	 * Restituisce un iteratore sui nomi dei giocatori presenti nella collezione.
	 * @return Un iteratore sui nomi dei giocatori presenti nella collezione.
	 */
	protected Iterator<String> getIteratorOnConnectedPlayerNames() { // Testato
		if (!connTable.isEmpty()) return connTable.values().iterator();
		else return null;
	}
	/**
	 * Restituisce il nome dell'utente associato al token.
	 * @return Il nome dell'utente associato al token.
	 * @throws InvalidTokenException Se il token non e' valido (non e' contenuto nella lista).
	 */
	protected String getName(String token) throws InvalidTokenException { // Testato
		if (existsToken(token)) return connTable.get(token);
		else throw new InvalidTokenException();
	}
	/**
	 * Restituisce il token dell'utente a partire dal suo nome.
	 * @param nomeGiocatore Il nome del giocatore associato al token richiesto.
	 * @return Il token associato al nome del giocatore.
	 */
	protected String getToken(String nomeGiocatore) { // Testato
		Iterator<String> itNames = getIteratorOnTokens();
		if (itNames == null) return null;
		String tmpToken = null;
		while (itNames.hasNext()) {
			tmpToken = itNames.next();
			if (connTable.get(tmpToken).equals(nomeGiocatore)) return tmpToken;  
		}
		return null;
	}
}