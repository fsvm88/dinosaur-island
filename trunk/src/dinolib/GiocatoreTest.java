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
	
	private void testGiocatore() {
		setUp();
		assertNotNull(testGiocatore);
	}
	
	private void testGetNome() {
		assertEquals("abc", testGiocatore.getNome());
		assertFalse("abd".equals(testGiocatore.getNome()));
	}
	
	private void testPasswordIsValid() {
		assertTrue(testGiocatore.passwordIsValid("passWord"));
		assertFalse(testGiocatore.passwordIsValid("password"));
	}
	
	private void testGetPunteggio() {
		assertNotNull(testGiocatore.getPunteggio());
	}
	
	private void testCreaRazza() {
		assertNull(testGiocatore.getRazza());
		testGiocatore.creaNuovaRazza("myRazza", 'c');
		assertNotNull(testGiocatore.getRazza());
		assertEquals("myRazza", testGiocatore.getRazza().getNome());
		assertEquals('c', testGiocatore.getRazza().getTipo().charValue());
	}
	
	private void testHasRazza() {
		assertFalse(testGiocatore.hasRazza());
		testGiocatore.getRazza().add(new Carnivoro(new Coord(0,0)));
		assertTrue(testGiocatore.hasRazza());
	}
	
	private void testAggiorna() {
		assertEquals(0, testGiocatore.getPunteggio().getPunteggioDaNome("myRazza").intValue());
		testGiocatore.aggiorna();
		assertEquals(2, testGiocatore.getPunteggio().getPunteggioDaNome("myRazza").intValue());
	}
	
	@Test
	public void testAll() {
		testGiocatore();
		testGetNome();
		testPasswordIsValid();
		testGetPunteggio();
		testCreaRazza();
		testHasRazza();
		testAggiorna();
	}
}