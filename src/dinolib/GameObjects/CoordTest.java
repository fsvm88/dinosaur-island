package dinolib.GameObjects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CoordTest {
	private Coord coord = null;
	
	@Before
	public void setUp() {
		coord = new Coord(0, 0);
	}
	
	@Test
	public void testCostruttore() {
		assertNotNull(coord);
	}
	
	@Test
	public void testGetters() {
		assertEquals(0, coord.getX());
		assertEquals(0, coord.getY());
		coord = new Coord(999, 1000);
		assertEquals(999, coord.getX());
		assertEquals(1000, coord.getY());
	}
	
	@Test
	public void testEquals() {
		assertTrue(coord.equals(new Coord(0, 0)));
	}
}