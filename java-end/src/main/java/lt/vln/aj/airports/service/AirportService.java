package lt.vln.aj.airports.service;

import lt.vln.aj.airports.dto.AirportDetailsDto;
import lt.vln.aj.airports.dto.AirportInfoLowestPriceDto;
import lt.vln.aj.airports.entity.AirportEntity;
import lt.vln.aj.airports.enums.Provider;
import lt.vln.aj.airports.repository.AirportRepository;
import lt.vln.aj.airports.service.helper.PriceProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public record AirportService(AirportRepository airportRepository,
                             PriceProvider priceProvider) implements ImportCsvService {

    @Override
    public void importAll(MultipartFile file) throws IOException {
        List<AirportEntity> airportEntities = AirportEntity.csvToAirportEntities(file.getInputStream());
        airportRepository.saveAll(airportEntities);
    }

    public List<AirportInfoLowestPriceDto> getInfoByRegion(String regionCode) {
        List<AirportEntity> aeList = airportRepository.findAllByIsoRegion(regionCode);
        return aeList
                .parallelStream()
                .map(ae -> Map.entry(ae, getLowestPriceForAirport(ae)))
                .filter(entry -> entry.getValue().isPresent())
                .map(entry -> new AirportInfoLowestPriceDto(
                        entry.getKey().getId(),
                        entry.getKey().getName(),
                        entry.getKey().getMunicipality(),
                        entry.getValue().get()))
                .collect(Collectors.toList());
    }

    public AirportDetailsDto getAirportDetails(Integer airportId) {
        AirportEntity ae = airportRepository.findById(airportId).orElseThrow();
        Map<Provider, Integer> prices = getActiveProvidersPrices(ae);
        return new AirportDetailsDto(
                airportId,
                ae.getType(),
                ae.getName(),
                ae.getLatitudeDeg(),
                ae.getLongitudeDeg(),
                ae.getElevationFt(),
                ae.getContinent(),
                ae.getIsoCountry(),
                ae.getIsoRegion(),
                ae.getMunicipality(),
                ae.getScheduledService(),
                ae.getGpsCode(),
                ae.getIataCode(),
                ae.getLocalCode(),
                prices
        );
    }

    private Optional<Integer> getLowestPriceForAirport(AirportEntity ae) {
        Map<Provider, Integer> prices = getActiveProvidersPrices(ae);
        return prices
                .values()
                .stream()
                .min(Integer::compare);
    }

    private Map<Provider, Integer> getActiveProvidersPrices(AirportEntity ae) {
        List<Provider> activeProviders = Provider.resolveActiveProviders(ae);
        int airportId = ae.getId();

        return activeProviders
                .parallelStream()
                .map(provider -> Map.entry(provider, priceProvider.getPrice(provider, airportId)))
                .filter(entry -> entry.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(), (a, b) -> a, ConcurrentHashMap::new));
    }

}