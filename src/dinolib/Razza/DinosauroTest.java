package dinolib.Razza;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dinolib.Mappa.Coord;


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
		setUp();
	}
	
	@Test
	public void testIsAtDimensioneMax() {
		assertFalse(dinosauro.isAtDimensioneMax());
		ageDinosauro();
		assertTrue(dinosauro.isAtDimensioneMax());
		setUp();
	}
	
	@Test
	public void testUsabilita() {
		assertTrue(dinosauro.isUsabile());
		dinosauro.nonUsabile();
		assertFalse(dinosauro.isUsabile());
		dinosauro.usabile();
		assertTrue(dinosauro.isUsabile());
		setUp();
	}
	
	@Test
	public void testSetEnergiaAttuale() {
		int energiaAttuale = dinosauro.getEnergiaAttuale();
		dinosauro.setEnergiaAttuale(energiaAttuale+1);
		assertEquals(energiaAttuale+1, dinosauro.getEnergiaAttuale());
		setUp();
	}
	
	@Test
	public void testSetCoord() {
		Coord tc1 = new Coord(20, 14);
		dinosauro.setCoord(tc1);
		assertEquals(tc1, dinosauro.getCoord());
		Coord tc2 = new Coord(27, 19);
		dinosauro.setCoord(tc2);
		assertEquals(tc2, dinosauro.getCoord());
		setUp();
	}
	
	@Test
	public void testInvecchia() {
		int curVita = dinosauro.getTurnoDiVita();
		dinosauro.invecchia();
		assertEquals(curVita+1, dinosauro.getTurnoDiVita());
		setUp();
	}
	
	@Test
	public void testToString() {
		assertEquals("Erbivoro", dinosauro.toString());
	}
	
	@Test
	public void testHasEnergyMethods() {
		ageDinosauro();
		dinosauro.setEnergiaAttuale(dinosauro.getEnergiaAttuale());
		assertTrue(dinosauro.hasEnergyToGrow());
		assertTrue(dinosauro.hasEnergyToRepl());
		dinosauro.setEnergiaAttuale(1);
		assertFalse(dinosauro.hasEnergyToGrow());
		assertFalse(dinosauro.hasEnergyToRepl());
		setUp();
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
		setUp();
	}
}