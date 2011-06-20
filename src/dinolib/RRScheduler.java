package dinolib;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
/**
 * @author fabio
 */
/**
 * Classe che implementa lo scheduler per i turni dei giocatori.
 * Gestisce la coda dei giocatori attualmente in gioco e nient'altro.
 */
public class RRScheduler {
	/**
	 * Contiene il numero massimo dei giocatori in gioco.
	 * Ne viene fatta una copia locale da parte del costruttore, di modo da poter implementare altre funzioni.
	 * @uml.property name="MAX_PLAYERS_INGAME"
	 */
	private int max_PLAYERS_INGAME = 0;
	/**
	 * Istanzia il riferimento alla coda dei giocatori in gioco.
	 * @uml.property name="playersQueue"
	 */
	private ArrayBlockingQueue<String> playersQueue = null;
	/**
	 * Inizializza la coda con la lunghezza specificata.
	 * @param queue_lenght La lunghezza massima della coda dei giocatori.
	 */
	protected RRScheduler(int queue_lenght) { // Testato - throws IllegalArgumentException if ((queue_lenght <= 0) || queue_lenght != (int))
		max_PLAYERS_INGAME = queue_lenght;
		playersQueue = new ArrayBlockingQueue<String>(max_PLAYERS_INGAME);
	}

	/**
	 * Aggiunge un giocatore in fondo alla coda.
	 * @param token Il token del giocatore da aggiungere.
	 * @return True se il giocatore e' stato aggiunto (la collezione e' stata modificata), false altrimenti.
	 * @throws InterruptedException Se un altro thread sta gia' modificando la collezione.
	 */
	protected boolean newTask(String token) throws InterruptedException { // Testato
		if (!playersQueue.contains(token)) {
			playersQueue.put(token);
			return true;
		}
		else return false;
	}
	/**
	 * Rimuove un giocatore dalla lista (in qualunque posizione sia).
	 * @param token Il token del giocatore da rimuovere.
	 * @return True se il giocatore e' stato rimosso (la collezione e' stata modificata), false altrimenti.
	 */
	protected boolean killTask(String token) { // Testato
		if (playersQueue.contains(token)) {
			playersQueue.remove(token);
			return true;
		}
		else return false;
	}
	/**
	 * Ritorna la testa della coda, il token del primo utente che gioca.
	 * @return La testa della coda
	 * @throws InterruptedException
	 */
	protected String getCurrentTask() throws InterruptedException { return playersQueue.poll(1L, TimeUnit.SECONDS); } // Testato
	/**
	 * Dice se la lista e' piena.
	 * @return True se la lista e' piena, false altrimenti.
	 */
	protected boolean maxPlayers() { // Testato
		if (playersQueue.size() < max_PLAYERS_INGAME) return false;
		else return true;
	}
	/**
	 * Dice se qualcuno sta giocando (la lista non e' vuota).
	 * @return True se qualcuno sta giocando, false altrimenti.
	 */
	protected boolean hasQueuedTasks() { // Testato
		if (playersQueue.size() == 0) return false;
		else return true;
	}
	/**
	 * Dice se la lista contiene un giocatore in particolare.
	 * @param token Il token del giocatore che non so se e' contenuto nella lista.
	 * @return True se il giocatore e' nella lista, false altrimenti.
	 */
	protected boolean hasTask(String token) { // Testato
		if (playersQueue.contains(token)) return true;
		else return false;
	}
	/**
	 * Ritorna un iteratore sulla lista di giocatori nella coda.
	 * @return L'iteratore sulla lista dei giocatori nella coda.
	 */
	protected Iterator<String> iterator() { return playersQueue.iterator(); } // Testato
}