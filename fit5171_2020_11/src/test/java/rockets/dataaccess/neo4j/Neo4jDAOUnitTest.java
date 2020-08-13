package rockets.dataaccess.neo4j;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;
import org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import rockets.dataaccess.DAO;
import rockets.model.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Neo4jDAOUnitTest {
    private DAO dao;
    private Session session;
    private SessionFactory sessionFactory;

    private LaunchServiceProvider longMarch;
    private LaunchServiceProvider spacex;
    private Rocket rocket;
    private Launch launch;
    private User user;
    @BeforeAll
    public void initializeNeo4j() {
        ServerControls embeddedDatabaseServer = TestServerBuilders.newInProcessBuilder().newServer();
        GraphDatabaseService dbService = embeddedDatabaseServer.graph();
        EmbeddedDriver driver = new EmbeddedDriver(dbService);
        sessionFactory = new SessionFactory(driver, User.class.getPackage().getName());
        session = sessionFactory.openSession();
        dao = new Neo4jDAO(session);
    }

    @BeforeEach
    public void setup() {
        longMarch = new LaunchServiceProvider("LongMarch", 1970, "China");
        spacex = new LaunchServiceProvider("SpaceX", 2002, "USA");
        rocket = new Rocket("ShenZhou5", "China", longMarch);
        launch = new Launch();
        launch.setLaunchVehicle(rocket);
        user = new User();
    }

    @Test
    public void shouldCreateNeo4jDAOSuccessfully() {
        assertNotNull(dao);
    }


    @Test
    public void shouldCreateARocketSuccessfully() {
        rocket.setWikilink("https://en.wikipedia.org/wiki/Shenzhou_5");
        Rocket graphRocket = dao.createOrUpdate(rocket);
        assertNotNull(graphRocket.getId());
        assertEquals(rocket, graphRocket);
        assertNotNull(graphRocket.getManufacturer().getId());
        assertEquals(rocket.getWikilink(), graphRocket.getWikilink());
        assertEquals(rocket.getManufacturer(), graphRocket.getManufacturer());
    }

    @Test
    public void shouldUpdateRocketAttributeSuccessfully() {
        rocket.setWikilink("https://en.wikipedia.org/wiki/Falcon_9");

        Rocket graphRocket = dao.createOrUpdate(rocket);
        assertNotNull(graphRocket.getId());
        assertEquals(rocket, graphRocket);

        String newLink = "http://adifferentlink.com";
        rocket.setWikilink(newLink);
        dao.createOrUpdate(rocket);
        graphRocket = dao.load(Rocket.class, rocket.getId());
        assertEquals(newLink, graphRocket.getWikilink());
    }

    @Test
    public void shouldNotSaveTwoSameRockets() {
        assertNull(spacex.getId());

        Rocket rocket1 = new Rocket("CN1", "CN", spacex);
        Rocket rocket2 = new Rocket("CN1", "CN", spacex);
        assertEquals(rocket1, rocket2);
        dao.createOrUpdate(rocket1);
        assertNotNull(spacex.getId());
        Collection<Rocket> rockets = dao.loadAll(Rocket.class);
        assertEquals(1, rockets.size());
        Collection<LaunchServiceProvider> manufacturers = dao.loadAll(LaunchServiceProvider.class);
        assertEquals(1, manufacturers.size());
        dao.createOrUpdate(rocket2);
        manufacturers = dao.loadAll(LaunchServiceProvider.class);
        assertEquals(1, manufacturers.size());
        rockets = dao.loadAll(Rocket.class);
        assertEquals(1, rockets.size());
    }

    @Test
    public void shouldLoadAllRockets() {
        Set<Rocket> rockets = Sets.newHashSet(
                new Rocket("ShenZhou5", "China", longMarch),
                new Rocket("Falcon9", "USA", spacex)
        );

        for (Rocket r : rockets) {
            dao.createOrUpdate(r);
        }

        Collection<Rocket> loadedRockets = dao.loadAll(Rocket.class);
        assertEquals(rockets.size(), loadedRockets.size());
        for (Rocket r : rockets) {
            assertTrue(loadedRockets.contains(r));
        }
    }

    @Test
    public void shouldCreateALaunchSuccessfully() {
        Launch launch = new Launch();
        launch.setLaunchDate(LocalDate.of(2016, 6, 1));
        launch.setLaunchVehicle(rocket);
        launch.setLaunchSite("VAFB");
        launch.setOrbit("LEO");
        dao.createOrUpdate(launch);

        Collection<Launch> launches = dao.loadAll(Launch.class);
        assertFalse(launches.isEmpty());
        assertTrue(launches.contains(launch));
    }


    @Test
    public void shouldUpdateLaunchAttributesSuccessfully() {
        Launch launch = new Launch();
        launch.setLaunchDate(LocalDate.of(2016, 6, 1));
        launch.setLaunchVehicle(rocket);
        launch.setLaunchSite("VAFB");
        launch.setOrbit("LEO");
        dao.createOrUpdate(launch);

        Collection<Launch> launches = dao.loadAll(Launch.class);

        Launch loadedLaunch = launches.iterator().next();
        assertNull(loadedLaunch.getFunction());

        launch.setFunction("experimental");
        dao.createOrUpdate(launch);
        launches = dao.loadAll(Launch.class);
        assertEquals(1, launches.size());
        loadedLaunch = launches.iterator().next();
        assertEquals("experimental", loadedLaunch.getFunction());
    }

    @Test
    public void shouldDeleteRocketWithoutDeleteLSP() {
        dao.createOrUpdate(rocket);
        assertNotNull(rocket.getId());
        assertNotNull(rocket.getManufacturer().getId());
        assertFalse(dao.loadAll(Rocket.class).isEmpty());
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
        dao.delete(rocket);
        assertTrue(dao.loadAll(Rocket.class).isEmpty());
        assertFalse(dao.loadAll(LaunchServiceProvider.class).isEmpty());
    }

    //be able to delete launch without deleting rocket
    @Test
    public void shouldDeleteLaunchWithoutDeleteRocket(){
        dao.createOrUpdate(launch);
        assertNotNull(launch.getId());
        assertNotNull(launch.getLaunchVehicle().getId());
        assertFalse(dao.loadAll(Launch.class).isEmpty());
        assertFalse(dao.loadAll(Rocket.class).isEmpty());
        dao.delete(launch);
        assertTrue(dao.loadAll(Launch.class).isEmpty());
        assertFalse(dao.loadAll(Rocket.class).isEmpty());

    }



    //be able to delete the rocket
    @Test
    public void shouldDeleteRocket(){
        dao.createOrUpdate(rocket);
        assertNotNull(rocket.getId());
        assertFalse(dao.loadAll(Rocket.class).isEmpty());
        dao.delete(rocket);
        assertTrue(dao.loadAll(Rocket.class).isEmpty());
    }

    //be able to delete the user
    @Test
    public void shouldDeleteUser(){
        User user = new User();
        dao.createOrUpdate(user);
        assertNotNull(user.getId());
        assertFalse(dao.loadAll(User.class).isEmpty());
        dao.delete(user);
        assertTrue(dao.loadAll(User.class).isEmpty());
    }

    //be able to delete the Launch
    @Test
    public void shouldDeleteLaunch()
    {
        dao.createOrUpdate(launch);
        assertNotNull(launch.getId());
        assertFalse( dao.loadAll(Launch.class).isEmpty());
        dao.delete(launch);
        assertTrue( dao.loadAll(Launch.class).isEmpty());
    }

    //be able to delete the LaunchServiceProvider
    @Test
    public void shouldDeleteLSP()
    {
        dao.createOrUpdate(spacex);
        assertNotNull(spacex.getId());
        assertFalse( dao.loadAll(LaunchServiceProvider.class).isEmpty());
        dao.delete(spacex);
        assertTrue( dao.loadAll(LaunchServiceProvider.class).isEmpty());
    }

    @AfterEach
    public void tearDown() {
        session.purgeDatabase();
    }

    @AfterAll
    public void closeNeo4jSession() {
        session.clear();
        sessionFactory.close();
    }
}