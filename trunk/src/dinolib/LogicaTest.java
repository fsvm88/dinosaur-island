package dinolib;

import static org.junit.Assert.*;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

public class LogicaTest {
	private Logica myLogica = null;
	private Hashtable<String, Giocatore> testGiocatori = null;
	
	private void addSomeUsers() throws UserExistsException {
		myLogica.doCreaUtente("abc", "pwd");
		myLogica.doCreaUtente("abd", "pwd2");
		myLogica.doCreaUtente("abe", "pwd3");
		testGiocatori.put(CommonUtils.getNewToken(), myLogica.getPlayerByName("abc"));
		testGiocatori.put(CommonUtils.getNewToken(), myLogica.getPlayerByName("abd"));
		testGiocatori.put(CommonUtils.getNewToken(), myLogica.getPlayerByName("abe"));
	}
	private void mySetUp() throws Exception {
		setUp();
		addSomeUsers();
	}
	
	@Before
	public void setUp() throws Exception {
		myLogica = new Logica();
		testGiocatori = new Hashtable<String, Giocatore>();
	}
	
	private void testLogica() throws Exception {
		setUp();
		assertNotNull(myLogica);
	}
	
	private void testGetLatoDellaMappa() {
		assertNotNull(myLogica.getLatoDellaMappa());
		assertEquals(40, myLogica.getLatoDellaMappa());
	}
	private void testGetCella() {
		Cella tempCella = myLogica.getCella(0, 0);
		assertNotNull(tempCella);
	}
	
	private void testExistsPlayerWithName() {
		assertTrue(myLogica.existsPlayerWithName("abc"));
		assertFalse(myLogica.existsPlayerWithName("abf"));
	}
	
	private void testGetPlayerByName() {
		assertNotNull(myLogica.existsPlayerWithName("abc"));
		assertNull(myLogica.existsPlayerWithName("abf"));
	}
	
	private void testGetIteratorS() {
		assertNotNull(myLogica.getIteratorOnPIds());
		assertNotNull(myLogica.getIteratorOnPlayers());
	}
	
	private void testDoLoginUtente() {
		assertFalse(myLogica.getPlayerByName("abc").isLogged());
		assertFalse(myLogica.getPlayerByName("abd").isLogged());
		assertFalse(myLogica.getPlayerByName("abe").isLogged());
		

	}
	
	@Test
	public void testAllLogica() throws Exception {
		testLogica();
		testGetLatoDellaMappa();
		testGetCella();
		mySetUp();
		testExistsPlayerWithName();
		testGetPlayerByName();
		testGetIteratorS();
		testDoLoginUtente();
		
	}
}