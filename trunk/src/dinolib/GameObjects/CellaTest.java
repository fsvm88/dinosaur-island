package dinolib.GameObjects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CellaTest {
	private Cella cella;

	@Before
	public void setUp() {
		cella = null;
	}
	
	@Test
	public void testAcqua() {
		cella = new Acqua();
		assertEquals("Acqua", cella.toString());
		assertEquals(0, cella.getValoreAttuale());
		assertNull(cella.getIdDelDinosauro());
		assertNull(cella.getCellaSuCuiSiTrova());
	}
	
	@Test
	public void testTerra() {
		cella = new Terra();
		assertEquals("Terra", cella.toString());
		assertEquals(0, cella.getValoreAttuale());
		assertNull(cella.getIdDelDinosauro());
		assertNull(cella.getCellaSuCuiSiTrova());
	}
	
	@Test
	public void testCellaConDinosauro() {
		String idDino = "abcd";
		cella = new CellaConDinosauro(idDino, new Terra());
		assertEquals("Dinosauro", cella.toString());
		assertNotNull(cella.getIdDelDinosauro());
		assertEquals(idDino, cella.getIdDelDinosauro());
		assertNotNull(cella.getCellaSuCuiSiTrova());
		assertEquals(0, cella.getValoreAttuale());
	}
	
	@Test
	public void testVegetazione() {
		cella = new Vegetazione(400);
		assertEquals("Vegetazione", cella.toString());
		assertNull(cella.getIdDelDinosauro());
		assertNull(cella.getCellaSuCuiSiTrova());
		int initValue = cella.getValoreAttuale();
		cella.mangia(100);
		assertEquals(initValue-100, cella.getValoreAttuale());
		initValue = cella.getValoreAttuale();
		cella.aggiorna();
		assertTrue((cella.getValoreAttuale() > initValue));
		cella.mangia(1000);
		assertEquals(0, cella.getValoreAttuale());
	}
	
	@Test
	public void testCarogna() {
		cella = new Carogna(400);
		assertEquals("Carogna", cella.toString());
		assertNull(cella.getIdDelDinosauro());
		assertNull(cella.getCellaSuCuiSiTrova());
		int initValue = cella.getValoreAttuale();
		cella.aggiorna();
		assertTrue((cella.getValoreAttuale() < initValue));
		initValue = cella.getValoreAttuale();
		cella.mangia(100);
		assertEquals(initValue-100, cella.getValoreAttuale());
		cella.mangia(1000);
		assertEquals(0, cella.getValoreAttuale());
	}
}