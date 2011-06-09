package dinolib;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import dinolib.Mappa.CellaTest;
import dinolib.Razza.DinosauroTest;
import dinolib.Razza.RazzaTest;

@RunWith(Suite.class)
@SuiteClasses({DinosauroTest.class, CellaTest.class, RazzaTest.class, LogicaTest.class})
public class DinolibTest {

	public static Test suite() {
		return null;
		
	}

}
