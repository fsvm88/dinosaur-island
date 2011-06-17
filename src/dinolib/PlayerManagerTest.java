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
	
	@Test
	public void testCostruttore() {
		assertNotNull(pMan);
	}
	
	@Test
	public void testAddExists() {
		pMan.add(new Giocatore("abc", "pass"));
		assertTrue(pMan.exists("abc"));
		assertFalse(pMan.exists("abd"));
	}
	
	@Test
	public void testGetPlayer() {
		testAddExists();
		assertNotNull(pMan.getPlayer("abc"));
		assertNull(pMan.getPlayer("abd"));
	}
	
	@Test
	public void testIterator() {
		testAddExists();
		pMan.add(new Giocatore("abe", "pass"));
		pMan.add(new Giocatore("abf", "pass"));
		assertNotNull(pMan.iterator());
		Iterator<Giocatore> itP = pMan.iterator();
		assertTrue(itP.hasNext());
		assertNotNull(itP.next());
		assertTrue(itP.hasNext());
		assertNotNull(itP.next());
		assertTrue(itP.hasNext());
		assertNotNull(itP.next());
		assertFalse(itP.hasNext());
	}
	
	@Test
	public void testContains() {
		// Crea almeno un giocatore
		testAddExists();
		Iterator<Giocatore> itP = pMan.iterator();
		assertTrue(itP.hasNext());
		Giocatore tmpGioc = itP.next();
		assertNotNull(tmpGioc);
		assertTrue(pMan.contains(tmpGioc));
		assertFalse(pMan.contains(new Giocatore("abi", "pass")));
	}
	
	@Test
	public void testAggiorna() {
		// Crea più di un giocatore
		testIterator();
		Giocatore tmpGioc = pMan.iterator().next();
		assertNotNull(tmpGioc);
		tmpGioc.creaNuovaRazza("prova", 'c');
		assertNotNull(tmpGioc.getRazza());
		tmpGioc.getRazza().add(new Carnivoro(new Coord(12,23)));
		int curPunteggio = tmpGioc.getPunteggio().getPunteggioDaNome("prova");
		assertEquals(0, curPunteggio);
		pMan.aggiorna();
		assertEquals(2, tmpGioc.getPunteggio().getPunteggioDaNome("prova").intValue());
	}
}