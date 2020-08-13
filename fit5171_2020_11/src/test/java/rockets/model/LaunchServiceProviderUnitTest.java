package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;


public class LaunchServiceProviderUnitTest {
    private LaunchServiceProvider serviceProvider;

    @BeforeEach
    public void setUp()  {
        serviceProvider= new LaunchServiceProvider("SAC",1990,"Japan");}


    @DisplayName("should throw exception when pass null to setName function")
    @Test
    public void testNameNotNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> serviceProvider.setName(null));
        assertEquals("name cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty email address to setEmail function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void testNameNotEmpty(String name) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> serviceProvider.setName(name));
        assertEquals("name cannot be null or empty", exception.getMessage());
    }



    @DisplayName("should throw exception when pass a empty country to setCountry function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void testCountryNotEmpty(String country)
    {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> serviceProvider.setCountry(country));
        assertEquals("Country cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setCountry function")
    @Test
    public void testCountryNotNull()
    {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> serviceProvider.setCountry(null));
        assertEquals("Country cannot be null or empty", exception.getMessage());
    }


    @DisplayName("should throw exception when pass empty to setYear")
    @ParameterizedTest
    @ValueSource(ints = 0)
    public void testYearFoundedNotEmpty(int yearFounded){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> serviceProvider.setYear(yearFounded));
        assertEquals("Year cannot be empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5,-10})
    public void testYearNotNegative(int yearFounded) {
        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class, () -> serviceProvider.setYear(yearFounded));
        assertEquals("Year cannot be negative", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setRockets")
    @Test
    public void testRockets()
    {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> serviceProvider.setRockets(null));
        assertEquals("Rockets cannot be null", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setHeadquarters function")
    @Test
    public void testHeadquartersNotNullOrEmpty(){
        try{
            serviceProvider.setHeadquarters(null);
            fail("Headquarters cannot be null");
        } catch (NullPointerException e)
        {
            assertTrue(e.getMessage().contains("null"));
        }

        try{
            serviceProvider.setHeadquarters("");
            fail("Headquarters cannot be empty");
        } catch(Exception e)
        {
            assertTrue(e.getMessage().contains("empty"));
        }
    }

    @DisplayName("should throw exception when Service provider are different ")
    @Test
    public void testDiffServiceProvider()
    {
        LaunchServiceProvider serviceProvider2 = new LaunchServiceProvider("MyProvider", 2020, "CHINA");
        assertFalse(serviceProvider.equals(serviceProvider2));

    }
}
