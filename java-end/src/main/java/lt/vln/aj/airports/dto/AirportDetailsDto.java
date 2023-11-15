package lt.vln.aj.airports.dto;

import lt.vln.aj.airports.enums.AirportType;
import lt.vln.aj.airports.enums.Continent;
import lt.vln.aj.airports.enums.Provider;

import java.util.Map;

public record AirportDetailsDto(int id,
                                AirportType type,
                                String name,
                                Float latitudeDeg,
                                Float longitudeDeg,
                                Integer elevationFt,
                                Continent continent,
                                String isoCountry,
                                String isoRegion,
                                String municipality,
                                String scheduledService,
                                String gpsCode,
                                String iataCode,
                                String localCode,
                                Map<Provider, Integer> prices) {
}
