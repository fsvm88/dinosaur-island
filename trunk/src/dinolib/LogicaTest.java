package dinolib;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dinolib.Exceptions.InvalidTokenException;
import dinolib.Exceptions.NomeRazzaOccupatoException;
import dinolib.Exceptions.RazzaNonCreataException;
import dinolib.Exceptions.TroppiGiocatoriException;
import dinolib.Exceptions.UserAuthenticationFailedException;
import dinolib.Exceptions.UserExistsException;

public class LogicaTest {
	private Logica logicaTest = null;
	private String testingToken = null;
	private String testingToken2 = null;
	private String testingToken3 = null;
	
	@Before
	public void setUp() {
		logicaTest = new Logica();
	}

	private void testCostruttore() {
		setUp();
		assertNotNull(logicaTest);
	}

	private void testObjectsGetters() {
		assertTrue(logicaTest.isLogicaRunning());
		assertNotNull(logicaTest.getCMan());
		assertNotNull(logicaTest.getPMan());
		assertNotNull(logicaTest.getRRSched());
		assertNotNull(logicaTest.getMappa());
	}

	private void testDoCreaUtente() {
		try {
			assertTrue(logicaTest.doCreaUtente("abc", "myPwd"));
		}
		catch (UserExistsException e) {
			fail();
		}
		try {
			assertFalse(logicaTest.doCreaUtente("abc", "myPwd"));
		}
		catch (UserExistsException e) { System.out.println("Eccezione UserExists gestita correttamente."); }
	}

	private void testDoLogin() {
		try {
			assertFalse(logicaTest.doLogin("abE", "nnn"));
		} catch (UserAuthenticationFailedException e) {}
		try {
			assertTrue(logicaTest.doLogin("abc", "mmm"));
		} catch (UserAuthenticationFailedException e) {}
		try {
			assertTrue(logicaTest.doLogin("abc", "myPwd"));
		} catch (UserAuthenticationFailedException e) { fail(); }	
	}

	private void testDoCreaRazza() {
		try {
			logicaTest.doCreaUtente("abd", "myPwd2");
			logicaTest.doLogin("abd", "myPwd2");
			testingToken2 = logicaTest.getCMan().getToken("abd");
			System.out.println("testingToken2: " + testingToken2);
		}
		catch (UserExistsException e) { fail(); }
		catch (UserAuthenticationFailedException e) { fail(); }

		try {
			assertTrue(logicaTest.doCreaRazza(testingToken, "miaRazza1", 'c'));
			assertFalse(logicaTest.doCreaRazza(testingToken, "mbc", 'e'));
			assertFalse(logicaTest.doCreaRazza(testingToken2, "miaRazza1", 'e'));
		}
		catch (NomeRazzaOccupatoException e) { System.out.println("Eccezione NomeRazzaOccupato gestita correttamente."); }
		catch (InvalidTokenException e) { System.out.println("Eccezione InvalidToken gestita correttamente."); }
		System.out.println("eseguo dopo la prima eccezione");
		try {
			assertTrue(logicaTest.doCreaRazza("298375892735", "bbb", 'd'));
		}
		catch (NomeRazzaOccupatoException e) { System.out.println("Eccezione NomeRazzaOccupato gestita correttamente."); }
		catch (InvalidTokenException e) { System.out.println("Eccezione InvalidToken gestita correttamente."); }
	}

	private void testIsPlayerLogged() {
		try {
			assertTrue(logicaTest.isPlayerLogged(testingToken));
			assertTrue(logicaTest.isPlayerLogged(testingToken2));
		}
		catch (InvalidTokenException e) { }
		try {
			assertFalse(logicaTest.isPlayerLogged("zzz"));
		}
		catch (InvalidTokenException e) { System.out.println("Eccezione InvalidToken gestita correttamente."); }
	}

	private void testGetPlayerByToken() {
		try {
			assertNotNull(logicaTest.getPlayerByToken(testingToken));
			assertNotNull(logicaTest.getPlayerByToken(testingToken2));
			assertNull(logicaTest.getPlayerByToken("zzz"));
		}
		catch (InvalidTokenException e) { System.out.println("Eccezione InvalidToken gestita correttamente."); }
	}

	private void testDoAccessoPartita() {
		/* Testa token non valido */
		try {
			assertFalse(logicaTest.doAccessoPartita("zzzz"));
		}
		catch (InvalidTokenException e) { System.out.println("Eccezione InvalidToken gestita correttamente."); }
		catch (InterruptedException e) { }
		catch (TroppiGiocatoriException e) { System.out.println("Eccezione TroppiGiocatoriException gestita correttamente."); }
		catch (RazzaNonCreataException e) { System.out.println("Eccezione RazzaNonCreataException gestita correttamente."); }

		/* Testa token valido e condizioni valide */
		try {
			assertTrue(logicaTest.doAccessoPartita(testingToken));
		}
		catch (InvalidTokenException e) { System.out.println("Eccezione InvalidToken gestita correttamente."); }
		catch (InterruptedException e) { }
		catch (TroppiGiocatoriException e) { System.out.println("Eccezione TroppiGiocatoriException gestita correttamente."); }
		catch (RazzaNonCreataException e) { System.out.println("Eccezione RazzaNonCreataException gestita correttamente."); }

		/* Aggiungi un po' di utenti per i test successivi */
		try {
			assertTrue(logicaTest.doCreaUtente("abe", "myPwd3"));
			assertTrue(logicaTest.doLogin("abe", "myPwd3"));
			assertTrue(logicaTest.doCreaUtente("abf", "myPwd4"));
			assertTrue(logicaTest.doLogin("abf", "myPwd4"));
			assertTrue(logicaTest.doCreaUtente("abg", "myPwd5"));
			assertTrue(logicaTest.doLogin("abg", "myPwd5"));
			assertTrue(logicaTest.doCreaUtente("abh", "myPwd6"));
			assertTrue(logicaTest.doLogin("abh", "myPwd6"));
			assertTrue(logicaTest.doCreaUtente("abi", "myPwd7"));
			assertTrue(logicaTest.doLogin("abi", "myPwd7"));
			assertTrue(logicaTest.doCreaUtente("abj", "myPwd8"));
			assertTrue(logicaTest.doLogin("abj", "myPwd8"));
			assertTrue(logicaTest.doCreaUtente("abk", "myPwd9"));
			assertTrue(logicaTest.doLogin("abk", "myPwd9"));
			assertTrue(logicaTest.doCreaUtente("abl", "myPwd10"));
			assertTrue(logicaTest.doLogin("abl", "myPwd10"));
			assertTrue(logicaTest.doCreaUtente("abm", "myPwd11"));
			assertTrue(logicaTest.doLogin("abm", "myPwd11"));
			assertTrue(logicaTest.doCreaUtente("abn", "myPwd12"));
			assertTrue(logicaTest.doLogin("abn", "myPwd12"));
			testingToken3 = logicaTest.getCMan().getToken("abe");
		}
		catch (UserExistsException e) { }
		catch (UserAuthenticationFailedException e) { }

		/* Testa razza non creata */
		try {
			assertTrue(logicaTest.doAccessoPartita(testingToken3));
		}
		catch (InvalidTokenException e) { System.out.println("Eccezione InvalidToken gestita correttamente."); }
		catch (InterruptedException e) { }
		catch (TroppiGiocatoriException e) { System.out.println("Eccezione TroppiGiocatoriException gestita correttamente."); }
		catch (RazzaNonCreataException e) { System.out.println("Eccezione RazzaNonCreataException gestita correttamente."); }

		/* Crea le razze per tutti i giocatori di modo da testare la condizione "troppi giocatori" */
		try {
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abe"), "miaRazza3", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abf"), "miaRazza4", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abg"), "miaRazza5", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abh"), "miaRazza6", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abi"), "miaRazza7", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abj"), "miaRazza8", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abk"), "miaRazza9", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abl"), "miaRazza10", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abm"), "miaRazza11", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abn"), "miaRazza12", 'c'));
		}
		catch (NomeRazzaOccupatoException e) { System.out.println("Eccezione NomeRazzaOccupato gestita correttamente."); }
		catch (InvalidTokenException e) { System.out.println("Eccezione InvalidToken gestita correttamente."); }

		/* Testa troppi giocatori */
		try {
			assertTrue(logicaTest.doAccessoPartita(testingToken3));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abf")));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abg")));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abh")));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abi")));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abj")));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abk")));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abl")));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abm")));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abn")));
		}
		catch (InvalidTokenException e) { System.out.println("Eccezione InvalidToken gestita correttamente."); }
		catch (InterruptedException e) { }
		catch (TroppiGiocatoriException e) { System.out.println("Eccezione TroppiGiocatoriException gestita correttamente."); }
		catch (RazzaNonCreataException e) { System.out.println("Eccezione RazzaNonCreataException gestita correttamente."); }
	}

	private void testIsPlayerInGame() {
		try {
			assertTrue(logicaTest.isPlayerInGame(testingToken));
			assertFalse(logicaTest.isPlayerInGame(testingToken2));
			assertTrue(logicaTest.isPlayerInGame(testingToken3));
			assertTrue(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abf")));
			assertTrue(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abg")));
			assertTrue(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abh")));
			assertTrue(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abi")));
			assertTrue(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abj")));
			assertTrue(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abk")));
			assertFalse(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abl")));
			assertFalse(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abm")));
			assertFalse(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abn")));
		}
		catch (InvalidTokenException e) { }
	}


	@Test
	public void testAll() throws Exception {
		testCostruttore();
		testObjectsGetters();
		testDoCreaUtente();
		testDoLogin();
		testingToken = logicaTest.getCMan().getToken("abc");
		System.out.println("testingToken: " + testingToken);
		testDoCreaRazza();
		testIsPlayerLogged();
		testGetPlayerByToken();
		testDoAccessoPartita();
		testIsPlayerInGame();
	}

	// TODO
	/* existsDinosauroWithId
	 * isPlayerInGame
	 * isMioTurno
	 * getPlayerByIdDinosauro
	 * updatePartita
	 * doDeponiUovo
	 * doUscitaPartita
	 * doLogout
	 * doMuoviDinosauro
	 */
}