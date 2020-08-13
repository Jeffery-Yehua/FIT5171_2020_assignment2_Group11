package rockets.model;

import com.google.common.collect.Sets;

import java.util.Objects;
import java.util.Set;


import static org.apache.commons.lang3.Validate.*;
import static org.apache.commons.lang3.Validate.notBlank;


public class LaunchServiceProvider extends Entity {
    private String name;

    private int yearFounded;

    private String country;

    private String headquarters;

    private Set<Rocket> rockets;

    /**
     * All parameters shouldn't be null.
     *
     * @param name
     * @param yearFounded
     * @param country
     * @parm headquarters
     */

    public LaunchServiceProvider(String name, int yearFounded, String country) {
        notNull(name);
        notBlank(name);
        notNaN(yearFounded);
        notBlank(country);
        notNull(country);


        this.name = name;
        this.yearFounded = yearFounded;
        this.country = country;

        rockets = Sets.newLinkedHashSet();
    }

    public String getName() {
        return name;
    }

    public int getYearFounded() {
        return yearFounded;
    }

    public String getCountry() {
        return country;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public Set<Rocket> getRockets() {
        return rockets;
    }

    public void setHeadquarters(String headquarters) {
        if(headquarters == null) throw new NullPointerException("Head quarters cannot be null");
        else if(headquarters.trim().isEmpty()) throw new IllegalArgumentException("Head quarters cannot be empty");
        this.headquarters = headquarters;
    }

    public void setRockets(Set<Rocket> rockets)
    {
        if(rockets == null) throw new NullPointerException("Rockets cannot be null");
        this.rockets = rockets;
    }

    public void setName(String name) {
        notBlank((name), "name cannot be null or empty");
        notNull((name), "name cannot be null or empty");
        this.name = name;
    }

    public void setCountry(String country)
    {
        notBlank((country), "Country cannot be null or empty");
        notNull((country), "Country cannot be null or empty");
        this.country = country;
    }



    public void setYear(int yearFounded) {
        if(yearFounded==0 ) throw new IllegalArgumentException("Year cannot be empty");
        else if( yearFounded <0 ) throw new IndexOutOfBoundsException("Year cannot be negative");
        this.yearFounded = yearFounded;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaunchServiceProvider that = (LaunchServiceProvider) o;
        return yearFounded == that.yearFounded &&
                Objects.equals(name, that.name) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, yearFounded, country);
    }
}
