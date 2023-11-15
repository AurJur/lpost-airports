package lt.vln.aj.airports.controller;

import lombok.extern.log4j.Log4j2;
import lt.vln.aj.airports.controller.helper.ImportCsvControllerHelper;
import lt.vln.aj.airports.controller.helper.ResponseMessage;
import lt.vln.aj.airports.dto.AirportDetailsDto;
import lt.vln.aj.airports.dto.AirportInfoLowestPriceDto;
import lt.vln.aj.airports.entity.AirportEntity;
import lt.vln.aj.airports.repository.AirportRepository;
import lt.vln.aj.airports.service.AirportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static lt.vln.aj.airports.controller.AirportController.API_AIRPORT;
import static lt.vln.aj.airports.controller.helper.ImportCsvControllerHelper.IMPORT_CSV;

@Log4j2
//@CrossOrigin({"http://localhost:8080", "http://localhost:3000/"})
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(API_AIRPORT)
public record AirportController(AirportService airportService,
                                AirportRepository airportRepository,
                                ImportCsvControllerHelper importCsvControllerHelper) {

    public static final String API_AIRPORT = "/api/airport";
    public static final String GET_ALL_BY_REGION = "/get-all-by-region";
    public static final String GET_INFO_BY_REGION = "/get-info-by-region";
    public static final String GET_DETAILS_ID = "/get-details/{id}";

    @PostMapping(IMPORT_CSV)
    public ResponseEntity<ResponseMessage> importCsv(@RequestParam("file") MultipartFile file) {
        log.info("Call to " + API_AIRPORT + IMPORT_CSV + ". Importing file: " + file.getOriginalFilename());
        return importCsvControllerHelper.importCsv(airportService, file);
    }

    @GetMapping(GET_ALL_BY_REGION)
    public ResponseEntity<List<AirportEntity>> getAllByRegion(@RequestParam String regionCode) {
        try {
            log.info("Call to " + API_AIRPORT + GET_ALL_BY_REGION + " with " + regionCode + ".");
            List<AirportEntity> airportsByRegion = airportRepository.findAllByIsoRegion(regionCode);
            log.info("Airports by region retrieved. Size: " + airportsByRegion.size());
            return new ResponseEntity<>(airportsByRegion, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while retrieving airports by region.");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(GET_INFO_BY_REGION)
    public ResponseEntity<List<AirportInfoLowestPriceDto>> getInfoByRegion(@RequestParam String regionCode) {
        long start = System.currentTimeMillis();
        try {
            log.info("Call to " + API_AIRPORT + GET_INFO_BY_REGION + " with " + regionCode + ".");
            List<AirportInfoLowestPriceDto> airportInfoLowestPriceDtoList = airportService.getInfoByRegion(regionCode);
            long end = System.currentTimeMillis();
            long executionTime = (end - start) / 1000;
            log.info("Airports info with lowest price by region retrieved. Size: " + airportInfoLowestPriceDtoList.size() + ". Execution time: " + executionTime);
            return new ResponseEntity<>(airportInfoLowestPriceDtoList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while retrieving airports info with lowest price by region.");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(GET_DETAILS_ID)
    public ResponseEntity<AirportDetailsDto> getAirportDetails(@PathVariable Integer id) {
        try {
            log.info("Call to " + API_AIRPORT + GET_DETAILS_ID.replace("{id}", id.toString()) + ".");
            AirportDetailsDto airportDetailsDto = airportService.getAirportDetails(id);
            log.info("Airport details retrieved for ID: " + id);
            return new ResponseEntity<>(airportDetailsDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while retrieving airport details. ID: " + id);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
