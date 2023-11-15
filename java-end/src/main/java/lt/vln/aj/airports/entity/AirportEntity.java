package lt.vln.aj.airports.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lt.vln.aj.airports.enums.AirportType;
import lt.vln.aj.airports.enums.Continent;
import lt.vln.aj.airports.exception.CustomParseException;
import lt.vln.aj.airports.util.CsvUtil;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static lt.vln.aj.airports.enums.Provider.*;
import static lt.vln.aj.airports.util.CsvUtil.*;

@Log4j2
@Entity
@Table(name = "airport")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AirportEntity {

    @Id
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private AirportType type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private Float latitudeDeg;

    @Column(nullable = true)
    private Float longitudeDeg;

    @Column(nullable = true)
    private Integer elevationFt;

    @Column(nullable = false)
    private Continent continent;

    @Column(nullable = false)
    private String isoCountry;

    @Column(nullable = false)
    private String isoRegion;

    @Column(nullable = false)
    private String municipality;

    @Column(nullable = false)
    private String scheduledService;

    @Column(nullable = false)
    private String gpsCode;

    @Column(nullable = false)
    private String iataCode;

    @Column(nullable = false)
    private String localCode;

    @Column(nullable = false)
    private Boolean wizzair;

    @Column(nullable = false)
    private Boolean ryanair;

    @Column(nullable = false)
    private Boolean airbaltic;

    @Column(nullable = false)
    private Boolean lufthansa;

    @Column(nullable = false)
    private Boolean turkishAirlines;

    public static List<AirportEntity> csvToAirportEntities(InputStream is) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, csvFormat)) {
            return csvParser
                    .getRecords()
                    .stream()
                    .map(AirportEntity::parseFromCsvRecord)
                    .toList();
        }
    }

    public static AirportEntity parseFromCsvRecord(CSVRecord r) {
        try {
            return new AirportEntity(
                    Integer.parseInt(r.get("id")),
                    AirportType.ofText(r.get("type")),
                    r.get("name"),
                    CsvUtil.parseAllowNull(Float.class, r.get("latitude_deg")),
                    CsvUtil.parseAllowNull(Float.class, r.get("longitude_deg")),
                    CsvUtil.parseAllowNull(Integer.class, r.get("elevation_ft")),
                    Continent.valueOf(r.get("continent")),
                    r.get("iso_country"),
                    r.get("iso_region"),
                    r.get("municipality"),
                    r.get("scheduled_service"),
                    r.get("gps_code"),
                    r.get("iata_code"),
                    r.get("local_code"),
                    r.get(WIZZAIR.getText()).equals("1") ? TRUE : FALSE,
                    r.get(RYANAIR.getText()).equals("1") ? TRUE : FALSE,
                    r.get(AIRBALTIC.getText()).equals("1") ? TRUE : FALSE,
                    r.get(LUFTHANSA.getText()).equals("1") ? TRUE : FALSE,
                    r.get(TURKISH_AIRLINES.getText()).equals("1") ? TRUE : FALSE
            );
        } catch (IllegalArgumentException | CustomParseException e) {
            log.error("An error occurred while parsing CSVRecord to AirportEntity (record id=" + r.get("id") + "):", e);
            throw new RuntimeException();
        }
    }
}