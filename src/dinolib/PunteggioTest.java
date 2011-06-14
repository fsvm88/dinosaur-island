package dinolib;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class PunteggioTest {
	private Punteggio myPunteggio = null;
	
	@Before
	public void setUp() {
		myPunteggio = new Punteggio();
	}
	
	private void testCostruttore() {
		myPunteggio = null;
		assertNull(myPunteggio);
		setUp();
		assertNotNull(myPunteggio);
	}
	
	private void testUpdatePunteggio() {
		assertFalse(myPunteggio.updatePunteggio(null, null));
		assertFalse(myPunteggio.updatePunteggio("myP", null));
		assertFalse(myPunteggio.updatePunteggio("myP", 0));
		assertTrue(myPunteggio.updatePunteggio("myP", 45));
		assertTrue(myPunteggio.updatePunteggio("myP", 87));
		assertTrue(myPunteggio.updatePunteggio("myQ", 97));
		assertTrue(myPunteggio.updatePunteggio("myR", 64));
	}
	
	private void testIterator() {
		Iterator<String> itP = myPunteggio.iterator();
		assertNotNull(itP);
		assertNotNull(itP.hasNext());
		assertNotNull(itP.next());
	}
	
	private void testGetPunteggioDaNome() {
		assertEquals(87, myPunteggio.getPunteggioDaNome("myP").intValue());
		assertEquals(97, myPunteggio.getPunteggioDaNome("myQ").intValue());
		assertEquals(64, myPunteggio.getPunteggioDaNome("myR").intValue());
	}
	
	@Test
	public void testAll() {
		testCostruttore();
		testUpdatePunteggio();
		testIterator();
		testGetPunteggioDaNome();
	}
}