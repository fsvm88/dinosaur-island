package dinolib.Razza;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import dinolib.Exceptions.GenericDinosauroException;
import dinolib.Mappa.Coord;

/**
 * @author  fabio
 */
public class RazzaTest {
	private Razza myRazza;
	private final Coord defaultCoord = new Coord(0,0);
	private final Character defaultType = 'e';

	// SetUp, creo una razza con valori di default accettabili
	@Before
	public void setUp() {
		myRazza = new Razza("testRazza", defaultType.charValue());
	}

	@Test
	public void testAdd() {
		assertEquals(0, myRazza.size());
		myRazza.add(new Erbivoro(defaultCoord));
		assertEquals(1, myRazza.size());
		myRazza.add(new Erbivoro(defaultCoord));
		assertEquals(2, myRazza.size());
		myRazza.add(new Erbivoro(defaultCoord));
		assertEquals(3, myRazza.size());
	}

	@Test
	public void testRemove() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		Iterator<Dinosauro> itDinosauro = myRazza.iterator();
		if (itDinosauro.hasNext()) {
			Dinosauro tempDinosauro = itDinosauro.next();
			assertNotNull(tempDinosauro);
		}
		itDinosauro.remove();
		assertEquals(2, myRazza.size());
	}

	@Test
	public void testIterator() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		Iterator<Dinosauro> itDinosauri = myRazza.iterator();
		assertNotNull(itDinosauri);
		assertTrue(itDinosauri.hasNext());
		assertNotNull(itDinosauri.next());
		assertTrue(itDinosauri.hasNext());
		assertNotNull(itDinosauri.next());
		assertTrue(itDinosauri.hasNext());
		assertNotNull(itDinosauri.next());
		assertFalse(itDinosauri.hasNext());
	}

	@Test
	public void testSize() {
		assertEquals(0, myRazza.size());
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
	}

	@Test
	public void testIsEmpty() {
		assertEquals(0, myRazza.size());
		assertTrue(myRazza.isEmpty());
		myRazza.clear();
		assertTrue(myRazza.isEmpty());
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		assertFalse(myRazza.isEmpty());
	}

	@Test
	public void testClear() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		myRazza.clear();
		assertEquals(0, myRazza.size());
	}

	@Test
	public void testContains() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		Iterator<Dinosauro> itDinosauri = myRazza.iterator();
		assertTrue(itDinosauri.hasNext());
		if (itDinosauri.hasNext()) {
			Dinosauro tempDinosauro = itDinosauri.next();
			assertTrue(myRazza.contains(tempDinosauro));
			itDinosauri.remove();
			assertFalse(myRazza.contains(tempDinosauro));
		}
	}

	@Test
	public void testRemoveById() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		int startCount = myRazza.size();
		Iterator<Dinosauro> itDinosauri = myRazza.iterator();
		Dinosauro tempDinosauro = null;
		int counter = 0;
		while (counter<2) {
			while (itDinosauri.hasNext()) {
				tempDinosauro = itDinosauri.next();
				counter++;
			}
		}
		myRazza.removeById(tempDinosauro.getIdDinosauro());
		assertEquals((startCount-1), myRazza.size());
		myRazza.add(tempDinosauro);
		assertEquals(startCount, myRazza.size());
	}
	
	@Test
	public void testGetTipo() {
		assertNotNull(myRazza.getTipo());
		assertEquals('e', myRazza.getTipo().charValue());
	}

	@Test
	public void testEstinzione() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		assertFalse(myRazza.isEmpty());
		Dinosauro tempDinosauro = null;
		// Prendo tutti i dinosauri e li rimuovo uno per uno
		while (!myRazza.isEmpty()) {
			Iterator<Dinosauro> itDinosauro = myRazza.iterator();
			while (itDinosauro.hasNext()) {
				tempDinosauro = itDinosauro.next();
			}
			if (tempDinosauro != null) {
				myRazza.remove(tempDinosauro);
			}
			else break;
		}
		// Alla fine la lista deve essere pulita (viene resettata da remove quando vede che è l'ultimo dinosauro)
		assertEquals(0, myRazza.size());
		assertTrue(myRazza.isEmpty());
	}

	@Test
	public void testGetDinosauroById() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		int startCount = myRazza.size();
		Iterator<Dinosauro> itDinosauri = myRazza.iterator();
		Dinosauro tempDinosauro = null;
		int counter = 0;
		while (counter<2) {
			while (itDinosauri.hasNext()) {
				tempDinosauro = itDinosauri.next();
				counter++;
			}
		}
		// Aggiungo e rimuovo il dinosauro con lo stesso id per verificare che effettivamente l'associazione dinosauro<->id è univoca
		assertNotNull(myRazza.getDinosauroById(tempDinosauro.getIdDinosauro()));
		myRazza.removeById(tempDinosauro.getIdDinosauro());
		assertEquals((startCount-1), myRazza.size());
		assertNull(myRazza.getDinosauroById(tempDinosauro.getIdDinosauro()));
		myRazza.add(tempDinosauro);
		assertEquals(startCount, myRazza.size());
		assertNotNull(myRazza.getDinosauroById(tempDinosauro.getIdDinosauro()));
	}

	@Test
	public void testHasNumeroMassimo() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		assertFalse(myRazza.hasNumeroMassimo());
		myRazza.add(new Erbivoro(defaultCoord));
		assertEquals(4, myRazza.size());
		assertFalse(myRazza.hasNumeroMassimo());
		myRazza.add(new Erbivoro(defaultCoord));
		assertEquals(5, myRazza.size());
		assertTrue(myRazza.hasNumeroMassimo());
	}

	@Test
	public void testExistsDinosauroWithId() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		int startCount = myRazza.size();
		Iterator<Dinosauro> itDinosauri = myRazza.iterator();
		Dinosauro tempDinosauro = null;
		int counter = 0;
		while (counter < 2 && itDinosauri.hasNext()) {
			tempDinosauro = itDinosauri.next();
			counter++;
		}
		// Aggiungo e rimuovo il dinosauro con lo stesso id per verificare che effettivamente l'associazione dinosauro<->id è univoca
		assertTrue(myRazza.existsDinosauroWithId(tempDinosauro.getIdDinosauro()));
		myRazza.removeById(tempDinosauro.getIdDinosauro());
		assertEquals((startCount-1), myRazza.size());
		assertFalse(myRazza.existsDinosauroWithId(tempDinosauro.getIdDinosauro()));
		myRazza.add(tempDinosauro);
		assertEquals(startCount, myRazza.size());
		assertTrue(myRazza.existsDinosauroWithId(tempDinosauro.getIdDinosauro()));
	}

	@Test
	public void testAggiornaPunteggio() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		int initPunteggio = myRazza.getPunteggio();
		Iterator<Dinosauro> itDino = myRazza.iterator();
		assertNotNull(itDino);
		assertTrue(itDino.hasNext());
		Dinosauro tempDinosauro = itDino.next();
		assertNotNull(tempDinosauro);
		// Verifico che il punteggio iniziale sia diverso da quello finale dopo che ho chiamato aggiornaRazza
		tempDinosauro.setEnergiaAttuale(tempDinosauro.getEnergiaMax());
		tempDinosauro.cresci();
		tempDinosauro.setEnergiaAttuale(tempDinosauro.getEnergiaMax());
		myRazza.aggiornaRazza();
		assertFalse(initPunteggio == myRazza.getPunteggio());
	}

	// Helper per far crescere il dinosauro alla dimensione massima
	private void ageDinosauro(Dinosauro tempDinosauro) {
		while (!tempDinosauro.isAtDimensioneMax()) {
			tempDinosauro.setEnergiaAttuale(tempDinosauro.getEnergiaMax());
			tempDinosauro.cresci();
		}
	}

	@Test
	public void testCresciDinosauro() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		Iterator<Dinosauro> itDino = myRazza.iterator();
		assertNotNull(itDino);
		assertTrue(itDino.hasNext());
		Dinosauro tempDinosauro = itDino.next();
		assertNotNull(tempDinosauro);
		ageDinosauro(tempDinosauro);
		try {
			myRazza.cresciDinosauro(tempDinosauro.getIdDinosauro());
		} catch (GenericDinosauroException e) {
			assertEquals("raggiuntaDimensioneMax", e.getMessage());
		}
		assertTrue(itDino.hasNext());
		tempDinosauro = itDino.next();
		assertNotNull(tempDinosauro);
		tempDinosauro.setEnergiaAttuale(1);
		try {
			myRazza.cresciDinosauro(tempDinosauro.getIdDinosauro());
		} catch (GenericDinosauroException e) {
			assertEquals("mortePerInedia", e.getMessage());
		}
	}

	@Test
	public void testDeponiUovo() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		Iterator<Dinosauro> itDino = myRazza.iterator();
		assertNotNull(itDino);
		assertTrue(itDino.hasNext());
		Dinosauro tempDinosauro = itDino.next();
		assertNotNull(tempDinosauro);
		tempDinosauro.setEnergiaAttuale(1);
		try {
			myRazza.deponiUovo(tempDinosauro.getIdDinosauro());
		} catch (GenericDinosauroException e) {
			assertEquals("mortePerInedia", e.getMessage());
		}
		assertFalse(myRazza.hasNumeroMassimo());
		while (!myRazza.hasNumeroMassimo()) {
			myRazza.add(new Erbivoro(new Coord(0, 0)));
		}
		assertTrue(myRazza.hasNumeroMassimo());
		itDino = myRazza.iterator();
		assertNotNull(itDino);
		assertTrue(itDino.hasNext());
		tempDinosauro = itDino.next();
		assertNotNull(tempDinosauro);
		try {
			myRazza.deponiUovo(tempDinosauro.getIdDinosauro());
		} catch (GenericDinosauroException e) {
			assertEquals("raggiuntoNumeroMaxDinosauri", e.getMessage());
		}
	}

	@Test
	public void testMuoviDinosauro() {
		// Aggiungi 3 dinosauri alla razza tramite testAdd
		testAdd();
		assertEquals(3, myRazza.size());
		Iterator<Dinosauro> itDino = myRazza.iterator();
		assertNotNull(itDino);
		assertTrue(itDino.hasNext());
		Dinosauro tempDinosauro = itDino.next();
		assertNotNull(tempDinosauro);
		try {
			myRazza.muoviDinosauro(tempDinosauro.getIdDinosauro(), new Coord(1, 1));
		} catch (GenericDinosauroException e) {}
		tempDinosauro.invecchia();
		try {
			tempDinosauro.setEnergiaAttuale(1);
			myRazza.muoviDinosauro(tempDinosauro.getIdDinosauro(), new Coord(1, 1));
		} catch (GenericDinosauroException e) {
			assertEquals("mortePerInedia", e.getMessage());
		}
	}
}