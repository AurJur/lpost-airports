package lt.vln.aj.airports.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lt.vln.aj.airports.enums.Continent;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static lt.vln.aj.airports.util.CsvUtil.*;

@Log4j2
@Entity
@Table(name = "country")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CountryEntity {

    @Id
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Continent continent;

    public static List<CountryEntity> csvToCountryEntities(InputStream is) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, csvFormat)) {
            return csvParser
                    .getRecords()
                    .stream()
                    .map(CountryEntity::parseFromCsvRecord)
                    .toList();
        }
    }

    public static CountryEntity parseFromCsvRecord(CSVRecord r) {
        try {
            return new CountryEntity(
                    r.get("code"),
                    r.get("name"),
                    Continent.valueOf(r.get("continent"))
            );
        } catch (IllegalArgumentException e) {
            log.error("An error occurred while parsing CSVRecord to CountryEntity (record code=" + r.get("code") + "):", e);
            throw new RuntimeException();
        }
    }
}
