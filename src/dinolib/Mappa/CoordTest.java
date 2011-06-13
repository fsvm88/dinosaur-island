package dinolib.Mappa;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CoordTest {
	private Coord coord = null;
	
	@Before
	public void setUp() {
		coord = new Coord(0, 0);
	}
	
	private void testGetters() {
		assertEquals(0, coord.getX());
		assertEquals(0, coord.getY());
	}
	
	private void testEquals() {
		assertTrue(coord.equals(new Coord(0, 0)));
	}
	
	@Test
	public void testCoord() {
		testGetters();
		testEquals();
	}
}