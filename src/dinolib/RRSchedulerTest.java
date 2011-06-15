package dinolib;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RRSchedulerTest {
	private RRScheduler rrsched = null;

	@Before
	public void setUp() {
		rrsched = new RRScheduler(8);
	}

	private void testCostruttore() {
		setUp();
		assertNotNull(rrsched);
		assertNotNull(new RRScheduler(1));
		assertNotNull(new RRScheduler(90));
		assertNotNull(new RRScheduler(10000));
	}

	private void testNewTask() {
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

	private void testHasQueuedTasks() {
		setUp();
		assertFalse(rrsched.hasQueuedTasks());
		testNewTask();
		assertTrue(rrsched.hasQueuedTasks());
	}

	private void testHasTask() {
		assertTrue(rrsched.hasTask("abcd"));
		assertTrue(rrsched.hasTask("abce"));
		assertTrue(rrsched.hasTask("abcf"));
		assertFalse(rrsched.hasTask("abdf"));
		assertFalse(rrsched.hasTask("abcb"));
	}

	private void testIterator() {
		assertNotNull(rrsched.iterator());
		assertTrue(rrsched.iterator().hasNext());
		assertNotNull(rrsched.iterator().next());
	}

	private void testMaxPlayers() {
		assertFalse(rrsched.maxPlayers());
		try {
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

	private void testKillTask() {
		assertTrue(rrsched.hasQueuedTasks());
		assertTrue(rrsched.hasTask("abcd"));
		assertTrue(rrsched.killTask("abcd"));
		assertFalse(rrsched.hasTask("abcd"));
		assertTrue(rrsched.hasTask("abce"));
		assertTrue(rrsched.killTask("abce"));
		assertFalse(rrsched.hasTask("abce"));
		assertTrue(rrsched.hasTask("abcf"));
		assertTrue(rrsched.killTask("abcf"));
		assertFalse(rrsched.hasTask("abcf"));
		assertTrue(rrsched.hasTask("abcg"));
		assertTrue(rrsched.killTask("abcg"));
		assertFalse(rrsched.hasTask("abcg"));
		assertTrue(rrsched.hasTask("abch"));
		assertTrue(rrsched.killTask("abch"));
		assertFalse(rrsched.hasTask("abch"));
		assertTrue(rrsched.hasTask("abci"));
		assertTrue(rrsched.killTask("abci"));
		assertFalse(rrsched.hasTask("abci"));
		assertTrue(rrsched.hasTask("abcj"));
		assertTrue(rrsched.killTask("abcj"));
		assertFalse(rrsched.hasTask("abcj"));
		assertTrue(rrsched.hasTask("abck"));
		assertTrue(rrsched.killTask("abck"));
		assertFalse(rrsched.hasTask("abck"));
		assertFalse(rrsched.hasQueuedTasks());
	}

	private void testGetCurrentTask() {
		setUp();
		testNewTask();
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

	@Test
	public void testAll() {
		testCostruttore();
		testNewTask();
		testHasQueuedTasks();
		testHasTask();
		testIterator();
		testMaxPlayers();
		testKillTask();
		testGetCurrentTask();
	}
}