package lt.vln.aj.airports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AirportsApplication {

    //TODO:
    // why importing regions csv file to countries end point succeeds? Because regions file has code, name, continent (apart from id and iso_country). Add check?
    // status after successfull/failed upload should disappear after a certain time.

    public static void main(String[] args) {
        SpringApplication.run(AirportsApplication.class, args);
    }

}
