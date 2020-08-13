package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class LaunchTest {

    private Launch launch;

    @BeforeEach
    public void setUp() {
        launch = new Launch();
    }



    @DisplayName("should throw exception when pass null to setLaunchOutcome")
    @Test
    public void testLaunchOutcomeNotNull()
    {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> launch.setLaunchOutcome(null));
        assertEquals("LaunchOutcome cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setLaunchDate")
    @Test
    public void testLaunchDateNotNull()
    {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> launch.setLaunchDate(null));
        assertEquals("LaunchDate cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setLaunchDate")
    @Test
    public void testLaunchPriceNotNull()
    {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> launch.setPrice(null));
        assertEquals("price cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setLaunchVehicle")
    @Test
    public void testLaunchVehicleNotNull()
    {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> launch.setLaunchVehicle(null));
        assertEquals("LaunchVehicle cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setLaunchServiceProvider")
    @Test
    public void testLaunchServiceProviderNotNull()
    {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> launch.setLaunchServiceProvider(null));
        assertEquals("LaunchServiceProvider cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass empty to setYear")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void testLaunchSiteNotEmpty(String s){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> launch.setLaunchSite(s));
        assertEquals("LaunchSite cannot be empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass empty to setYear")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void testOrbitNotEmpty(String o){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> launch.setOrbit(o));
        assertEquals("orbit cannot be empty", exception.getMessage());
    }
    @DisplayName("should throw exception when pass empty to setYear")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void testFunctionNotEmpty(String f){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> launch.setFunction(f));
        assertEquals("function cannot be empty", exception.getMessage());
    }
    @Test
    public void testSetOutcomeSucceed(){
        launch.setLaunchOutcome(Launch.LaunchOutcome.SUCCESSFUL);
        assertTrue(launch.getLaunchOutcome().equals(Launch.LaunchOutcome.SUCCESSFUL));
    }
}