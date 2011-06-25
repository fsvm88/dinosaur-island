package dinolib.GameObjects;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;



/**
 * @author  fabio
 */
public class DinosauroTest {
	/**
	 * @uml.property  name="dinosauro"
	 * @uml.associationEnd  
	 */
	private Dinosauro dinosauro;
	private final Coord defaultCoord = new Coord(0, 0);
	
	@Before
	public void setUp() {
		dinosauro = new Erbivoro(defaultCoord);
	}
	
	// Fa crescere il dinosauro fino alla dimensione massima, helper per tutti gli altri test.
	private void ageDinosauro() {
		while (!dinosauro.isAtDimensioneMax()) {
			dinosauro.setEnergiaAttuale(dinosauro.getEnergiaMax());
			dinosauro.cresci();
		}
	}
	
	@Test
	public void testCresci() {
		if (!dinosauro.isAtDimensioneMax()) {
			int curDimensione = dinosauro.getDimensione();
			dinosauro.cresci();
			assertEquals(curDimensione+1, dinosauro.getDimensione());
		}
		ageDinosauro();
		if (dinosauro.isAtDimensioneMax()) {
			int curDimensione = dinosauro.getDimensione();
			dinosauro.cresci();
			assertEquals(curDimensione, dinosauro.getDimensione());
		}
	}
	
	@Test
	public void testIsAtDimensioneMax() {
		assertFalse(dinosauro.isAtDimensioneMax());
		ageDinosauro();
		assertTrue(dinosauro.isAtDimensioneMax());
	}
	
	@Test
	public void testUsabilita() {
		assertTrue(dinosauro.isUsabile());
		dinosauro.nonUsabile();
		assertFalse(dinosauro.isUsabile());
		dinosauro.usabile();
		assertTrue(dinosauro.isUsabile());
	}
	
	@Test
	public void testSetEnergiaAttuale() {
		int energiaAttuale = dinosauro.getEnergiaAttuale();
		dinosauro.setEnergiaAttuale(energiaAttuale+1);
		assertEquals(energiaAttuale+1, dinosauro.getEnergiaAttuale());
	}
	
	@Test
	public void testSetCoord() {
		Coord tc1 = new Coord(20, 14);
		dinosauro.setCoord(tc1);
		assertEquals(tc1, dinosauro.getCoord());
		Coord tc2 = new Coord(27, 19);
		dinosauro.setCoord(tc2);
		assertEquals(tc2, dinosauro.getCoord());
	}
	
	@Test
	public void testInvecchia() {
		int curVita = dinosauro.getTurnoDiVita();
		dinosauro.invecchia();
		assertEquals(curVita+1, dinosauro.getTurnoDiVita());
	}
	
	@Test
	public void testToString() {
		assertEquals("Erbivoro", dinosauro.toString());
	}
	
	@Test
	public void testHasEnergyMethods() {
		ageDinosauro();
		dinosauro.setEnergiaAttuale(dinosauro.getEnergiaMax());
		assertTrue(dinosauro.hasEnergyToGrow());
		assertTrue(dinosauro.hasEnergyToRepl());
		dinosauro.setEnergiaAttuale(1);
		assertFalse(dinosauro.hasEnergyToGrow());
		assertFalse(dinosauro.hasEnergyToRepl());
	}
	
	@Test
	public void testGetTipoRazza() {
		assertEquals("Erbivoro", dinosauro.getTipoRazza());
	}
	
	@Test
	public void testGetRangeVista() {
		assertEquals(2, dinosauro.getRangeVista());
		dinosauro.cresci();
		assertEquals(3, dinosauro.getRangeVista());
		ageDinosauro();
		assertEquals(4, dinosauro.getRangeVista());
	}
}