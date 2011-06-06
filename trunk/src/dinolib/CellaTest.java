package dinolib;

import static org.junit.Assert.*;

import org.junit.Test;

public class CellaTest {
	private Cella cella;

	
	private void testCommon() {
		assertNull(cella.getIdDelDinosauro());
		assertNull(cella.getCellaSuCuiSiTrova());
	}
	
	private void testCommonVuote() {
		testCommon();
		assertEquals(0, cella.getValoreAttuale());
	}
	
	private void testAcqua() {
		cella = new Acqua();
		assertEquals("acqua", cella.toString());
		testCommonVuote();
	}
	
	private void testTerra() {
		cella = new Terra();
		assertEquals("terra", cella.toString());
		testCommonVuote();
	}
	/**
	 * Testa le celle vuote come acqua o terra.
	 */
	@Test
	public void testVuote() {
		testAcqua();
		testTerra();
	}
	
	private void testCellaConDinosauro() {
		String idDino = "abcd";
		cella = new CellaConDinosauro(idDino, new Terra());
		assertEquals("dinosauro", cella.toString());
		assertNotNull(cella.getIdDelDinosauro());
		assertEquals(idDino, cella.getIdDelDinosauro());
		assertNotNull(cella.getCellaSuCuiSiTrova());
		assertEquals(0, cella.getValoreAttuale());
	}
	
	private void testVegetazione() {
		cella = new Vegetazione(400);
		assertEquals("vegetazione", cella.toString());
		testCommon();
		int initValue = cella.getValoreAttuale();
		cella.mangia(100);
		assertEquals(initValue-100, cella.getValoreAttuale());
		initValue = cella.getValoreAttuale();
		cella.aggiornaCellaSulTurno();
		assertTrue((cella.getValoreAttuale() > initValue));
		cella.mangia(1000);
		assertEquals(0, cella.getValoreAttuale());
	}
	
	private void testCarogna() {
		cella = new Carogna(400);
		assertEquals("carogna", cella.toString());
		testCommon();
		int initValue = cella.getValoreAttuale();
		cella.aggiornaCellaSulTurno();
		assertTrue((cella.getValoreAttuale() < initValue));
		initValue = cella.getValoreAttuale();
		cella.mangia(100);
		assertEquals(initValue-100, cella.getValoreAttuale());
		cella.mangia(1000);
		assertEquals(0, cella.getValoreAttuale());
	}
	
	/**
	 * Testa le celle che contengono qualcosa come dinosauro, vegetazione e carogna.
	 */
	@Test
	public void testPiene() {
		testCellaConDinosauro();
		testVegetazione();
		testCarogna();
	}
}