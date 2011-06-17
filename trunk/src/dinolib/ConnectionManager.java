package dinolib;

import java.util.Hashtable;
import java.util.Iterator;

import dinolib.Exceptions.InvalidTokenException;

class ConnectionManager {
	/**
	 * Istanzia il riferimento all'hashtable che contiene le coppie token-chiave.
	 * @uml.property name="connTable"
	 */
	private Hashtable<String, String> connTable = null;

	/**
	 * Costruttore, inizializza l'hashtable.
	 */
	public ConnectionManager() { // Testato
		connTable = new Hashtable<String, String>();
	}
	/**
	 * Controlla se esiste il token nella lista.
	 * @param token
	 * @return
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
	 * Controlla se esiste l'utente nella lista.
	 * @param nomeGiocatore
	 * @return
	 */
	protected boolean existsName(String nomeGiocatore) { // Testato
		if (connTable.containsValue(nomeGiocatore)) return true;
		else return false;
	}

	/**
	 * Aggiunge un utente alla lista delle connessioni.
	 * @param nomeGiocatore
	 * @param token
	 * @return
	 */
	protected boolean collega(String nomeGiocatore, String token) { // Testato
		if (connTable.put(token, nomeGiocatore) == null) return true;
		else return false;
	}
	/**
	 * Rimuove un utente dalla lista delle connessioni.
	 * @param token
	 * @return
	 */
	protected boolean scollega(String token) { // Testato
		if (connTable.remove(token) != null) return true;
		else return false;
	}
	/**
	 * Dà un iteratore sui token.
	 * @return
	 */
	protected Iterator<String> getIteratorOnTokens() { // Testato
		if (!connTable.isEmpty()) return connTable.keySet().iterator();
		else return null;
	}
	/**
	 * Dà un iteratore sui nomi dei giocatori connessi.	
	 * @return
	 */
	protected Iterator<String> getIteratorOnConnectedPlayerNames() { // Testato
		if (!connTable.isEmpty()) return connTable.values().iterator();
		else return null;
	}
	/**
	 * Restituisce il nome dell'utente associato al token.
	 * @return 
	 * @throws InvalidTokenException 
	 */
	protected String getName(String token) throws InvalidTokenException { // Testato
		if (existsToken(token)) return connTable.get(token);
		else throw new InvalidTokenException();
	}
	/**
	 * Restituisce il token dell'utente a partire dal suo nome.
	 * @param nomeGiocatore
	 * @return
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
