package lt.vln.aj.airports.controller;

import lombok.extern.log4j.Log4j2;
import lt.vln.aj.airports.controller.helper.ImportCsvControllerHelper;
import lt.vln.aj.airports.controller.helper.ResponseMessage;
import lt.vln.aj.airports.entity.CountryEntity;
import lt.vln.aj.airports.repository.CountryRepository;
import lt.vln.aj.airports.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static lt.vln.aj.airports.controller.CountryController.API_COUNTRY;
import static lt.vln.aj.airports.controller.helper.ImportCsvControllerHelper.IMPORT_CSV;

@Log4j2
//@CrossOrigin({"http://localhost:8080", "http://localhost:3000/"})
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(API_COUNTRY)
public record CountryController(CountryService countryService,
                                CountryRepository countryRepository,
                                ImportCsvControllerHelper importCsvControllerHelper) {

    public static final String API_COUNTRY = "/api/country";
    public static final String GET_ALL = "/get-all";

    @PostMapping(IMPORT_CSV)
    public ResponseEntity<ResponseMessage> importCsv(@RequestParam("file") MultipartFile file) {
        log.info("Call to " + API_COUNTRY + IMPORT_CSV + ". Importing file: " + file.getOriginalFilename());
        return importCsvControllerHelper.importCsv(countryService, file);
    }

    @GetMapping(GET_ALL)
    public ResponseEntity<List<CountryEntity>> getAllCountries() {
        try {
            log.info("Call to " + API_COUNTRY + GET_ALL + ".");
            List<CountryEntity> allCountries = countryRepository.findAll();
            log.info("All countries retrieved. Size: " + allCountries.size());
            return new ResponseEntity<>(allCountries, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while retrieving all countries.");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
