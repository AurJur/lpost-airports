package lt.vln.aj.airports.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Table(name = "region")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegionEntity {

    @Id
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String continent;

    @Column(nullable = true)
    private String isoCountry;

    public static List<RegionEntity> csvToRegionEntities(InputStream is) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, csvFormat)) {
            return csvParser
                    .getRecords()
                    .stream()
                    .map(RegionEntity::parseFromCsvRecord)
                    .toList();
        }
    }

    public static RegionEntity parseFromCsvRecord(CSVRecord r) {
        try {
            return new RegionEntity(
                    Integer.parseInt(r.get("id")),
                    r.get("code"),
                    r.get("name"),
                    r.get("continent"),
                    r.get("iso_country")
            );
        } catch (IllegalArgumentException e) {
            log.error("An error occurred while parsing CSVRecord to RegionEntity (record id=" + r.get("id") + "):", e);
            throw new RuntimeException();
        }
    }
}
