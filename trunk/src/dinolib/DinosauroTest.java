package dinolib;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DinosauroTest {
	private Dinosauro dinosauro;
	
	@Before
	public void setUp() {
		dinosauro = new Erbivoro(0, 0);
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
	public void testSetXY() {
		int x = 20;
		int y = 14;
		dinosauro.setXY(x, y);
		assertEquals(x, dinosauro.getX());
		assertEquals(y, dinosauro.getY());
		x = 27;
		dinosauro.setX(x);
		assertEquals(x, dinosauro.getX());
		y = 19;
		dinosauro.setY(y);
		assertEquals(y, dinosauro.getY());
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