package lt.vln.aj.airports.controller;

import lombok.extern.log4j.Log4j2;
import lt.vln.aj.airports.controller.helper.ImportCsvControllerHelper;
import lt.vln.aj.airports.controller.helper.ResponseMessage;
import lt.vln.aj.airports.entity.RegionEntity;
import lt.vln.aj.airports.repository.RegionRepository;
import lt.vln.aj.airports.service.RegionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static lt.vln.aj.airports.controller.RegionController.API_REGION;
import static lt.vln.aj.airports.controller.helper.ImportCsvControllerHelper.IMPORT_CSV;

@Log4j2
//@CrossOrigin({"http://localhost:8080", "http://localhost:3000/"})
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(API_REGION)
public record RegionController(RegionService regionService,
                               RegionRepository regionRepository,
                               ImportCsvControllerHelper importCsvControllerHelper) {

    public static final String API_REGION = "/api/region";
    public static final String GET_ALL_BY_COUNTRY = "/get-all-by-country";

    @PostMapping(IMPORT_CSV)
    public ResponseEntity<ResponseMessage> importCsv(@RequestParam("file") MultipartFile file) {
        log.info("Call to " + API_REGION + IMPORT_CSV + ". Importing file: " + file.getOriginalFilename());
        return importCsvControllerHelper.importCsv(regionService, file);
    }

    @GetMapping(GET_ALL_BY_COUNTRY)
    public ResponseEntity<List<RegionEntity>> getAllByCountry(@RequestParam String countryCode) {
        try {
            log.info("Call to " + API_REGION + GET_ALL_BY_COUNTRY + " with " + countryCode + ".");
            List<RegionEntity> regionsByCountry = regionRepository.findAllByIsoCountry(countryCode);
            log.info("Regions by country retrieved. Size: " + regionsByCountry.size());
            return new ResponseEntity<>(regionsByCountry, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while retrieving regions by country.");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
