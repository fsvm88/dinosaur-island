package dinolib.GameObjects;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dinolib.ConfigurationOpts;
import dinolib.Exceptions.GenericDinosauroException;

/**
 * Astrae la collezione dei dinosauri rispetto al giocatore.
 */
public class Razza implements Set<Dinosauro>, Serializable {
	/**
	 * Generated version ID for Serializable.
	 */
	private static final long serialVersionUID = -6356926535239722927L;
	/* Tutte le variabili statiche/definitive e non modificabili */
	/**
	 * Variabile che definisce il nome della specie di Dinosauri. Viene impostata definitivamente dal costruttore.
	 * @uml.property  name="nomeRazza"
	 */
	private String nome = null;
	
	/* Tutte le variabili istanziabili */
	/**
	 * ConcurrentHashMap con tutti i dinosauri presenti nella specie.
	 * @uml.property  name="Dinosauri"
	 */
	private HashSet<Dinosauro> dinosauri = new HashSet<Dinosauro>();
	/**
	 * Contiene il punteggio corrente della specie.
	 * @uml.property  name="punteggio"
	 */
	private int punteggio = 0;
	/**
	 * Contiene i turni di vita della specie.
	 * @uml.property name="turniDiVita"
	 */
	private int turniDiVita = 0;
	/**
	 * Contiene il tipo della r+azza.
	 * Impostata definitivamente solo dal costruttore.
	 * @uml.property name="tipoRazza"
	 */
	private Character tipoRazza = null;

	/* Costruttore */
	/**
	 * Richiede il nuovo nome della razza e il tipo della razza.
	 * @param nomeRazza Il nome della razza.
	 * @param tipoRazza Il tipo della razza.
	 */
	Razza(String nomeRazza, Character tipoRazza) { // Testato
		this.nome = nomeRazza;
		this.tipoRazza = tipoRazza;
	}

	/* Tutti i getter */
	/**
	 * Restituisce il nome della razza.
	 * @return Il nome della razza.
	 */
	public String getNome() { return nome; }
	/**
	 * Restituisce il punteggio corrente della razza.
	 * @return Il punteggio corrente della razzza.
	 */
	public int getPunteggio() { return punteggio; } // Testato
	/**
	 * Restituisce il tipo della razza.
	 * @return Il tipo della razza.
	 */
	public Character getTipo() { return tipoRazza; } // Testato
	/**
	 * Restituisce un dinosauro a partire dal suo Id.
	 * @param idDinosauroCercato L'Id del dinosauro che sto cercando.
	 * @return Il dinosauro che stavo cercando. Null altrimenti.
	 */
	public synchronized Dinosauro getDinosauroById(String idDinosauroCercato) { // Testato
		if (existsDinosauroWithId(idDinosauroCercato)) {
			Iterator<Dinosauro> itDinosauri = this.iterator();
			Dinosauro tempDinosauro;
			while (itDinosauri.hasNext()) {
				tempDinosauro = itDinosauri.next();
				if (tempDinosauro.getIdDinosauro().equals(idDinosauroCercato)) return tempDinosauro;
			}
		}
		return null;
	}
	/**
	 * Verifica se la razza ha il numero massimo di dinosauri.
	 * @return True se ho raggiunto il numero massimo, False altrimenti.
	 */
	protected synchronized boolean hasNumeroMassimo() { // Testato
		if (this.size() >= ConfigurationOpts.NUMERO_MAX_DINOSAURI) return true;
		else return false;
	}
	/**
	 * Verifica se esiste un dinosauro nella razza.
	 * @param idDaCercare L'Id del dinosauro che non so se esiste.
	 * @return True se il dinosauro esiste, false altrimenti.
	 */
	public boolean existsDinosauroWithId(String idDaCercare) { // Testato
		Iterator<Dinosauro> itDinosauri = this.iterator();
		while (itDinosauri.hasNext()) {
			if (itDinosauri.next().getIdDinosauro().equals(idDaCercare)) return true;
		}
		return false;
	}
	/* Tutti i setter */
	/**
	 * Aggiorna il punteggio della specie.
	 */
	private synchronized void aggiornaPunteggio() {
		Iterator<Dinosauro> itDinosauri = this.iterator();
		while (itDinosauri.hasNext()) {
			punteggio += (1+itDinosauri.next().getDimensione()); 
		}
	}
	/**
	 * Helper per invecchiare i dinosauri nella specie.
	 */
	private synchronized void invecchiaDinosauri() {
		Iterator<Dinosauro> itDinosauri = this.iterator();
		Dinosauro tempDinosauro = null;
		while (itDinosauri.hasNext()) {
			tempDinosauro = itDinosauri.next();
			tempDinosauro.invecchia();
			if (tempDinosauro.getTurnoDiVita()>tempDinosauro.getDurataVitaMax()) {
				tempDinosauro.nonUsabile();
				remove(tempDinosauro);
			}
		}
	}
	/**
	 * Aggiorna la razza di dinosauri.
	 * Se la razza non e' estinta aggiorna il punteggio e invecchia i suoi dinosauri.
	 * Se la razza non e' estinta aumenta di 1 il turno di vita.
	 * Verifica se la razza deve estinguersi questo turno perche' e' troppo vecchia, se si' estinguila.
	 */
	public synchronized void aggiornaRazza() { // Testato
		if (!isEmpty()) {
			aggiornaPunteggio();
			invecchiaDinosauri();
		}
		if (!isEmpty()) {
			turniDiVita += 1;
			if (turniDiVita >= ConfigurationOpts.TURNI_DI_VITA_MAX) {
				dinosauri.clear();
			}
		}
	}
	/**
	 * Rimuove un dinosauro dalla razza usando l'id come parametro.
	 * @param idToRemove L'Id del dinosauro da rimuovere.
	 */
	public synchronized void removeById(String idToRemove) { // Testato
		Iterator<Dinosauro> itDinosauri = this.iterator();
		Dinosauro tempDinosauro = null;
		while(itDinosauri.hasNext()) {
			tempDinosauro = itDinosauri.next();
			if (tempDinosauro.getIdDinosauro().equals(idToRemove)) {
				break;
			}
		}
		dinosauri.remove(tempDinosauro);
		return;
	}
	/**
	 * Helper per far crescere il dinosauro richiesto.
	 * Nel caso muoia d'inedia lo uccide, lo rimuove e lancia un'eccezione.
	 * @param idDinosauro L'Id del dinosauro da crescere.
	 * @throws GenericDinosauroException Se il dinosauro muore per inedia o raggiunge la dimensione massima.
	 */
	public synchronized void cresciDinosauro(String idDinosauro) throws GenericDinosauroException { // Testato
		Dinosauro tempDinosauro = getDinosauroById(idDinosauro);
		if (!tempDinosauro.isAtDimensioneMax()) {
			if (tempDinosauro.hasEnergyToGrow()) {
				tempDinosauro.cresci();
			}
			else {
				this.remove(tempDinosauro);
				throw new GenericDinosauroException("mortePerInedia");
			}
		}
		else throw new GenericDinosauroException("raggiuntaDimensioneMax");
	}
	/**
	 * Helper per far deporre l'uovo al dinosauro richiesto.
	 * Nel caso muoia d'inedia lo uccide, lo rimuove e lancia un'eccezione.
	 * @param idDinosauro L'Id del dinosauro a cui far deporre l'uovo.
	 * @throws GenericDinosauroException Se il dinosauro muore per inedia o la razza ha gia' il numero massimo di dinosauri.
	 */
	public synchronized void deponiUovo(String idDinosauro) throws GenericDinosauroException { // Testato
		Dinosauro tempDinosauro = getDinosauroById(idDinosauro);
		if (!hasNumeroMassimo()) {
			if (tempDinosauro.hasEnergyToRepl()) {
				tempDinosauro.deponiUovo();
			}
			else {
				this.remove(tempDinosauro);
				throw new GenericDinosauroException("mortePerInedia");
			}
		}
		else throw new GenericDinosauroException("raggiuntoNumeroMaxDinosauri");
	}
	/**
	 * Helper per far muovere il dinosauro.
	 * Nel caso muoia d'inedia lo uccide, lo rimuove e lancia un'eccezione.
	 * @param idDinosauro L'id del dinosauro da muovere.
	 * @param newCoord Le nuove coordinate del dinosauro.
	 * @throws GenericDinosauroException Se il dinosauro muore per inedia o ha gia' raggiunto il suo limite di mosse.
	 */
	public synchronized void muoviDinosauro(String idDinosauro, Coord newCoord) throws GenericDinosauroException { // Testato
		Dinosauro tempDinosauro = getDinosauroById(idDinosauro);
		if (tempDinosauro.hasMovimento()) {
			if (tempDinosauro.getEnergiaAttuale()<tempDinosauro.getEnergiaMovimento()) {
				tempDinosauro.nonUsabile();
				this.removeById(idDinosauro);
				throw new GenericDinosauroException("mortePerInedia");
			}
			else {
				tempDinosauro.setCoord(newCoord);
				tempDinosauro.setEnergiaAttuale(tempDinosauro.getEnergiaAttuale()-tempDinosauro.getEnergiaMovimento());
				tempDinosauro.haMosso();
				return;
			}
		}
		else throw new GenericDinosauroException("raggiuntoLimiteMosseDinosauro");
	}

	/* Tutti i metodi importati dall'interfaccia, questi sono quelli supportati */
	/**
	 * Restituisce la dimensione della razza.
	 * @return Intero che dice quanti dinosauri sono presenti nella razza.
	 */
	@Override
	public int size() { // Testato
		return dinosauri.size();
	}

	/**
	 * Dice se la razza e' vuota.
	 * @return True se la razza e' vuota, false se non e' vuota.
	 */
	@Override
	public boolean isEmpty() { // Testato
		if (dinosauri.isEmpty()) return true;
		else return false;
	}

	/**
	 * Restituisce un iteratore sulla razza.
	 * @return Iteratore sulla razza.
	 */
	@Override
	public Iterator<Dinosauro> iterator() { // Testato
		return dinosauri.iterator();
	}
	/**
	 * Aggiunge un dinosauro alla razza (se non e' gia' presente).
	 * @return True se la collezione e' stata modificata (il dinosauro e' stato aggiunto), false se la collezione non e' stata modificata (il dinosauro non e' stato aggiunto).
	 */
	@Override
	public synchronized boolean add(Dinosauro e) { // Testato
		if (e != null) {
			Iterator<Dinosauro> itDinosauro = this.iterator();
			while (itDinosauro.hasNext()) {
				if (e.getIdDinosauro().equals(itDinosauro.next().getIdDinosauro())) {
					return false;
				}
			}
			dinosauri.add(e);
			return true;
		}
		else return false;
	}

	/**
	 * Rimuove un dinosauro richiesto dalla razza.
	 * @return True se la collezione e' stata modificata (il dinosauro e' stato rimosso), false se la collezione non e' stata modificata (il dinosauro non e' stato rimosso).
	 */
	@Override
	public synchronized boolean remove(Object o) {  // Testato
		if (o != null) {
			dinosauri.remove((Dinosauro) o);
			if (this.isEmpty()) {
				dinosauri.clear();
			}
			return true;
		}
		else return false;
	}

	/* Tutti i metodi importati dall'interfaccia, quelli non supportati */
	/**
	 * Operazione non supportata.
	 */
	@Override
	public boolean addAll(Collection<? extends Dinosauro> c) { throw new UnsupportedOperationException(); }
	/**
	 * Operazione non supportata.
	 */
	@Override
	public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	/**
	 * Operazione non supportata.
	 */
	@Override
	public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	/**
	 * Operazione non supportata.
	 */
	@Override
	public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	/**
	 * Operazione non supportata.
	 */
	@Override
	public Object[] toArray() { throw new UnsupportedOperationException(); }
	/**
	 * Operazione non supportata.
	 */
	@Override
	public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
	/**
	 * Operazione non supportata.
	 */
	@Override
	public boolean contains(Object o) { throw new UnsupportedOperationException(); }
	/**
	 * Operazione non supportata.
	 */
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
}