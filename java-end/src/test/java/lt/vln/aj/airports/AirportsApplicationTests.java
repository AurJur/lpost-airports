package lt.vln.aj.airports;

import lt.vln.aj.airports.dto.AirportInfoLowestPriceDto;
import lt.vln.aj.airports.service.AirportService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AirportsApplicationTests {

    @Autowired
    private AirportService airportService;
    private static final String usWashingtonRegion = "US-WA";
    private static final String expectedDataFile = "TestData_getInfoByRegion.csv";

    @Test
        //long test (10 threads ~5min)
    void testAirportServiceGetInfoByRegion() {
        List<AirportInfoLowestPriceDto> expected = readExpectedValues(expectedDataFile);
        List<AirportInfoLowestPriceDto> actual = airportService.getInfoByRegion(usWashingtonRegion);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    private List<AirportInfoLowestPriceDto> readExpectedValues(String filePath) {
        List<AirportInfoLowestPriceDto> resultList = new ArrayList<>();
        CSVFormat csvFormat = CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).setTrim(true).build();
        try (BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(filePath)), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, csvFormat)) {
            for (CSVRecord csvRecord : csvParser) {
                int id = Integer.parseInt(csvRecord.get("Id"));
                String name = csvRecord.get("Name");
                String municipality = csvRecord.get("Municipality");
                Integer lowestPrice = Integer.parseInt(csvRecord.get("LowestPrice"));
                AirportInfoLowestPriceDto airportInfoLowestPriceDto = new AirportInfoLowestPriceDto(id, name, municipality, lowestPrice);
                resultList.add(airportInfoLowestPriceDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

}
