package dinolib;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LogicaTest {
	private Logica myLogica = null;
	
	private void addSomeUsers() throws UserExistsException {
		myLogica.doCreaUtente("abc", "pwd");
		myLogica.doCreaUtente("abd", "pwd2");
		myLogica.doCreaUtente("abe", "pwd3");
	}
	private void mySetUp() throws Exception {
		setUp();
		addSomeUsers();
	}
	
	@Before
	public void setUp() throws Exception {
		myLogica = new Logica();
	}
	
	private void testLogica() throws Exception {
		setUp();
		assertNotNull(myLogica);
	}
	
	private void testGetLatoDellaMappa() {
		assertNotNull(myLogica.getLatoDellaMappa());
		assertEquals(40, myLogica.getLatoDellaMappa());
	}
	
	private void testExistsUserWithName() {
		assertTrue(myLogica.existsUserWithName("abc"));
		assertTrue(myLogica.existsUserWithName("abd"));
		assertTrue(myLogica.existsUserWithName("abe"));
	}
	public void testGetCella() {
		Cella tempCella = myLogica.getCella(0, 0);
		assertNotNull(tempCella);
	}
	private void testGetPlayerByName() {
		assertNotNull(myLogica.getPlayerByName("abc"));
		assertNotNull(myLogica.getPlayerByName("abd"));
		assertNotNull(myLogica.getPlayerByName("abe"));
		Giocatore gioc1 = myLogica.getPlayerByName("abc");
		Giocatore gioc2 = myLogica.getPlayerByName("abd");
		Giocatore gioc3 = myLogica.getPlayerByName("abc");
		assertSame(gioc1, gioc3);
		assertNotSame(gioc1, gioc2);
	}
	public void testGetPlayerName() {
		Giocatore tempPlayer = myLogica.getPlayerByName("abd");
		assertNotNull(tempPlayer);
		assertEquals("abd", tempPlayer.getNome());
	}
	
	@Test
	public void testAllLogica() throws Exception {
		testLogica();
		testDoCreaUtente();
		testExistsUserWithName();
		testGetLatoDellaMappa();
		testGetCella();
		testGetPlayerByName();
		testGetPlayerName();
		mySetUp();
	}
	
	

	

	@Test
	public void doLoginUtente() {
		
	}
	
	@Test
	public void testIsSomeonePlaying() throws InvalidTokenException, NonInPartitaException, TroppiGiocatoriException, RazzaNonCreataException, InterruptedException, NonAutenticatoException {
		assertFalse(myLogica.isSomeonePlaying());
		assertNotNull(myLogica.doLoginUtente(myLogica.getPlayerByName("abc")));
		myLogica.accediAPartita(myLogica.getPlayerToken("abc"));
		assertTrue(myLogica.isSomeonePlaying());
		myLogica.doEsciDallaPartita(myLogica.getPlayerToken("abc"));
		assertFalse(myLogica.isSomeonePlaying());
	}
	
	@Test
	public void testIsLogicaRunning() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIteratorOnPlayers() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetItDinosauri() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIteratorOnPNames() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddPlayerToConnTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIteratorOnPIds() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsMioTurno() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateNewRaceForPlayer() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsPlayerConnected() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsMaxPlayersInGame() {
		fail("Not yet implemented");
	}


	@Test
	public void testGetPlayerByToken() {
		fail("Not yet implemented");
	}

	@Test
	public void testExistsPlayerWithToken() {
		fail("Not yet implemented");
	}

	@Test
	public void testExistsRaceForPlayer() {
		fail("Not yet implemented");
	}

	@Test
	public void testPlayerHasDinosauro() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsPlayerInGame() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsPlayerLogged() {
		fail("Not yet implemented");
	}

	@Test
	public void testExistsRaceWithName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlayerToken() {
		fail("Not yet implemented");
	}

	@Test
	public void testBroadcastCambioTurno() {
		fail("Not yet implemented");
	}

	@Test
	public void testConfermaTurno() {
		fail("Not yet implemented");
	}

	@Test
	public void testInserisciDinosauriNellaMappa() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoEsciDallaPartita() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoLogout() {
		fail("Not yet implemented");
	}

	@Test
	public void testTrySpawnOfAnEgg() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoSubtraction() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoAddition() {
		fail("Not yet implemented");
	}

	@Test
	public void testAccediAPartita() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeponiUovo() {
		fail("Not yet implemented");
	}

	
}
