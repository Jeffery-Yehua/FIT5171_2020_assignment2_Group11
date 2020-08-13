package rockets.mining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.model.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class RocketMiner {
    private static Logger logger = LoggerFactory.getLogger(RocketMiner.class);

    private DAO dao;

    public RocketMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * TODO: to be implemented & tested!
     * Returns the top-k most active rockets, as measured by number of completed launches.
     *
     * @param k the number of rockets to be returned.
     * @return the list of k most active rockets.
     */
    public List<Rocket> mostLaunchedRockets(int k) {
        Collection<Launch> launches = dao.loadAll(Launch.class);
        List<Launch> launchList = new ArrayList<>(launches);
        List<Rocket> sortedRockets = getSortedRocketsByLaunches(launchList);
        List<Rocket> topRockets = new ArrayList<>();
        int i = 0;
        for (Rocket rocket : sortedRockets){
            topRockets.add(rocket);
            i++;
            if(i >= k)
                break;
        }
        return topRockets;
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most reliable launch service providers as measured
     * by percentage of successful launches.
     *
     * @param k the number of launch service providers to be returned.
     * @return the list of k most reliable ones.
     */
    public List<LaunchServiceProvider> mostReliableLaunchServiceProviders(int k) {
        logger.info("find most Reliable LaunchServiceProviders  " + k + " launches");
        Collection<LaunchServiceProvider> lsps = dao.loadAll(LaunchServiceProvider.class);
        Collection<Launch> launches = dao.loadAll(Launch.class);
        List<Launch> launchList = new ArrayList<>(launches);
        List<LaunchServiceProvider> lspList = new ArrayList<>(lsps);
        Map<LaunchServiceProvider, Double> lspMap = new LinkedHashMap<>();
        double successfulLaunches=0.0;
        double allLaunches=0.0;
        // double failedLaunches=0;
        for(LaunchServiceProvider launchServiceProvider : lspList)
        {
            for(Launch launch : launchList)
            {
                if(launch.getLaunchOutcome().equals(Launch.LaunchOutcome.SUCCESSFUL))
                {
                    successfulLaunches++;
                }
                allLaunches++;
            }
            if(allLaunches!=0) {
                double ratio = successfulLaunches / allLaunches;
                lspMap.put(launchServiceProvider, ratio);
            }

        }
        Map<LaunchServiceProvider,Double> sortedLsp = lspMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(k)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));
        List<LaunchServiceProvider> topLSP = new ArrayList<>();
        for (LaunchServiceProvider lsp : sortedLsp.keySet()){
            topLSP.add(lsp);
        }
        return topLSP;
    }
    /**
     * <p>
     * Returns the top-k most recent launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most recent launches.
     */
    public List<Launch> mostRecentLaunches(int k) {
        logger.info("find most recent " + k + " launches");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Comparator<Launch> launchDateComparator = (a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate());
        return launches.stream().sorted(launchDateComparator).limit(k).collect(Collectors.toList());
    }
    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the dominant country who has the most launched rockets in an orbit.
     *
     * @param orbit the orbit
     * @return the country who sends the most payload to the orbit
     */
    public String dominantCountry(String orbit) {
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Collection<Rocket> rockets = dao.loadAll(Rocket.class);
        List<Launch> launchList = new ArrayList<>(launches);
        List<Rocket> rocketList = new ArrayList<>(rockets);
        Map<String, Integer> countryMap = new HashMap<>();
        for(Launch launch: launchList) {
            if (launch.getOrbit().equals(orbit)) {
                Rocket rocket = launch.getLaunchVehicle();
                if (countryMap.containsKey(launch.getLaunchVehicle().getCountry())) {
                    String country = rocket.getCountry();
                    int   number_launches = countryMap.get(country);
                    countryMap.put(country, ++number_launches);
                }
                else
                {
                    countryMap.put(rocket.getCountry(),1);
                }
            }
        }
        Map<String, Integer> sortedRocket = countryMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));
        String topRockets = sortedRocket.toString();
        System.out.println(topRockets);
        //for (String country : sortedRocket.keySet()){
        //topRockets.valueOf(country);
        //}
        Map.Entry<String,Integer> entry = countryMap.entrySet().iterator().next();
        String country = entry.getKey();
        return country;
    }
    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most expensive launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most expensive launches.
     */
    public List<Launch> mostExpensiveLaunches(int k) {
        logger.info("find most expensive " + k + " launches");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Comparator<Launch> launchPriceComparator = (a, b) -> -a.getPrice().compareTo(b.getPrice());
        return launches.stream().sorted(launchPriceComparator).limit(k).collect(Collectors.toList());
    }


    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns a list of launch service provider that has the top-k highest
     * sales revenue in a year.
     *
     * @param k the number of launch service provider.
     * @param year the year in request
     * @return the list of k launch service providers who has the highest sales revenue.
     */
    public List<LaunchServiceProvider> highestRevenueLaunchServiceProviders(int k, int year) {
        logger.info("find top " + k + " highest sales in the year "+year);
        Collection<Launch> launches = dao.loadAll(Launch.class);
        // GROUP BY LAUNCH SERVICE PROVIDER WITH REVENUE AMOUNT
        Map<LaunchServiceProvider, BigDecimal> mapByLsp = getRevenuePerLspInYear(launches,year);
        // GET SORTED LIST OF LSPs
        List<LaunchServiceProvider> sortedLsps = getSortedLspByRevenue(mapByLsp);
        int i = 0;
        List<LaunchServiceProvider> topKlsps = new ArrayList<>();
        for (LaunchServiceProvider lsp : sortedLsps){
            i++;
            topKlsps.add(lsp);
            if(i >= k)
            { break;}
        }

        return topKlsps;
    }



    public static Map<LaunchServiceProvider, BigDecimal> getRevenuePerLspInYear(Collection<Launch> launches, int year){
        // FILTER LAUNCHES PER YEAR
        List<Launch> filteredLaunchList = launches.stream().filter(Launch -> Launch.getLaunchDate().getYear() == year).collect(Collectors.toList());
        Map<LaunchServiceProvider, BigDecimal> mapByLsp = new HashMap<>();
        for (Launch l : filteredLaunchList) {
            if (mapByLsp.containsKey(l.getLaunchServiceProvider())){
                BigDecimal bd = mapByLsp.get(l.getLaunchServiceProvider()).add(l.getPrice());
                mapByLsp.put(l.getLaunchServiceProvider(),bd);
            } else {
                BigDecimal tmp = l.getPrice();
                mapByLsp.put(l.getLaunchServiceProvider(),tmp);
            }
        }
        return mapByLsp;
    }


    // Get List of LSP Sorted (descending) by the Revenue Amount
    public static List<LaunchServiceProvider> getSortedLspByRevenue(Map<LaunchServiceProvider,BigDecimal> mapUnsorted){
        Map<LaunchServiceProvider, BigDecimal> sortedLSP = mapUnsorted
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));
        return new ArrayList<>(sortedLSP.keySet());
    }

    public static List<Rocket> getSortedRocketsByLaunches (List<Launch> listLaunches){
        Map<Rocket,Integer> mapRockets = new HashMap<>();
        for (Launch launch: listLaunches){
            Rocket rocket = launch.getLaunchVehicle();
            if (mapRockets.containsKey(rocket)){
                int number_launches = mapRockets.get(rocket);
                mapRockets.put(rocket,++number_launches);
            } else {
                mapRockets.put(rocket,1);
            }
        }
        Map<Rocket, Integer> sortedRocket = mapRockets
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));
        List<Rocket> topRockets = new ArrayList<>();
        for (Rocket rocket : sortedRocket.keySet()){
            topRockets.add(rocket);
        }
        return topRockets;
    }
}
