package dinolib;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import dinolib.Exceptions.GenericDinosauroException;
import dinolib.Exceptions.InvalidTokenException;
import dinolib.Exceptions.NomeRazzaOccupatoException;
import dinolib.Exceptions.RazzaNonCreataException;
import dinolib.Exceptions.TroppiGiocatoriException;
import dinolib.Exceptions.UserAuthenticationFailedException;
import dinolib.Exceptions.UserExistsException;
import dinolib.Mappa.Coord;
import dinolib.Razza.Dinosauro;

public class LogicaTest {
	private Logica logicaTest = null;
	private String testingToken;
	private String testingToken2;
	private String testingToken3;

	// Prima di eseguire i test istanzio logica e pulisco i testingToken dalle vecchie iterazioni dei test.
	@Before
	public void setUp() {
		logicaTest = new Logica();
		testingToken = null;
		testingToken2 = null;
		testingToken3 = null;
	}

	// Helper per aggiungere due utenti e ottenere testingToken e testingToken2
	// Questi statement sono già testati con altri test, ma per sicurezza li rifaccio.
	// Non tollero eccezioni dato che logicaTest è un riferimento nuovo.
	private void aggiungiDueUtenti() {
		try {
			assertTrue(logicaTest.doCreaUtente("abc", "myPwd"));
			assertTrue(logicaTest.doLogin("abc", "myPwd"));
			testingToken = logicaTest.getCMan().getToken("abc");
			assertNotNull(testingToken);
			assertTrue(logicaTest.doCreaUtente("abd", "myPwd2"));
			assertTrue(logicaTest.doLogin("abd", "myPwd2"));
			testingToken2 = logicaTest.getCMan().getToken("abd");
			assertNotNull(testingToken2);
		}
		catch (UserExistsException e) { fail(); }
		catch (UserAuthenticationFailedException e) { fail(); }
	}

	// Testo il costruttore, che non mi dia un valore nullo
	@Test
	public void testCostruttore() {
		assertNotNull(logicaTest);
	}

	// Testo i getter per gli altri oggetti istanziati da Logica, che non siano nulli (e che logica si avviata -> isLogicaRunning())
	@Test
	public void testObjectsGetters() {
		assertTrue(logicaTest.isLogicaRunning());
		assertNotNull(logicaTest.getCMan());
		assertNotNull(logicaTest.getPMan());
		assertNotNull(logicaTest.getRRSched());
		assertNotNull(logicaTest.getMappa());
	}

	// Testo la creazione di un utente
	@Test
	public void testDoCreaUtente() {
		// Prima testo il caso senza errori, se il test fallisce qui c'è qualcosa di molto sbagliato (catch), non posso proseguire oltre.
		try {
			assertTrue(logicaTest.doCreaUtente("abc", "myPwd"));
		}
		catch (UserExistsException e) { fail(); }
		// Poi provo a crearne un altro con lo stesso nome: operazione non permessa -> mi aspetto il lancio di un'eccezione che gestisco.
		try {
			assertFalse(logicaTest.doCreaUtente("abc", "myPwd"));
		}
		catch (UserExistsException e) { assertTrue(true); System.out.println("[testDoCreaUtente] Eccezione UserExists gestita correttamente."); }
	}

	// Testo il login di un utente
	@Test
	public void testDoLogin() {
		// Aggiungo almeno l'utente "abc" con testDoCreaUtente
		testDoCreaUtente();
		// Prima provo a loggarmi con un nome sbagliato: eccezione che gestisco.
		try {
			assertFalse(logicaTest.doLogin("abE", "nnn"));
		} catch (UserAuthenticationFailedException e) {}
		// Poi provo a sbagliare la password: eccezione che gestisco
		try {
			assertTrue(logicaTest.doLogin("abc", "mmm"));
		} catch (UserAuthenticationFailedException e) {}
		// Infine se i due test sopra non mi hanno dato errori, posso loggarmi con utente e password corretti. Se fallisco qui il catch termina i test con errore.
		try {
			assertTrue(logicaTest.doLogin("abc", "myPwd"));
		} catch (UserAuthenticationFailedException e) { fail(); }	
	}

	// Testo la creazione di una razza per uno e poi per più utenti
	@Test
	public void testDoCreaRazza() {
		// Aggiungo due utenti tramite l'helper per avere una base su cui effettuare il test.
		aggiungiDueUtenti();
		try {
			// Provo a creare una razza con l'utente 1 ("abc"), ho successo.
			assertTrue(logicaTest.doCreaRazza(testingToken, "miaRazza1", 'c'));
			// Poi provo a crearne un'altra sempre con lo stesso utente, fallisco perchè la razza per l'utente esiste già.
			assertFalse(logicaTest.doCreaRazza(testingToken, "mbc", 'e'));
			// Provo a creare con il secondo utente un'altra razza ma avente lo stesso nome della razza del primo utente.
			// Fallisco con eccezione che gestisco perchè il nome della razza è già occupato
			assertFalse(logicaTest.doCreaRazza(testingToken2, "miaRazza1", 'e'));
		}
		catch (NomeRazzaOccupatoException e) { System.out.println("[testDoCreaRazza] Eccezione NomeRazzaOccupato gestita correttamente."); }
		catch (InvalidTokenException e) { fail(); }
		try {
			// Infine testo la creazione della razza con un token non valido.
			// Fallisco con l'eccezione InvalidToken che gestisco.
			assertTrue(logicaTest.doCreaRazza("298375892735", "bbb", 'd'));
		}
		catch (NomeRazzaOccupatoException e) { fail(); }
		catch (InvalidTokenException e) { System.out.println("[testDoCreaRazza] Eccezione InvalidToken gestita correttamente."); }
	}

	// Testo se l'helper isPlayerLogged funziona correttamente
	@Test
	public void testIsPlayerLogged() {
		// Richiamo l'helper per aggiungere due utenti ("abc" e "abd") e avere testingToken e testingToken2.
		aggiungiDueUtenti();
		// Se il player è effettivamente loggato è tutto ok e ho true come risposta.
		// Non tollero eccezioni.
		try {
			assertTrue(logicaTest.isPlayerLogged(testingToken));
			assertTrue(logicaTest.isPlayerLogged(testingToken2));
		}
		catch (InvalidTokenException e) { fail(); }
		// Se il player non è loggato ho false come risposta.
		// Non tollero eccezioni.
		try {
			assertFalse(logicaTest.isPlayerLogged("zzz"));
		}
		catch (InvalidTokenException e) { fail(); }
	}
	
	// Testo se l'helper getPlayerByToken funziona correttamente
	@Test
	public void testGetPlayerByToken() {
		// Richiamo l'helper per aggiungere due utenti ("abc" e "abd") e avere testingToken e testingToken2.
		aggiungiDueUtenti();
		try {
			// Le prime due invocazioni si aspettano che gli utenti esistano
			assertNotNull(logicaTest.getPlayerByToken(testingToken));
			assertNotNull(logicaTest.getPlayerByToken(testingToken2));
			// La terza causa un'eccezione che viene gestita
			assertNull(logicaTest.getPlayerByToken("zzz"));
		}
		catch (InvalidTokenException e) { System.out.println("[testGetPlayerByToken] Eccezione InvalidToken gestita correttamente."); }
	}

	// Testo se l'accesso alla partita funziona correttamente
	@Test
	public void testDoAccessoPartita() {
		// Richiamo l'helper per aggiungere due utenti ("abc" e "abd") e avere testingToken e testingToken2.
		aggiungiDueUtenti();
		// Testo con un token non valido (mi aspetto eccezione)
		try {
			assertFalse(logicaTest.doAccessoPartita("zzzz"));
		}
		catch (InvalidTokenException e) { System.out.println("[testDoAccessoPartita] Eccezione InvalidToken gestita correttamente."); }
		catch (InterruptedException e) { fail(); }
		catch (TroppiGiocatoriException e) { fail(); }
		catch (RazzaNonCreataException e) { fail(); }

		// Testo con token e condizioni valide (creo la razza visto che i giocatori sono "solo" aggiunti all'array)
		// Non ammetto eccezioni
		try {
			assertTrue(logicaTest.doCreaRazza(testingToken, "miaRazza1", 'e'));
			assertTrue(logicaTest.doAccessoPartita(testingToken));
		}
		catch (InvalidTokenException e) { fail(); }
		catch (InterruptedException e) { fail(); }
		catch (TroppiGiocatoriException e) { fail(); }
		catch (RazzaNonCreataException e) { fail(); }
		catch (NomeRazzaOccupatoException e) { fail(); }

		// Aggiungi un po' di utenti per i test successivi
		// Non ammetto eccezioni
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
		catch (UserExistsException e) { fail(); }
		catch (UserAuthenticationFailedException e) { fail(); }

		// Testo un utente con la razza non creata (mi aspetto eccezione)
		try {
			assertTrue(logicaTest.doAccessoPartita(testingToken2));
		}
		catch (InvalidTokenException e) { fail(); }
		catch (InterruptedException e) { fail(); }
		catch (TroppiGiocatoriException e) { fail(); }
		catch (RazzaNonCreataException e) { System.out.println("[testDoAccessoPartita] Eccezione RazzaNonCreataException gestita correttamente."); }

		// Crea le razze per tutti i giocatori di modo da testare la condizione "troppi giocatori"
		// Non ammetto eccezioni
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
		catch (NomeRazzaOccupatoException e) { fail(); }
		catch (InvalidTokenException e) { fail(); }

		// Testo troppi giocatori (mi aspetto eccezione)
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
		catch (InvalidTokenException e) { fail(); }
		catch (InterruptedException e) { fail(); }
		catch (TroppiGiocatoriException e) { System.out.println("[testDoAccessoPartita] Eccezione TroppiGiocatoriException gestita correttamente."); }
		catch (RazzaNonCreataException e) { fail(); }
	}

	// Verifico che l'helper "isPlayerInGame" funzioni correttamente
	@Test
	public void testIsPlayerInGame() {
		// Richiamo l'helper per aggiungere due utenti ("abc" e "abd") e avere testingToken e testingToken2.
		aggiungiDueUtenti();
		// Aggiungo tutti gli utenti come nel test testDoAccessoPartita
		// Li creo, li loggo, creo le razze e li faccio accedere alla partita sin quando ho l'eccezione (TroppiGiocatoriException).
		// Poi verifico che di tutti questi solo i primi 8 sono in partita, gli altri no.
		try {
			assertTrue(logicaTest.doCreaRazza(testingToken, "miaRazza1", 'e'));
			assertTrue(logicaTest.doCreaRazza(testingToken2, "miaRazza2", 'e'));
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
			assertTrue(logicaTest.doAccessoPartita(testingToken));
			assertTrue(logicaTest.doAccessoPartita(testingToken2));
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
		catch (UserExistsException e) { fail(); }
		catch (UserAuthenticationFailedException e) { fail(); }
		catch (NomeRazzaOccupatoException e) { fail(); }
		catch (InvalidTokenException e) { fail(); }
		catch (InterruptedException e) { fail(); }
		catch (TroppiGiocatoriException e) { System.out.println("[testIsPlayerInGame] Eccezione TroppiGiocatoriException gestita correttamente "); }
		catch (RazzaNonCreataException e) { fail(); }
		// Testo che effettivamente i primi 8 sono in partita e gli altri no.
		// Attenzione!! Cambiando il parametro al costruttore di RRScheduler questo test NON VALE PIÙ ESATTAMENTE!
		// Avrò più o meno giocatori in partita dipendentemente dal parametro.
		try {
			assertTrue(logicaTest.isPlayerInGame(testingToken));
			assertTrue(logicaTest.isPlayerInGame(testingToken2));
			assertTrue(logicaTest.isPlayerInGame(testingToken3));
			assertTrue(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abf")));
			assertTrue(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abg")));
			assertTrue(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abh")));
			assertTrue(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abi")));
			assertTrue(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abj")));
			assertFalse(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abk")));
			assertFalse(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abl")));
			assertFalse(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abm")));
			assertFalse(logicaTest.isPlayerInGame(logicaTest.getCMan().getToken("abn")));
		}
		catch (InvalidTokenException e) { }
	}

	// Verifico che l'helper existsDinosauroWithId funzioni correttamente
	@Test
	public void testExistsDinosauroWithId() {
		// Richiamo l'helper per aggiungere due utenti ("abc" e "abd") e avere testingToken e testingToken2.
		aggiungiDueUtenti();
		// Creo la razza per l'utente e poi parto col test
		// Non ammetto eccezioni
		try {
			assertTrue(logicaTest.doCreaRazza(testingToken, "miaRazza1", 'e'));
			assertTrue(logicaTest.doAccessoPartita(testingToken));
		}
		catch (InvalidTokenException e) { fail(); }
		catch (NomeRazzaOccupatoException e) { fail(); }
		catch (InterruptedException e) { fail(); }
		catch (TroppiGiocatoriException e) { fail(); }
		catch (RazzaNonCreataException e) { fail(); }
		// Parto col test. Semplicemente prendo un iteratore sui dinosauri del giocatore,
		// verifico che abbia elementi e poi verifico che effettivamente existsDinosauroWithId ritorni i valori desiderati.
		// Non ammetto eccezioni
		Iterator<Dinosauro> itDino = null;
		try {
			// Prendo l'iteratore sulla razza
			itDino = logicaTest.getPlayerByToken(testingToken).getRazza().iterator();
		}
		catch (InvalidTokenException e) { fail(); }
		// Testo che l'iteratore sia consistente (sebbene abbia un solo elemento).
		assertNotNull(itDino);
		assertTrue(itDino.hasNext());
		// Prendo il primo dinosauro restituito
		Dinosauro tmpDino = itDino.next();
		// Verifico che sia consistente (non nullo)
		assertNotNull(tmpDino);
		// Ne prendo l'id
		String tmpId = tmpDino.getIdDinosauro();
		// Verifico che l'id non sia nullo
		assertNotNull(tmpId);
		// Verifico che l'id estratto sia effettivamente uguale a quello del dinosauro
		assertEquals(tmpId, tmpDino.getIdDinosauro());
		// Testo che il dinosauro effettivamente esiste: mi aspetto true
		assertTrue(logicaTest.existsDinosauroWithId(tmpId));
		// Testo che un dinosauro con Id non previsto non esiste (è troppo corto rispetto a quelli standard impiegati)
		assertFalse(logicaTest.existsDinosauroWithId("iu23jwegnw"));
	}

	// Testo che l'helper getPlayerByIdDinosauro funzioni correttamente
	@Test
	public void testGetPlayerByIdDinosauro() {
		// Richiamo l'helper per aggiungere due utenti ("abc" e "abd") e avere testingToken e testingToken2.
		aggiungiDueUtenti();
		// Creo la razza per l'utente e poi parto col test
		// Non ammetto eccezioni
		try {
			assertTrue(logicaTest.doCreaRazza(testingToken, "miaRazza1", 'e'));
			assertTrue(logicaTest.doAccessoPartita(testingToken));
		}
		catch (InvalidTokenException e) { fail(); }
		catch (NomeRazzaOccupatoException e) { fail(); }
		catch (InterruptedException e) { fail(); }
		catch (TroppiGiocatoriException e) { fail(); }
		catch (RazzaNonCreataException e) { fail(); }
		// Parto col test. Semplicemente prendo un iteratore sui dinosauri del giocatore,
		// verifico che abbia elementi e poi verifico che effettivamente getPlayerByIdDinosauro ritorni il giocatore che possiede il dinosauro.
		// Non ammetto eccezioni
		Iterator<Dinosauro> itDino = null;
		try {
			// Prendo l'iteratore sulla razza
			itDino = logicaTest.getPlayerByToken(testingToken).getRazza().iterator();
		}
		catch (InvalidTokenException e) { fail(); }
		// Testo che l'iteratore sia consistente (sebbene abbia un solo elemento).
		assertNotNull(itDino);
		assertTrue(itDino.hasNext());
		// Prendo il primo dinosauro restituito
		Dinosauro tmpDino = itDino.next();
		// Verifico che sia consistente (non nullo)
		assertNotNull(tmpDino);
		// Ne prendo l'id
		String tmpId = tmpDino.getIdDinosauro();
		// Verifico che l'id non sia nullo
		assertNotNull(tmpId);
		// Verifico che l'id estratto sia effettivamente uguale a quello del dinosauro
		assertEquals(tmpId, tmpDino.getIdDinosauro());
		// Verifico che getPlayerByIdDinosauro ritorni un valore non nullo per l'Id del dinosauro estratto
		assertNotNull(logicaTest.getPlayerByIdDinosauro(tmpId));
		// Estraggo il giocatore
		Giocatore tempGiocatore = logicaTest.getPlayerByIdDinosauro(tmpId);
		// Verifico che il giocatore non sia nullo
		assertNotNull(tempGiocatore);
		// Verifico che effettivamente il dinosauro è tra quelli del giocatore
		assertTrue(tempGiocatore.getRazza().contains(tmpDino));
		assertTrue(tempGiocatore.getRazza().existsDinosauroWithId(tmpId));
		// Verifico che con un Id fasullo getPlayerByIdDinosauro ritorna null
		assertNull(logicaTest.getPlayerByIdDinosauro("kajsf"));
		// Verifico di nuovo che comunque il dinosauro esiste, per cui l'associazione dinosauro<->giocatore è univoca
		assertTrue(tempGiocatore.getRazza().existsDinosauroWithId(tmpId));
	}

	// Testo che doDeponiUovo funzioni correttamente
	@Test
	public void testDoDeponiUovo() {
		// Richiamo l'helper per aggiungere due utenti ("abc" e "abd") e avere testingToken e testingToken2.
		aggiungiDueUtenti();
		// Creo la razza per l'utente e poi parto col test
		// Non ammetto eccezioni
		try {
			assertTrue(logicaTest.doCreaRazza(testingToken, "miaRazza1", 'e'));
			assertTrue(logicaTest.doAccessoPartita(testingToken));
		}
		catch (InvalidTokenException e) { fail(); }
		catch (NomeRazzaOccupatoException e) { fail(); }
		catch (InterruptedException e) { fail(); }
		catch (TroppiGiocatoriException e) { fail(); }
		catch (RazzaNonCreataException e) { fail(); }
		// Parto col test. Semplicemente prendo un iteratore sui dinosauri del giocatore,
		// verifico che abbia elementi e poi estraggo in locale il primo dinosauro restituito dall'iteratore.
		// Non ammetto eccezioni
		Iterator<Dinosauro> itDino = null;
		try {
			// Prendo l'iteratore sulla razza
			itDino = logicaTest.getPlayerByToken(testingToken).getRazza().iterator();
		}
		catch (InvalidTokenException e) { fail(); }
		// Testo che l'iteratore sia consistente (sebbene abbia un solo elemento).
		assertNotNull(itDino);
		assertTrue(itDino.hasNext());
		// Prendo il primo dinosauro restituito
		Dinosauro tmpDino = itDino.next();
		// Verifico che sia consistente (non nullo)
		assertNotNull(tmpDino);
		// Ne prendo l'id
		String tmpId = tmpDino.getIdDinosauro();
		// Verifico che l'id non sia nullo
		assertNotNull(tmpId);
		// Verifico che l'id estratto sia effettivamente uguale a quello del dinosauro
		assertEquals(tmpId, tmpDino.getIdDinosauro());
		// Imposto l'energia al massimo così da poter far crescere il dinosauro
		tmpDino.setEnergiaAttuale(tmpDino.getEnergiaMax());
		// Fai crescere il dinosauro quanto serve per testare deponiUovo (almeno dimensione 2)
		// Non ammetto eccezioni
		try {
			logicaTest.getPlayerByToken(testingToken).getRazza().cresciDinosauro(tmpId);
			logicaTest.getPlayerByToken(testingToken).getRazza().aggiornaRazza();
		}
		catch (InvalidTokenException e) { fail(); }
		catch (GenericDinosauroException e) { fail(); }
		// Re-imposto l'energia al massimo
		tmpDino.setEnergiaAttuale(tmpDino.getEnergiaMax());
		// Creo una nuova variabile per gestire il token ritornato da una chiamata valida a doDeponiUovo
		String newToken = null;
		// Testo doDeponiUovo con un Id dinosauro valido ma un token non valido
		// Viene sollevata un'eccezione InvalidTokenException che gestisco
		try {
			newToken = logicaTest.doDeponiUovo("alkjs", tmpId);
			// Mi assicuro che il token ritornato non sia nullo
			assertNull(newToken);
			// Mi assicuro che il dinosauro a cui sto cercando di far deporre l'uovo effettivamente esiste
			assertTrue(logicaTest.existsDinosauroWithId(tmpId));
		}
		catch (GenericDinosauroException e) { fail(); }
		catch (InvalidTokenException e) { System.out.println("[testDoDeponiUovo] Eccezione InvalidTokenException gestita correttamente."); }
		// Testo doDeponiUovo con condizioni valide
		// Non ammetto eccezioni
		try {
			newToken = logicaTest.doDeponiUovo(testingToken, tmpId);
			// Mi assicuro che il token ritornato non sia nullo
			assertNotNull(newToken);
			// Mi assicuro che effettivamente il dinosauro esiste
			assertTrue(logicaTest.existsDinosauroWithId(newToken));
		}
		catch (GenericDinosauroException e) { fail(); }
		catch (InvalidTokenException e) { fail(); }
		// Testo doDeponiUovo dopo che ho già invocato l'azione con successo
		// Viene sollevata un'eccezione GenericDinosauroException con causa "raggiuntoLimiteMosseDinosauro" che gestisco.
		try {
			// Verifico che il valore di 
			assertNull(logicaTest.doDeponiUovo(testingToken, tmpId));
		}
		catch (GenericDinosauroException e) { System.out.println("[testDoDeponiUovo] Eccezione GenericDinosauroException gestita correttamente \"" + e.getMessage() + "\""); }
		catch (InvalidTokenException e) { fail(); }
		// Porto il dinosauro a energia minima (di modo che muoia)
		tmpDino.setEnergiaAttuale(1);
		// Testo doDeponiUovo dopo aver aggiornato la razza (di modo da avere nuovamente l'azione a disposizione
		// Viene sollevata un'eccezione GenericDinosauroException con causa "mortePerInedia" che gestisco
		try {
			logicaTest.getPlayerByToken(testingToken).getRazza().aggiornaRazza();
			assertNotNull(logicaTest.doDeponiUovo(testingToken, tmpId));
		}
		catch (GenericDinosauroException e) { System.out.println("[testDoDeponiUovo] Eccezione GenericDinosauroException gestita correttamente \"" + e.getMessage() + "\""); }
		catch (InvalidTokenException e) { fail(); }
	}

	// Testo che doMuoviDinosauro funzioni correttamente
	@Test
	public void testDoMuoviDinosauro() {
		// Richiamo l'helper per aggiungere due utenti ("abc" e "abd") e avere testingToken e testingToken2.
		aggiungiDueUtenti();
		// Creo la razza per l'utente e poi parto col test
		// Non ammetto eccezioni
		try {
			assertTrue(logicaTest.doCreaRazza(testingToken, "miaRazza1", 'e'));
			assertTrue(logicaTest.doAccessoPartita(testingToken));
		}
		catch (InvalidTokenException e) { fail(); }
		catch (NomeRazzaOccupatoException e) { fail(); }
		catch (InterruptedException e) { fail(); }
		catch (TroppiGiocatoriException e) { fail(); }
		catch (RazzaNonCreataException e) { fail(); }
		// Parto col test. Semplicemente prendo un iteratore sui dinosauri del giocatore,
		// verifico che abbia elementi e poi estraggo in locale il primo dinosauro restituito dall'iteratore.
		// Non ammetto eccezioni
		Iterator<Dinosauro> itDino = null;
		try {
			// Prendo l'iteratore sulla razza
			itDino = logicaTest.getPlayerByToken(testingToken).getRazza().iterator();
		}
		catch (InvalidTokenException e) { fail(); }
		// Testo che l'iteratore sia consistente (sebbene abbia un solo elemento).
		assertNotNull(itDino);
		assertTrue(itDino.hasNext());
		// Prendo il primo dinosauro restituito
		Dinosauro tmpDino = itDino.next();
		// Verifico che sia consistente (non nullo)
		assertNotNull(tmpDino);
		// Ne prendo l'id
		String tmpId = tmpDino.getIdDinosauro();
		// Verifico che l'id non sia nullo
		assertNotNull(tmpId);
		// Verifico che l'id estratto sia effettivamente uguale a quello del dinosauro
		assertEquals(tmpId, tmpDino.getIdDinosauro());
		// Imposto l'energia al massimo così da poter far crescere il dinosauro
		tmpDino.setEnergiaAttuale(tmpDino.getEnergiaMax());
		// Fai crescere il dinosauro quanto serve per testare 
		// Non ammetto eccezioni
		try {
			logicaTest.getPlayerByToken(testingToken).getRazza().cresciDinosauro(tmpId);
			logicaTest.getPlayerByToken(testingToken).getRazza().aggiornaRazza();
		}
		catch (InvalidTokenException e) { fail(); }
		catch (GenericDinosauroException e) { fail(); }
		// Re-imposto l'energia al massimo
		tmpDino.setEnergiaAttuale(tmpDino.getEnergiaMax());
		// Testa movimento quando tutto è corretto
		// Non ammetto eccezioni
		String ret = null;
		try {
			do {
				ret = logicaTest.doMuoviDinosauro(testingToken, tmpId, new Coord(tmpDino.getCoord().getX()+CommonUtils.getNewRandomIntValueOnMyMap(3), tmpDino.getCoord().getY()+CommonUtils.getNewRandomIntValueOnMyMap(3)));
				System.out.println("ret: " + ret);
				assertNotNull(ret);
			} while (!ret.equals("@ok"));
		}
		catch (InvalidTokenException e) { fail(); }
		catch (GenericDinosauroException e) { fail(); }
		// Testa movimento quando il dinosauro ha esaurito le mosse (GenericDinosauroException con causa "raggiuntoLimiteMosse")
		try {
			tmpDino = logicaTest.getPlayerByIdDinosauro(tmpId).getRazza().getDinosauroById(tmpId);
			tmpDino.setEnergiaAttuale(tmpDino.getEnergiaMax());
			ret = logicaTest.doMuoviDinosauro(testingToken, tmpId, new Coord(tmpDino.getCoord().getX()+1, tmpDino.getCoord().getY()+1));
			assertNotNull(ret);
		}
		catch (InvalidTokenException e) { fail(); }
		catch (GenericDinosauroException e) { System.out.println("[testDoMuoviDinosauro] Eccezione GenericDinosauroException gestita correttamente \"" + e.getMessage() + "\""); }
		// TODO implementare test più estensivi per erbivoro su vegetazione e carnivoro su carogna. Usare l'iteratore della mappa per avere una cella di tipo desiderato
	}

	// Testo che doUscitaPartita funzioni correttamente
	// Copio-incollo il setup di base: utenti loggati con razze valide e in partita (tranne uno, che funziona da "altro caso"
	@Test
	public void testDoUscitaPartita() {
		// Richiamo l'helper per aggiungere due utenti ("abc" e "abd") e avere testingToken e testingToken2.
		aggiungiDueUtenti();
		// Aggiungo tutti gli utenti come nel test testDoAccessoPartita
		// Li creo, li loggo, creo le razze e li faccio accedere alla partita.
		// Non ammetto eccezioni.
		try {
			assertTrue(logicaTest.doCreaRazza(testingToken, "miaRazza1", 'e'));
			assertTrue(logicaTest.doCreaRazza(testingToken2, "miaRazza2", 'e'));
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
			testingToken3 = logicaTest.getCMan().getToken("abe");
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abe"), "miaRazza3", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abf"), "miaRazza4", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abg"), "miaRazza5", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abh"), "miaRazza6", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abi"), "miaRazza7", 'c'));
			assertTrue(logicaTest.doAccessoPartita(testingToken));
			assertTrue(logicaTest.doAccessoPartita(testingToken2));
			assertTrue(logicaTest.doAccessoPartita(testingToken3));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abf")));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abg")));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abh")));
		}
		catch (UserExistsException e) { fail(); }
		catch (UserAuthenticationFailedException e) { fail(); }
		catch (NomeRazzaOccupatoException e) { fail(); }
		catch (InvalidTokenException e) { fail(); }
		catch (InterruptedException e) { fail(); }
		catch (TroppiGiocatoriException e) { fail(); }
		catch (RazzaNonCreataException e) { fail(); }
		// Testo effettivamente doUscitaPartita
		// Non ammetto eccezioni
		try {
			assertTrue(logicaTest.doUscitaPartita(testingToken));
			assertFalse(logicaTest.isPlayerInGame(testingToken));
			assertTrue(logicaTest.doUscitaPartita(testingToken2));
			assertFalse(logicaTest.isPlayerInGame(testingToken2));
			assertTrue(logicaTest.doUscitaPartita(testingToken3));
			assertFalse(logicaTest.isPlayerInGame(testingToken3));
			// Di quelli che seguono non ho più il token per testare che effettivamente non siano in partita
			assertTrue(logicaTest.doUscitaPartita(logicaTest.getCMan().getToken("abf")));
			assertTrue(logicaTest.doUscitaPartita(logicaTest.getCMan().getToken("abg")));
			assertTrue(logicaTest.doUscitaPartita(logicaTest.getCMan().getToken("abh")));
			assertFalse(logicaTest.doUscitaPartita(logicaTest.getCMan().getToken("abi")));
		}
		catch (InvalidTokenException e) { fail(); }
		// Testo con un token fasullo e gestisco l'eccezione InvalidToken
		try {
			assertFalse(logicaTest.doUscitaPartita("tokenFasullo"));
		}
		catch (InvalidTokenException e) { System.out.println("[testDoUscitaPartita] Eccezione InvalidToken gestita correttamente"); }
	}

	// Testo che doLogout funzioni correttamente
	// Copio-incollo il setup di base: utenti loggati con razze valide
	@Test
	public void testDoLogout() {
		// Richiamo l'helper per aggiungere due utenti ("abc" e "abd") e avere testingToken e testingToken2.
		aggiungiDueUtenti();
		// Aggiungo tutti gli utenti come nel test testDoAccessoPartita
		// Li creo, li loggo, creo le razze e li faccio accedere alla partita.
		// Non ammetto eccezioni.
		try {
			assertTrue(logicaTest.doCreaRazza(testingToken, "miaRazza1", 'e'));
			assertTrue(logicaTest.doCreaRazza(testingToken2, "miaRazza2", 'e'));
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
			testingToken3 = logicaTest.getCMan().getToken("abe");
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abe"), "miaRazza3", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abf"), "miaRazza4", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abg"), "miaRazza5", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abh"), "miaRazza6", 'c'));
			assertTrue(logicaTest.doCreaRazza(logicaTest.getCMan().getToken("abi"), "miaRazza7", 'c'));
			assertTrue(logicaTest.doAccessoPartita(testingToken));
			assertTrue(logicaTest.doAccessoPartita(testingToken2));
			assertTrue(logicaTest.doAccessoPartita(testingToken3));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abf")));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abg")));
			assertTrue(logicaTest.doAccessoPartita(logicaTest.getCMan().getToken("abh")));
		}
		catch (UserExistsException e) { fail(); }
		catch (UserAuthenticationFailedException e) { fail(); }
		catch (NomeRazzaOccupatoException e) { fail(); }
		catch (InvalidTokenException e) { fail(); }
		catch (InterruptedException e) { fail(); }
		catch (TroppiGiocatoriException e) { fail(); }
		catch (RazzaNonCreataException e) { fail(); }
		// Testa effettivamente il logout
		// Attenzione! Alcuni utenti sono ANCHE in partita.
		// Non dovrebbe costituire problema, perchè l'implementazione nel caso gestisce prima l'uscita e poi il logout.
		try {
			assertTrue(logicaTest.doLogout(testingToken));
			assertTrue(logicaTest.doLogout(testingToken2));
			assertTrue(logicaTest.doLogout(testingToken3));
			assertTrue(logicaTest.doLogout(logicaTest.getCMan().getToken("abf")));
			assertTrue(logicaTest.doLogout(logicaTest.getCMan().getToken("abg")));
			assertTrue(logicaTest.doLogout(logicaTest.getCMan().getToken("abh")));
		}
		catch (InvalidTokenException e) { System.out.println("[testDoLogout] Eccezione InvalidToken gestita correttamente."); }
		// Testo con un token fasullo e gestisco l'eccezione InvalidToken
		try {
			assertFalse(logicaTest.doLogout("tokenFasullo"));
		}
		catch (InvalidTokenException e) { System.out.println("[testDoUscitaPartita] Eccezione InvalidToken gestita correttamente"); }
	}
	
	// Testo che doCresciDinosauro funzioni correttamente
	// Copio-incollo il setup di base: utenti loggati con razze valide
	@Test
	public void testDoCresciDinosauro() {
		// Richiamo l'helper per aggiungere due utenti ("abc" e "abd") e avere testingToken e testingToken2.
		aggiungiDueUtenti();
		// Creo la razza per l'utente e poi parto col test
		// Non ammetto eccezioni
		try {
			assertTrue(logicaTest.doCreaRazza(testingToken, "miaRazza1", 'e'));
			assertTrue(logicaTest.doAccessoPartita(testingToken));
		}
		catch (InvalidTokenException e) { fail(); }
		catch (NomeRazzaOccupatoException e) { fail(); }
		catch (InterruptedException e) { fail(); }
		catch (TroppiGiocatoriException e) { fail(); }
		catch (RazzaNonCreataException e) { fail(); }
		// Parto col test. Semplicemente prendo un iteratore sui dinosauri del giocatore,
		// verifico che abbia elementi e poi estraggo in locale il primo dinosauro restituito dall'iteratore.
		// Non ammetto eccezioni
		Iterator<Dinosauro> itDino = null;
		try {
			// Prendo l'iteratore sulla razza
			itDino = logicaTest.getPlayerByToken(testingToken).getRazza().iterator();
		}
		catch (InvalidTokenException e) { fail(); }
		// Testo che l'iteratore sia consistente (sebbene abbia un solo elemento).
		assertNotNull(itDino);
		assertTrue(itDino.hasNext());
		// Prendo il primo dinosauro restituito
		Dinosauro tmpDino = itDino.next();
		// Verifico che sia consistente (non nullo)
		assertNotNull(tmpDino);
		// Ne prendo l'id
		String tmpId = tmpDino.getIdDinosauro();
		// Verifico che l'id non sia nullo
		assertNotNull(tmpId);
		// Verifico che l'id estratto sia effettivamente uguale a quello del dinosauro
		assertEquals(tmpId, tmpDino.getIdDinosauro());
		// Imposto l'energia al massimo così da poter far crescere il dinosauro
		tmpDino.setEnergiaAttuale(tmpDino.getEnergiaMax());
		// Testo con condizioni valide
		// Non ammetto eccezioni
		try {
			assertTrue(logicaTest.doCresciDinosauro(testingToken, tmpId));
		}
		catch (InvalidTokenException e) { fail(); }
		catch (GenericDinosauroException e) { fail(); }
		// Testo con id dinosauro fasullo
		// Non ammetto eccezioni
		try {
			assertFalse(logicaTest.doCresciDinosauro(testingToken, "IdDinoFasullo"));
		}
		catch (InvalidTokenException e) { fail(); }
		catch (GenericDinosauroException e) { fail(); }
		// Testo con token non valido
		try {
			assertFalse(logicaTest.doCresciDinosauro("IdFasullo", tmpId));
		}
		catch (InvalidTokenException e) { System.out.println("[testDoCresciDinosauro] Eccezione InvalidToken gestita correttamente"); }
		catch (GenericDinosauroException e) { fail(); }
		// Testo la morte per inedia
		// Imposto l'energia minima
		tmpDino.setEnergiaAttuale(1);
		try {
			assertFalse(logicaTest.doCresciDinosauro(testingToken, tmpId));
		}
		catch (InvalidTokenException e) { fail(); }
		catch (GenericDinosauroException e) { System.out.println("[testDoMuoviDinosauro] Eccezione GenericDinosauroException gestita correttamente \"" + e.getMessage() + "\""); }
	}

	// TODO
	/* isMioTurno
	 * run
	 * doCresciDinosauro
	 */
}