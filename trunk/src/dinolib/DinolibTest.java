package dinolib;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import dinolib.Mappa.CellaTest;
import dinolib.Mappa.CoordTest;
import dinolib.Mappa.MappaTest;
import dinolib.Razza.DinosauroTest;
import dinolib.Razza.RazzaTest;

@RunWith(Suite.class)
@SuiteClasses({DinosauroTest.class,
	RazzaTest.class,
	CoordTest.class,
	CellaTest.class,
	MappaTest.class,
	PunteggioTest.class,
	GiocatoreTest.class,
	PlayerManagerTest.class,
	ConnectionManagerTest.class,
	RRScheduler.class,
	LogicaTest.class})
	
public class DinolibTest {

	public static Test suite() {
		return null;
		
	}

}
