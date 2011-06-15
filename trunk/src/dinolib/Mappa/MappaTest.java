package dinolib.Mappa;

import static org.junit.Assert.*;

import java.util.Iterator;

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
		assertNotNull(testMappa.iterator());
		Iterator<Cella> itCelle = testMappa.iterator();
		assertTrue(itCelle.hasNext());
		Cella tempCella = null;
		int i = 0;
		int j = 0;
		int counter = 0;
		assertNull(tempCella);
		while (i<testMappa.getLatoDellaMappa() && itCelle.hasNext()) {
			j = 0;
			while (j<testMappa.getLatoDellaMappa() && itCelle.hasNext()) {
				counter++;
				tempCella = itCelle.next();
				assertNotNull(tempCella);
				j++;
			}
			i++;
		}
		assertFalse(itCelle.hasNext());
	}

	private void testSubIterator() {
		Coord tempCoord = new Coord(15,20);
		assertNotNull(testMappa.subIterator(tempCoord, 5, 5));
		Iterator<Cella> itCelle = testMappa.subIterator(tempCoord, 5, 5);
		assertTrue(itCelle.hasNext());
		Cella tempCella = null;
		int i = 0;
		int j = 0;
		int counter = 0;
		assertNull(tempCella);
		while (i<=5 && itCelle.hasNext()) {
			j = 0;
			while (j<=5 && itCelle.hasNext()) {
				counter++;
				tempCella = itCelle.next();
				assertNotNull(tempCella);
				j++;
			}
			i++;
		}
		assertFalse(itCelle.hasNext());
	}

	private void testAggiornaSuTurno() {
		Cella tempCella = null;
		Iterator<Cella> itCelle = testMappa.iterator();
		while (itCelle.hasNext()) {
			tempCella = itCelle.next();
			if (tempCella.toString().toLowerCase().equals("carogna")){
				break;
			}
		}
		assertTrue(tempCella.toString().toLowerCase().equals("carogna"));
		int prevValue = tempCella.getValoreAttuale();
		testMappa.aggiorna();
		if (prevValue == tempCella.getValoreAttuale()) fail();
	}

	@Test
	public void testAll() {
		testGetCella();
		testSpawnRimuoviDinosauro();
		testIterator();
		testSubIterator();
		testAggiornaSuTurno();
	}
}