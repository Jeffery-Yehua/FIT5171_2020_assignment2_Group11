package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class RocketUnitTest {
    private Rocket target;
    private String name = "trueName";

    private String country = "china";
    private LaunchServiceProvider manufacturer = new LaunchServiceProvider("ULA", 1990, "USA");

    @BeforeEach
    public void setUp() {
        target = new Rocket(name,country,manufacturer);
    }

    @DisplayName("should throw exception when pass null to Rocket name")
    @Test
    public void shouldThrowExceptionWhenNameIsNull() {
        String name =  null;
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new Rocket(name,country,manufacturer));
        assertEquals("Rocket name cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to Rocket country")
    @Test
    public void shouldThrowExceptionWhenCountryIsNull() {
        String country = null;
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new Rocket(name,country,manufacturer));
        assertEquals("Rocket country cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to Rocket manufacturer")
    @Test
    public void shouldThrowExceptionWhenManufacturerIsNull() {
        LaunchServiceProvider manufacturer = null;
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new Rocket(name,country,manufacturer));
        assertEquals("Rocket manufacturer cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty name to Rocket name")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenNameIsEmpty(String name) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Rocket(name,country,manufacturer));
        assertEquals("Rocket name cannot be null or empty", exception.getMessage());
    }


    @DisplayName("should throw exception when pass a empty country to Rocket country")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenCountryIsEmpty(String country) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Rocket(name,country,manufacturer));
        assertEquals("Rocket country cannot be null or empty", exception.getMessage());
    }

    //unit test that checks the mass to leo/gto/other can not be set to null
    @DisplayName("should throw exception when pass null to mass to leo/gto/other function")
    @Test
    public void shouldThrowExceptionWhenMassToLeoGtoOtherIsNull() {
        NullPointerException exceptionLeo = assertThrows(NullPointerException.class, () -> target.setMassToLEO(null));
        NullPointerException exceptionGto = assertThrows(NullPointerException.class, () -> target.setMassToGTO(null));
        NullPointerException exceptionOther = assertThrows(NullPointerException.class, () -> target.setMassToOther(null));
        assertEquals("mass to leo cannot be null", exceptionLeo.getMessage());
        assertEquals("mass to gto cannot be null", exceptionGto.getMessage());
        assertEquals("mass to other cannot be null", exceptionOther.getMessage());
    }


    //unit test that checks if the mass to leo is valid
    @DisplayName("should be a valid mass to leo")
    @ParameterizedTest
    @ValueSource(strings = {"-1", "0", "1.1", "200001"})
    public void shouldThrowExceptionWhenMassIsNotValid(String mass) {
        IllegalArgumentException exceptionLeo = assertThrows(IllegalArgumentException.class, () -> target.setMassToLEO(mass));
        IllegalArgumentException exceptionGto= assertThrows(IllegalArgumentException.class, () -> target.setMassToGTO(mass));
        IllegalArgumentException exceptionOther = assertThrows(IllegalArgumentException.class, () -> target.setMassToOther(mass));
        assertEquals("Mass should be an integer greater than 0 and less than 200000", exceptionLeo.getMessage());
        assertEquals("Mass should be an integer greater than 0 and less than 200000", exceptionGto.getMessage());
        assertEquals("Mass should be an integer greater than 0 and less than 200000", exceptionOther.getMessage());
    }

    @DisplayName("should return true when two rockets are same")
    @Test
    public void shouldReturnTrueWhenRocketsAreTheSame() {
        Rocket rocket2 = new Rocket(name,country,manufacturer);
        assertTrue(target.equals(rocket2));
    }

    @DisplayName("should return false when two rockets are different")
    @Test
    public void shouldReturnFalseWhenRocketsAreDifferent() {
        String name2 = "falseName";
        String country2 = "Australia";
        LaunchServiceProvider manufacturer2 = new LaunchServiceProvider("MyProvider", 2020, "CHINA");
        Rocket rocket2 = new Rocket(name2, country, manufacturer);
        Rocket rocket3 = new Rocket(name, country2, manufacturer);
        Rocket rocket4 = new Rocket(name, country, manufacturer2);
        assertFalse(target.equals(rocket2));
        assertFalse(target.equals(rocket3));
        assertFalse(target.equals(rocket4));
    }
}


