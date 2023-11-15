package lt.vln.aj.airports.service.helper;

import lt.vln.aj.airports.enums.Provider;

import java.util.Optional;

public interface PriceProvider {

    // Optional reflects that it may be no price for particular provider-airportId pair.
    Optional<Integer> getPrice(Provider provider, int airportId);

}
