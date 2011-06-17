package dinolib;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class RRSchedulerTest {
	private RRScheduler rrsched = null;

	@Before
	public void setUp() {
		rrsched = new RRScheduler(8);
	}

	@Test
	public void testCostruttore() {
		assertNotNull(rrsched);
	}

	@Test
	public void testNewTask() {
		try {
			assertTrue(rrsched.newTask("abcd"));
			assertTrue(rrsched.newTask("abce"));
			assertTrue(rrsched.newTask("abcf"));
			assertFalse(rrsched.newTask("abcd"));
			assertFalse(rrsched.newTask("abce"));
			assertFalse(rrsched.newTask("abcf"));
		} catch (InterruptedException e) {
			fail();
		}
	}

	@Test
	public void testHasQueuedTasks() {
		assertFalse(rrsched.hasQueuedTasks());
		try {
			assertTrue(rrsched.newTask("abcd"));
		}
		catch (InterruptedException e) { fail(); }
		assertTrue(rrsched.hasQueuedTasks());
	}

	@Test
	public void testHasTask() {
		assertFalse(rrsched.hasTask("abcd"));
		try {
			assertTrue(rrsched.newTask("abcd"));
		}
		catch (InterruptedException e) { fail(); }
		assertTrue(rrsched.hasTask("abcd"));
	}

	@Test
	public void testIterator() {
		try {
			assertTrue(rrsched.newTask("abcd"));
			assertTrue(rrsched.newTask("abce"));
		}
		catch (InterruptedException e) { fail(); }
		assertNotNull(rrsched.iterator());
		Iterator<String> itTasks = rrsched.iterator();
		assertNotNull(itTasks);
		assertTrue(itTasks.hasNext());
		assertNotNull(itTasks.next());
		assertTrue(itTasks.hasNext());
		assertNotNull(itTasks.next());
		assertFalse(itTasks.hasNext());
	}

	@Test
	public void testMaxPlayers() {
		assertFalse(rrsched.maxPlayers());
		try {
			assertTrue(rrsched.newTask("abcd"));
			assertTrue(rrsched.newTask("abce"));
			assertTrue(rrsched.newTask("abcf"));
			assertTrue(rrsched.newTask("abcg"));
			assertTrue(rrsched.newTask("abch"));
			assertTrue(rrsched.newTask("abci"));
			assertTrue(rrsched.newTask("abcj"));
			assertTrue(rrsched.newTask("abck"));
		}
		catch (InterruptedException e) {
			fail();
		}
		assertTrue(rrsched.maxPlayers());
	}

	@Test
	public void testKillTask() {
		assertFalse(rrsched.hasQueuedTasks());
		try {
			assertTrue(rrsched.newTask("abcd"));
			assertTrue(rrsched.newTask("abce"));
			assertTrue(rrsched.newTask("abcf"));
		}
		catch (InterruptedException e) {
			fail();
		}
		assertTrue(rrsched.hasTask("abcd"));
		assertTrue(rrsched.hasTask("abce"));
		assertTrue(rrsched.hasTask("abcf"));
		assertTrue(rrsched.killTask("abcd"));
		assertTrue(rrsched.killTask("abce"));
		assertTrue(rrsched.killTask("abcf"));
		assertFalse(rrsched.hasTask("abcd"));
		assertFalse(rrsched.hasTask("abce"));
		assertFalse(rrsched.hasTask("abcf"));
		assertFalse(rrsched.hasQueuedTasks());
	}

	@Test
	public void testGetCurrentTask() {
		try {
			assertTrue(rrsched.newTask("abcd"));
			assertTrue(rrsched.newTask("abce"));
			assertTrue(rrsched.newTask("abcf"));
		}
		catch (InterruptedException e) {
			fail();
		}
		assertTrue(rrsched.hasQueuedTasks());
		try {
			assertEquals("abcd", rrsched.getCurrentTask());
			assertEquals("abce", rrsched.getCurrentTask());
			assertEquals("abcf", rrsched.getCurrentTask());
		}
		catch (InterruptedException e) {
			fail();
		}
		assertFalse(rrsched.hasQueuedTasks());
	}
}