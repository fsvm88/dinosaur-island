package dinolib.Razza;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

/**
 * @author  fabio
 */
public class RazzaTest {
	/**
	 * @uml.property  name="specie"
	 * @uml.associationEnd  
	 */
	private Razza myRazza;
	
	@Before
	public void setUp() {
		myRazza = new Razza("testRazza", new Erbivoro(0,0));
	}
	
	private void testAdd() {
		assertEquals(1, myRazza.size());
		myRazza.add(new Erbivoro(0,0));
		assertEquals(2, myRazza.size());
		setUp();
	}
	
	private void testRemove() {
		assertEquals(1, myRazza.size());
		Iterator<Dinosauro> itDinosauro = myRazza.iterator();
		if (itDinosauro.hasNext()) {
			Dinosauro tempDinosauro = itDinosauro.next();
			myRazza.remove(tempDinosauro);
			assertEquals(0, myRazza.size());
		}
		setUp();
	}
	
	private void testIterator() {
		Iterator<Dinosauro> itDinosauri = myRazza.iterator();
		assertNotNull(itDinosauri);
		assertNotNull(itDinosauri.hasNext());
		assertNotNull(itDinosauri.next());
		setUp();
	}
	
	private void testSize() {
		assertEquals(1, myRazza.size());
		setUp();
	}
	
	private void testIsEmpty() {
		assertFalse(myRazza.isEmpty());
		myRazza.clear();
		assertTrue(myRazza.isEmpty());
		setUp();
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
			myRazza.remove(tempDinosauro);
			assertFalse(myRazza.contains(tempDinosauro));
		}
		setUp();
	}
	
	@Test
	public void testInterface() {
		testSize();
		testClear();
		testAdd();
		testIterator();
	/*	testAddNoDuplicatesAllowed(); */ // TODO trovare un modo per testare la non-duplicazione degli elementi da parte di Add.
		testRemove();
		testIsEmpty();
		testContains();
	}
	
	private void testGetDinosauroById() {
		assertEquals(1, myRazza.size());
		myRazza.add(new Erbivoro(0,0));
		myRazza.add(new Erbivoro(0,0));
		myRazza.add(new Erbivoro(0,0));
		assertEquals(4, myRazza.size());
		Iterator<Dinosauro> itDinosauri = myRazza.iterator();
		Dinosauro tempDinosauro = null;
		int counter = 0;
		while (counter < 2 && itDinosauri.hasNext()) {
			tempDinosauro = itDinosauri.next();
			counter++;
		}
		myRazza.removeById(tempDinosauro.getIdDinosauro());
		assertEquals(3, myRazza.size());
		setUp();
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
	
	private void testRemoveById() {
		Iterator<Dinosauro> itDinosauro = myRazza.iterator();
		if (itDinosauro.hasNext()) {
			Dinosauro tempDinosauro = itDinosauro.next();
			myRazza.removeById(tempDinosauro.getIdDinosauro());
			assertEquals(0, myRazza.size());
		}
		setUp();
	}
	
	@Test
	public void testMiscellanea() {
		testGetTipo();
		testGetDinosauroById();
		testEstinzione();
		testRemoveById();
	}
}
