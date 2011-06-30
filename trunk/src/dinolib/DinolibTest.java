package dinolib;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import dinolib.GameCollections.ConnectionManagerTest;
import dinolib.GameCollections.PlayerManagerTest;
import dinolib.GameCollections.RRScheduler;
import dinolib.GameObjects.CellaTest;
import dinolib.GameObjects.CoordTest;
import dinolib.GameObjects.DinosauroTest;
import dinolib.GameObjects.GiocatoreTest;
import dinolib.GameObjects.MappaTest;
import dinolib.GameObjects.PunteggioTest;
import dinolib.GameObjects.RazzaTest;

@RunWith(Suite.class)
@SuiteClasses({DinosauroTest.class,
	RazzaTest.class,
	CoordTest.class,
	CellaTest.class,
	MappaTest.class,
	PunteggioTest.class,
	RRScheduler.class,
	GiocatoreTest.class,
	PlayerManagerTest.class,
	ConnectionManagerTest.class,
	LogicaTest.class})
	
public class DinolibTest {
}
