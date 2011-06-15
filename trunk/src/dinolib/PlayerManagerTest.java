package dinolib;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
		assertTrue(pMan.hasNext());
		while ()
	}
	
	@Test
	public void testAll() {
		testCostruttore();
		testAddExists();
		testGetPlayer();
		testIterator();
	}
}