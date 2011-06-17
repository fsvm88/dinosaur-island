package dinolib.Razza;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dinolib.Exceptions.GenericDinosauroException;
import dinolib.Mappa.Coord;

/**
 * Astrae la collezione dei dinosauri rispetto al giocatore.
 */
public class Razza implements Set<Dinosauro> {
	/* Tutte le variabili statiche/definitive e non modificabili */
	/**
	 * Definisce definitivamente i turni di vita massimi per una specie.
	 * uml.property name="turni_DI_VITA_MAX"
	 */
	private final int TURNI_DI_VITA_MAX = 120;
	/**
	 * Variabile che definisce il nome della specie di Dinosauri. Viene impostata definitivamente dal costruttore.
	 * @uml.property  name="nomeRazza"
	 */
	private String nome = null;
	/**
	 * Contiene il numero massimo di dinosauri per specie. È una costante di gioco.
	 * @uml.property  name="numero_MAX_DINOSAURI"
	 */
	private final int numero_MAX_DINOSAURI = 5;

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
	 * uml.property name="turniDiVita"
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
	 * Implementa la costruzione della classe Specie.
	 * Richiede il nuovo nome della razza e l'istanza del nuovo dinosauro.
	 */
	public Razza(String nomeRazza, Character tipoRazza) { // Testato
		this.nome = nomeRazza;
		this.tipoRazza = tipoRazza;
	}

	/* Tutti i getter */
	/**
	 * @return
	 * @uml.property  name="nomeRazza"
	 */
	public String getNome() { return nome; }
	/**
	 * @return
	 * @uml.property  name="punteggio"
	 */
	public int getPunteggio() { return punteggio; } // Testato
	/**
	 * @return
	 * @uml.property  name="tipoRazza"
	 */
	public Character getTipo() { return tipoRazza; } // Testato
	public Dinosauro getDinosauroById(String idDinosauroCercato) { // Testato
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
	protected boolean hasNumeroMassimo() { // Testato
		if (this.size() >= numero_MAX_DINOSAURI) return true;
		else return false;
	}
	/**
	 * Helper booleano per verificare l'esistenza di un id (cioè l'esistenza di un dinosauro).
	 * @param idDaCercare
	 * @return
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
	private void aggiornaPunteggio() {
		Iterator<Dinosauro> itDinosauri = this.iterator();
		while (itDinosauri.hasNext()) {
			punteggio += (1+itDinosauri.next().getDimensione()); 
		}
	}
	/**
	 * Helper per invecchiare i dinosauri nella specie.
	 */
	private void invecchiaDinosauri() {
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
	 * Helper per aggiornare la specie.
	 */
	public void aggiornaRazza() { // Testato
		turniDiVita += 1;
		if (turniDiVita >= TURNI_DI_VITA_MAX) {
			dinosauri.clear();
		}
		if (!isEmpty()) {
			aggiornaPunteggio();
			invecchiaDinosauri();
		}
	}
	/**
	 * Rimuove un dinosauro usando l'id come parametro.
	 * @param idToRemove
	 */
	public void removeById(String idToRemove) { // Testato
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
	 * @param idDinosauro
	 * @throws GenericDinosauroException 
	 */
	public void cresciDinosauro(String idDinosauro) throws GenericDinosauroException { // Testato
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
	 * @param idDinosauro
	 * @throws GenericDinosauroException
	 */
	public void deponiUovo(String idDinosauro) throws GenericDinosauroException { // Testato
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
	 * @param idDinosauro
	 * @param newCoord
	 * @throws GenericDinosauroException 
	 */
	public void muoviDinosauro(String idDinosauro, Coord newCoord) throws GenericDinosauroException { // Testato
		Dinosauro tempDinosauro = getDinosauroById(idDinosauro);
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

	/* Tutti i metodi importati dall'interfaccia, questi sono quelli supportati */
	@Override
	public int size() { // Testato
		return dinosauri.size();
	}

	@Override
	public boolean isEmpty() { // Testato
		if (dinosauri.isEmpty()) return true;
		else return false;
	}

	@Override
	public boolean contains(Object o) { // Testato
		if (dinosauri.contains(o)) return true;
		else return false;
	}

	@Override
	public Iterator<Dinosauro> iterator() { // Testato
		return dinosauri.iterator();
	}

	@Override
	public void clear() { dinosauri = new HashSet<Dinosauro>(); } // Testato

	@Override
	public boolean add(Dinosauro e) { // Testato
		if (e != null) {
			Iterator<Dinosauro> itDinosauro = this.iterator();
			while (itDinosauro.hasNext()) {
				if (e.getIdDinosauro().equals(itDinosauro.next().getIdDinosauro())) return false;
			}
			dinosauri.add(e);
			return true;
		}
		else return false;
	}

	@Override
	public boolean remove(Object o) {  // Testato
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
	@Override
	public boolean addAll(Collection<? extends Dinosauro> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
	@Override
	public Object[] toArray() { throw new UnsupportedOperationException(); }
	@Override
	public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}