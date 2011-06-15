package dinolib.Mappa;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author  fabio
 */
public class MappaTest {
	private Mappa testMappa = null;
	
	@Before
	public void setUp() {
		testMappa = new Mappa(40);
	}
	
	private void testGetCella() {
		assertNotNull(testMappa.getCella(new Coord(0, 0)));
		assertNotNull(testMappa.getCella(new Coord(0, testMappa.getLatoDellaMappa()-1)));
		assertNotNull(testMappa.getCella(new Coord(testMappa.getLatoDellaMappa()-1, 0)));
		assertNull(testMappa.getCella(new Coord(-1, 0)));
		assertNull(testMappa.getCella(new Coord(0, -1)));
		assertNull(testMappa.getCella(new Coord(testMappa.getLatoDellaMappa(), 0)));
		assertNull(testMappa.getCella(new Coord(0, testMappa.getLatoDellaMappa())));
		assertNull(testMappa.getCella(new Coord(testMappa.getLatoDellaMappa()+1, 0)));
		assertNull(testMappa.getCella(new Coord(0, testMappa.getLatoDellaMappa()+1)));
	}
	
	private void testSpawnRimuoviDinosauro() {
		Coord tempCoord = new Coord(17, 15);
		String tipoCellaHost = testMappa.getCella(tempCoord).toString().toLowerCase();
		testMappa.spawnDinosauro(tempCoord, "myPrivateID");
		assertTrue(testMappa.getCella(tempCoord).toString().toLowerCase().equals("dinosauro"));
		assertEquals("myPrivateID", testMappa.getCella(tempCoord).getIdDelDinosauro());
		assertNotNull(testMappa.getCella(tempCoord).getCellaSuCuiSiTrova());
		assertEquals(tipoCellaHost, testMappa.getCella(tempCoord).getCellaSuCuiSiTrova().toString().toLowerCase());
		testMappa.rimuoviIlDinosauroDallaCella(tempCoord);
		assertNotNull(testMappa.getCella(tempCoord));
		assertEquals(tipoCellaHost, testMappa.getCella(tempCoord).toString().toLowerCase());
	}
	
	private void testIterator() {
		
	}
	
	private void testAggiornaSuTurno() {
		Cella tempCella = null;
	}
	
	@Test
	public void testAll() {
		testGetCella();
		testSpawnRimuoviDinosauro();
		testIterator();
		testAggiornaSuTurno();
	}
}