package dinolib;

import static org.junit.Assert.*;

import java.util.Iterator;


import org.junit.Before;
import org.junit.Test;

import dinolib.Exceptions.InvalidTokenException;

public class ConnectionManagerTest {
	private ConnectionManager cMan = null;

	@Before
	public void setUp() {
		cMan = new ConnectionManager();
	}

	@Test
	public void testCostruttore() {
		assertNotNull(cMan);
	}

	@Test
	public void testCollega() {
		if (! cMan.collega("user1", "tk1")) fail();
		if (! cMan.collega("user2", "tk2")) fail();
		if (! cMan.collega("user3", "tk3")) fail();
	}

	@Test
	public void testExists() {
		if (! cMan.collega("user1", "tk1")) fail();
		if (! cMan.collega("user2", "tk2")) fail();
		if (! cMan.collega("user3", "tk3")) fail();
		assertTrue(cMan.existsName("user1"));
		assertTrue(cMan.existsName("user2"));
		assertTrue(cMan.existsName("user3"));
		assertFalse(cMan.existsName("user4"));
		assertFalse(cMan.existsName("user5"));

		assertTrue(cMan.existsToken("tk1"));
		assertTrue(cMan.existsToken("tk2"));
		assertTrue(cMan.existsToken("tk3"));
		assertFalse(cMan.existsToken("tk4"));
		assertFalse(cMan.existsToken("tk5"));
	}

	@Test
	public void testGetIteratorOnTokens() {
		assertNull(cMan.getIteratorOnTokens());
		if (! cMan.collega("user1", "tk1")) fail();
		if (! cMan.collega("user2", "tk2")) fail();
		if (! cMan.collega("user3", "tk3")) fail();
		Iterator<String> itTokens = cMan.getIteratorOnTokens();
		assertNotNull(itTokens);
		assertTrue(itTokens.hasNext());
		assertNotNull(itTokens.next());
		assertTrue(itTokens.hasNext());
		assertNotNull(itTokens.next());
		assertTrue(itTokens.hasNext());
		assertNotNull(itTokens.next());
		assertFalse(itTokens.hasNext());
	}

	@Test
	public void testGetIteratorOnConnectedPlayerNames() {
		assertNull(cMan.getIteratorOnConnectedPlayerNames());
		if (! cMan.collega("user1", "tk1")) fail();
		if (! cMan.collega("user2", "tk2")) fail();
		if (! cMan.collega("user3", "tk3")) fail();
		Iterator<String> itNames = cMan.getIteratorOnConnectedPlayerNames();
		assertNotNull(itNames);
		assertTrue(itNames.hasNext());
		assertNotNull(itNames.next());
		assertTrue(itNames.hasNext());
		assertNotNull(itNames.next());
		assertTrue(itNames.hasNext());
		assertNotNull(itNames.next());
		assertFalse(itNames.hasNext());
	}

	@Test
	public void testGetName() {
		if (! cMan.collega("user1", "tk1")) fail();
		if (! cMan.collega("user2", "tk2")) fail();
		if (! cMan.collega("user3", "tk3")) fail();
		try {
			assertNotNull(cMan.getName("tk1"));
			assertEquals("user1", cMan.getName("tk1"));
			assertNotNull(cMan.getName("tk2"));
			assertEquals("user2", cMan.getName("tk2"));
			assertNotNull(cMan.getName("tk3"));
			assertEquals("user3", cMan.getName("tk3"));
			cMan.getName("tk4");
		}
		catch (InvalidTokenException e) {
			assertNull(e.getMessage());
		}
	}

	@Test
	public void testGetToken() {
		if (! cMan.collega("user1", "tk1")) fail();
		if (! cMan.collega("user2", "tk2")) fail();
		if (! cMan.collega("user3", "tk3")) fail();
		assertNotNull(cMan.getToken("user1"));
		assertEquals("tk1", cMan.getToken("user1"));
		assertNotNull(cMan.getToken("user2"));
		assertEquals("tk2", cMan.getToken("user2"));
		assertNotNull(cMan.getToken("user3"));
		assertEquals("tk3", cMan.getToken("user3"));
		assertNull(cMan.getToken("tk4"));
		assertNull(cMan.getToken("tk5"));
	}
	
	@Test
	public void testScollega() {
		if (! cMan.collega("user1", "tk1")) fail();
		if (! cMan.collega("user2", "tk2")) fail();
		if (! cMan.collega("user3", "tk3")) fail();
		assertTrue(cMan.existsToken("tk1"));
		assertTrue(cMan.scollega("tk1"));
		assertFalse(cMan.existsToken("tk1"));
		assertTrue(cMan.existsToken("tk2"));
		assertTrue(cMan.scollega("tk2"));
		assertFalse(cMan.existsToken("tk2"));
		assertTrue(cMan.existsToken("tk3"));
		assertTrue(cMan.scollega("tk3"));
		assertFalse(cMan.existsToken("tk3"));
		assertFalse(cMan.existsToken("tk4"));
		assertFalse(cMan.existsToken("tk5"));
	}
}