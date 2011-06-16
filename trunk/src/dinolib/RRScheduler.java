package dinolib;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

class RRScheduler {
	/**
	 * Istanzia il riferimento alla coda dei giocatori in gioco.
	 */
	private ArrayBlockingQueue<String> playersQueue = null;
	/**
	 * Contiene il numero massimo dei giocatori in gioco.
	 * Ne viene fatta una copia locale da parte del costruttore, di modo da poter implementare altre funzioni.
	 * @uml.property name="MAX_PLAYERS_INGAME"
	 */
	private int max_PLAYERS_INGAME = 0;

	/**
	 * Costruttore, inizializza la coda con la lunghezza specificata.
	 * @param queue_lenght
	 */
	protected RRScheduler(int queue_lenght) { // Testato - throws IllegalArgumentException if ((queue_lenght <= 0) || queue_lenght != (int))
		playersQueue = new ArrayBlockingQueue<String>(queue_lenght);
		max_PLAYERS_INGAME = queue_lenght;
	}

	/**
	 * Aggiunge un giocatore in fondo alla coda.
	 * @return
	 * @throws InterruptedException
	 */
	protected boolean newTask(String token) throws InterruptedException { // Testato
		if (!playersQueue.contains(token)) {
			playersQueue.put(token);
			return true;
		}
		else return false;
	}
	/**
	 * Rimuove un giocatore dalla lista (da qualunque posizione).
	 * @param token
	 * @return
	 */
	protected boolean killTask(String token) { // Testato
		if (playersQueue.contains(token)) {
			playersQueue.remove(token);
			return true;
		}
		else return false;
	}
	/**
	 * Ritorna la testa della coda, il primo utente che gioca.
	 * @return
	 * @throws InterruptedException
	 */
	protected String getCurrentTask() throws InterruptedException {
		return playersQueue.poll(1L, TimeUnit.SECONDS);
	}
	/**
	 * Dice se la lista è piena.
	 * @return
	 */
	protected boolean maxPlayers() { // Testato
		if (playersQueue.size() < max_PLAYERS_INGAME) return false;
		else return true;
	}
	/**
	 * Dice se qualcuno sta giocando (la lista non è vuota).
	 * @return
	 */
	protected boolean hasQueuedTasks() { // Testato
		if (playersQueue.size() == 0) return false;
		else return true;
	}
	/**
	 * Dice se la lista contiene un giocatore in particolare.
	 * @param token
	 * @return
	 */
	protected boolean hasTask(String token) { // Testato
		if (playersQueue.contains(token)) return true;
		else return false;
	}
	/**
	 * Ritorna un iteratore sulla lista di giocatori nella coda.
	 * @return
	 */
	protected Iterator<String> iterator() { // Testato
		return playersQueue.iterator();
	}
}