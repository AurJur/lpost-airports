package lt.vln.aj.airports.dto;

public record AirportInfoLowestPriceDto(int id,
                                        String name,
                                        String municipality,
                                        Integer lowestPrice) {
}
