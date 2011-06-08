package dinolib;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LogicaTest {
	private Logica myLogica = null;
	
	private void addSomeUsers() throws UserExistsException {
		myLogica.doCreaUtente("abc", "pwd");
		myLogica.doCreaUtente("abd", "pwd2");
		myLogica.doCreaUtente("abe", "pwd3");
	}
	private void mySetUp() throws Exception {
		setUp();
		addSomeUsers();
	}
	
	@Before
	public void setUp() throws Exception {
		myLogica = new Logica();
	}
	
	private void testLogica() throws Exception {
		setUp();
		assertNotNull(myLogica);
	}
	
	private void testGetLatoDellaMappa() {
		assertNotNull(myLogica.getLatoDellaMappa());
		assertEquals(40, myLogica.getLatoDellaMappa());
	}
	public void testGetCella() {
		Cella tempCella = myLogica.getCella(0, 0);
		assertNotNull(tempCella);
	}
	
	@Test
	public void testAllLogica() throws Exception {
		testLogica();
		testGetLatoDellaMappa();
		testGetCella();
		mySetUp();
	}	
}