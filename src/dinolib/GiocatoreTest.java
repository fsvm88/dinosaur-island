package dinolib;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dinolib.Mappa.*;
import dinolib.Razza.Carnivoro;

public class GiocatoreTest {
	private Giocatore testGiocatore;
	
	@Before
	public void setUp() {
		testGiocatore = new Giocatore("abc", "passWord");
	}
	
	@Test
	public void testCostruttore() {
		assertNotNull(testGiocatore);
	}
	
	@Test
	public void testGetNome() {
		assertEquals("abc", testGiocatore.getNome());
		assertFalse("abd".equals(testGiocatore.getNome()));
	}
	
	@Test
	public void testPasswordIsValid() {
		assertTrue(testGiocatore.passwordIsValid("passWord"));
		assertFalse(testGiocatore.passwordIsValid("password"));
	}
	
	@Test
	public void testGetPunteggio() {
		assertNotNull(testGiocatore.getPunteggio());
	}
	
	@Test
	public void testCreaRazza() {
		assertNull(testGiocatore.getRazza());
		testGiocatore.creaNuovaRazza("myRazza", 'c');
		assertNotNull(testGiocatore.getRazza());
		assertEquals("myRazza", testGiocatore.getRazza().getNome());
		assertEquals('c', testGiocatore.getRazza().getTipo().charValue());
	}
	
	@Test
	public void testHasRazza() {
		assertFalse(testGiocatore.hasRazza());
		testCreaRazza();
		testGiocatore.getRazza().add(new Carnivoro(new Coord(0,0)));
		assertTrue(testGiocatore.hasRazza());
	}
	
	@Test
	public void testAggiorna() {
		testHasRazza();
		assertEquals(0, testGiocatore.getPunteggio().getPunteggioDaNome("myRazza").intValue());
		testGiocatore.aggiorna();
		assertEquals(2, testGiocatore.getPunteggio().getPunteggioDaNome("myRazza").intValue());
	}
}