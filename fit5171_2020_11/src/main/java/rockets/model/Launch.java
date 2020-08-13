package rockets.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import java.util.Set;

import static org.apache.commons.lang3.Validate.*;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;


public class Launch extends Entity {
    public enum LaunchOutcome {
        FAILED, SUCCESSFUL
    }

    private LocalDate launchDate;

    private Rocket launchVehicle;

    private LaunchServiceProvider launchServiceProvider;


    private String launchSite;

    private String orbit;

    private String function;

    private BigDecimal price;

    private LaunchOutcome launchOutcome;


    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {
        notNull(launchDate, "LaunchDate cannot be null");
        this.launchDate = launchDate;
    }

    public Rocket getLaunchVehicle() {
        return launchVehicle;
    }

    public void setLaunchVehicle(Rocket launchVehicle) {
        notNull(launchVehicle, "LaunchVehicle cannot be null");
        this.launchVehicle = launchVehicle;
    }

    public LaunchServiceProvider getLaunchServiceProvider() {
        return launchServiceProvider;
    }

    public void setLaunchServiceProvider(LaunchServiceProvider launchServiceProvider) {
        notNull(launchServiceProvider, "LaunchServiceProvider cannot be null");
        this.launchServiceProvider = launchServiceProvider;
    }


    public String getLaunchSite() {
        return launchSite;
    }

    public void setLaunchSite(String launchSite) {
        notNull(launchSite, "LaunchSite cannot be null");
        notBlank(launchSite, "LaunchSite cannot be empty");
        this.launchSite = launchSite;
    }

    public String getOrbit() {
        return orbit;
    }

    public void setOrbit(String orbit) {
        notNull(orbit, "orbit cannot be null");
        notBlank(orbit, "orbit cannot be empty");
        this.orbit = orbit;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        notNull(function, "function cannot be null");
        notBlank(function, "function cannot be empty");
        this.function = function;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        notNull(price, "price cannot be null");
        this.price = price;
    }

    public LaunchOutcome getLaunchOutcome() {
        return launchOutcome;
    }

    public void setLaunchOutcome(LaunchOutcome launchOutcome) {
        notNull(launchOutcome, "LaunchOutcome cannot be null");
        this.launchOutcome = launchOutcome;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Launch launch = (Launch) o;
        return Objects.equals(launchDate, launch.launchDate) &&
                Objects.equals(launchVehicle, launch.launchVehicle) &&
                Objects.equals(launchServiceProvider, launch.launchServiceProvider) &&
                Objects.equals(orbit, launch.orbit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(launchDate, launchVehicle, launchServiceProvider, orbit);
    }
}
