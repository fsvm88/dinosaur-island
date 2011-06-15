package dinolib;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import dinolib.Mappa.Coord;
import dinolib.Razza.Carnivoro;

public class PlayerManagerTest {
	private PlayerManager pMan = null;
	
	@Before
	public void setUp() {
		pMan = new PlayerManager();
	}
	
	private void testCostruttore() {
		setUp();
		assertNotNull(pMan);
	}
	
	private void testAddExists() {
		pMan.add(new Giocatore("abc", "pass"));
		assertTrue(pMan.exists("abc"));
		assertFalse(pMan.exists("abd"));
	}
	
	private void testGetPlayer() {
		assertNotNull(pMan.getPlayer("abc"));
		assertNull(pMan.getPlayer("abd"));
	}
	
	private void testIterator() {
		pMan.add(new Giocatore("abe", "pass"));
		pMan.add(new Giocatore("abf", "pass"));
		pMan.add(new Giocatore("abg", "pass"));
		pMan.add(new Giocatore("abh", "pass"));
		assertNotNull(pMan.iterator());
		Iterator<Giocatore> itP = pMan.iterator();
		assertTrue(itP.hasNext());
		while (itP.hasNext()) {
			assertNotNull(itP.next());
		}
	}
	
	private void testContains() {
		Iterator<Giocatore> itP = pMan.iterator();
		itP.next();
		itP.next();
		Giocatore tmpGioc = itP.next();
		assertTrue(pMan.contains(tmpGioc));
		assertFalse(pMan.contains(new Giocatore("abi", "pass")));
	}
	
	private void testAggiorna() {
		Giocatore tmpGioc = pMan.iterator().next();
		assertNotNull(tmpGioc);
		tmpGioc.creaNuovaRazza("prova", 'c');
		assertNotNull(tmpGioc.getRazza());
		tmpGioc.getRazza().add(new Carnivoro(new Coord(0,0)));
		int curPunteggio = tmpGioc.getPunteggio().getPunteggioDaNome("prova");
		assertEquals(0, curPunteggio);
		pMan.aggiorna();
		assertEquals(2, tmpGioc.getPunteggio().getPunteggioDaNome("prova").intValue());
	}
	
	@Test
	public void testAll() {
		testCostruttore();
		testAddExists();
		testGetPlayer();
		testIterator();
		testContains();
		testAggiorna();
	}
}