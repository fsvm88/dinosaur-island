package dinolib.Razza;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import dinolib.Mappa.Coord;

/**
 * @author  fabio
 */
public class RazzaTest {
	/**
	 * @uml.property  name="specie"
	 * @uml.associationEnd  
	 */
	private Razza myRazza;
	private final Coord defaultCoord = new Coord(0,0);
	private final String defaultType = "erbivoro";

	@Before
	public void setUp() {
		myRazza = new Razza("testRazza", defaultType);
	}

	private void testAdd() {
		assertEquals(0, myRazza.size());
		myRazza.add(new Erbivoro(defaultCoord));
		assertEquals(1, myRazza.size());
		myRazza.add(new Erbivoro(defaultCoord));
		assertEquals(2, myRazza.size());
		myRazza.add(new Erbivoro(defaultCoord));
		assertEquals(3, myRazza.size());
	}

	private void testRemove() {
		assertEquals(0, myRazza.size());
		Iterator<Dinosauro> itDinosauro = myRazza.iterator();
		if (itDinosauro.hasNext()) {
			Dinosauro tempDinosauro = itDinosauro.next();
			assertNotNull(tempDinosauro);
		}
		itDinosauro.remove();
		assertEquals(0, myRazza.size());
		setUp();
	}

	private void testIterator() {
		Iterator<Dinosauro> itDinosauri = myRazza.iterator();
		assertNotNull(itDinosauri);
		assertNotNull(itDinosauri.hasNext());
		assertNotNull(itDinosauri.next());
	}

	private void testSize() {
		assertEquals(0, myRazza.size());
	}

	private void testIsEmpty() {
		assertFalse(myRazza.isEmpty());
		myRazza.clear();
		assertTrue(myRazza.isEmpty());
	}

	private void testClear() {
		assertEquals(1, myRazza.size());
		myRazza.clear();
		assertEquals(0, myRazza.size());
		setUp();
	}

	private void testContains() {
		Iterator<Dinosauro> itDinosauri = myRazza.iterator();
		if (itDinosauri.hasNext()) {
			Dinosauro tempDinosauro = itDinosauri.next();
			assertTrue(myRazza.contains(tempDinosauro));
			itDinosauri.remove();
			assertFalse(myRazza.contains(tempDinosauro));
		}
	}

	@Test
	public void testInterface() {
		testSize();
		testAdd();
		testClear();
		testAdd();
		testIterator();
		testRemove();
		testIsEmpty();
		testAdd();
		testContains();
		testRemoveById();
	}

	private void testRemoveById() {
		int startCount = myRazza.size();
		Iterator<Dinosauro> itDinosauri = myRazza.iterator();
		Dinosauro tempDinosauro = null;
		int counter = 0;
		while (counter < 2 && itDinosauri.hasNext()) {
			tempDinosauro = itDinosauri.next();
			counter++;
		}
		myRazza.removeById(tempDinosauro.getIdDinosauro());
		assertEquals((startCount-1), myRazza.size());
		myRazza.add(tempDinosauro);
		assertEquals(startCount, myRazza.size());
	}

	private void testGetTipo() {
		assertNotNull(myRazza.getTipo());
		assertEquals("Erbivoro", myRazza.getTipo());
		setUp();
	}

	private void testEstinzione() {
		assertFalse(myRazza.isEmpty());
		Iterator<Dinosauro> itDinosauro = myRazza.iterator();
		if (itDinosauro.hasNext()) {
			Dinosauro tempDinosauro = itDinosauro.next();
			myRazza.remove(tempDinosauro);
			assertEquals(0, myRazza.size());
		}
		assertTrue(myRazza.isEmpty());
		setUp();
	}

	private void testGetDinosauroById() {
		int startCount = myRazza.size();
		Iterator<Dinosauro> itDinosauri = myRazza.iterator();
		Dinosauro tempDinosauro = null;
		int counter = 0;
		while (counter < 2 && itDinosauri.hasNext()) {
			tempDinosauro = itDinosauri.next();
			counter++;
		}
		assertNotNull(myRazza.getDinosauroById(tempDinosauro.getIdDinosauro()));
		myRazza.removeById(tempDinosauro.getIdDinosauro());
		assertEquals((startCount-1), myRazza.size());
		assertNull(myRazza.getDinosauroById(tempDinosauro.getIdDinosauro()));
		myRazza.add(tempDinosauro);
		assertEquals(startCount, myRazza.size());
		assertNotNull(myRazza.getDinosauroById(tempDinosauro.getIdDinosauro()));
	}
	
	private void testHasNumeroMassimo() {
		assertFalse(myRazza.hasNumeroMassimo());
		myRazza.add(new Erbivoro(defaultCoord));
		assertEquals(5, myRazza.size());
		assertTrue(myRazza.hasNumeroMassimo());
	}
	
	private void testExistsDinosauroWithId() {
		int startCount = myRazza.size();
		Iterator<Dinosauro> itDinosauri = myRazza.iterator();
		Dinosauro tempDinosauro = null;
		int counter = 0;
		while (counter < 2 && itDinosauri.hasNext()) {
			tempDinosauro = itDinosauri.next();
			counter++;
		}
		assertTrue(myRazza.existsDinosauroWithId(tempDinosauro.getIdDinosauro()));
		myRazza.removeById(tempDinosauro.getIdDinosauro());
		assertEquals((startCount-1), myRazza.size());
		assertFalse(myRazza.existsDinosauroWithId(tempDinosauro.getIdDinosauro()));
		myRazza.add(tempDinosauro);
		assertEquals(startCount, myRazza.size());
		assertTrue(myRazza.existsDinosauroWithId(tempDinosauro.getIdDinosauro()));
	}
	
	private void testAggiornaPunteggio() {
		int initPunteggio = myRazza.getPunteggio();
		Iterator<Dinosauro> itDino = myRazza.iterator();
		Dinosauro tempDinosauro = itDino.next();
		tempDinosauro.setEnergiaAttuale(tempDinosauro.getEnergiaMax());
		tempDinosauro.cresci();
		tempDinosauro.setEnergiaAttuale(tempDinosauro.getEnergiaMax());
		myRazza.aggiornaRazza();
		assertFalse(initPunteggio == myRazza.getPunteggio());
	}
	
	@Test
	public void testMiscellanea() {
		testGetTipo();
		testGetDinosauroById();
		testEstinzione();
		testRemoveById();
		testHasNumeroMassimo();
		testExistsDinosauroWithId();
		testAggiornaPunteggio();
	}
}