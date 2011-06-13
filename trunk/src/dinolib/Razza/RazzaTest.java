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
	/**
	 * @uml.property  name="specie"
	 * @uml.associationEnd  
	 */
	private Razza myRazza;
	private final Coord defaultCoord = new Coord(0,0);
	private final Character defaultType = 'e';

	@Before
	public void setUp() {
		myRazza = new Razza("testRazza", defaultType.charValue());
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
		assertEquals(3, myRazza.size());
		Iterator<Dinosauro> itDinosauro = myRazza.iterator();
		if (itDinosauro.hasNext()) {
			Dinosauro tempDinosauro = itDinosauro.next();
			assertNotNull(tempDinosauro);
		}
		itDinosauro.remove();
		assertEquals(2, myRazza.size());
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
		assertEquals(2, myRazza.size());
		assertFalse(myRazza.isEmpty());
		myRazza.clear();
		assertTrue(myRazza.isEmpty());
	}

	private void testClear() {
		assertEquals(3, myRazza.size());
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

	private void testGetTipo() {
		assertNotNull(myRazza.getTipo());
		assertEquals('e', myRazza.getTipo().charValue());
		setUp();
	}

	private void testEstinzione() {
		assertFalse(myRazza.isEmpty());
		Dinosauro tempDinosauro = null;
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
		assertEquals(0, myRazza.size());
		assertTrue(myRazza.isEmpty());
		setUp();
	}

	private void testGetDinosauroById() {
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
		assertEquals(4, myRazza.size());
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

	private void ageDinosauro(Dinosauro tempDinosauro) {
		while (!tempDinosauro.isAtDimensioneMax()) {
			tempDinosauro.setEnergiaAttuale(tempDinosauro.getEnergiaMax());
			tempDinosauro.cresci();
		}
	}

	private void testCresciDinosauro() {
		Iterator<Dinosauro> itDino = myRazza.iterator();
		Dinosauro tempDinosauro = itDino.next();
		ageDinosauro(tempDinosauro);
		try {
			myRazza.cresciDinosauro(tempDinosauro.getIdDinosauro());
		} catch (GenericDinosauroException e) {
			assertEquals("raggiuntaDimensioneMax", e.getMessage());
		}
		tempDinosauro = itDino.next();
		tempDinosauro.setEnergiaAttuale(1);
		try {
			myRazza.cresciDinosauro(tempDinosauro.getIdDinosauro());
		} catch (GenericDinosauroException e) {
			assertEquals("mortePerInedia", e.getMessage());
		}
	}

	private void testDeponiUovo() {
		Iterator<Dinosauro> itDino = myRazza.iterator();
		Dinosauro tempDinosauro = itDino.next();
		tempDinosauro = itDino.next();
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
		Iterator<Dinosauro> itDino2 = myRazza.iterator();
		Dinosauro tempDinosauro2 = itDino2.next();
		try {
			myRazza.deponiUovo(tempDinosauro2.getIdDinosauro());
		} catch (GenericDinosauroException e) {
			assertEquals("raggiuntoNumeroMaxDinosauri", e.getMessage());
		}
	}

	private void testMuoviDinosauro() {
		Iterator<Dinosauro> itDino = myRazza.iterator();
		Dinosauro tempDinosauro = itDino.next();
		try {
			myRazza.muoviDinosauro(tempDinosauro.getIdDinosauro(), new Coord(1, 1));
		} catch (GenericDinosauroException e) {}
		try {
			tempDinosauro.setEnergiaAttuale(1);
			myRazza.muoviDinosauro(tempDinosauro.getIdDinosauro(), new Coord(1, 1));
		} catch (GenericDinosauroException e) {
			assertEquals("mortePerInedia", e.getMessage());
		}

	}

	@Test
	public void testMiscellanea() {
		setUp();
		testGetTipo();
		testAdd();
		testGetDinosauroById();
		testEstinzione();
		testAdd();
		testRemoveById();
		testHasNumeroMassimo();
		testExistsDinosauroWithId();
		testAggiornaPunteggio();
		testCresciDinosauro();
		testDeponiUovo();
		testMuoviDinosauro();
	}
}