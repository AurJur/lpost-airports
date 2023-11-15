package lt.vln.aj.airports.service;

import lt.vln.aj.airports.entity.RegionEntity;
import lt.vln.aj.airports.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public record RegionService(RegionRepository regionRepository) implements ImportCsvService {

    @Override
    public void importAll(MultipartFile file) throws IOException {
        List<RegionEntity> regionEntities = RegionEntity.csvToRegionEntities(file.getInputStream());
        regionRepository.saveAll(regionEntities);
    }

}
