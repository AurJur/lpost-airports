package lt.vln.aj.airports.service;

import lt.vln.aj.airports.entity.CountryEntity;
import lt.vln.aj.airports.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public record CountryService(CountryRepository countryRepository) implements ImportCsvService {

    @Override
    public void importAll(MultipartFile file) throws IOException {
        List<CountryEntity> countryEntities = CountryEntity.csvToCountryEntities(file.getInputStream());
        countryRepository.saveAll(countryEntities);
    }
}
