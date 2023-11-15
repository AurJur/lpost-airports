package lt.vln.aj.airports.controller.helper;

import lombok.extern.log4j.Log4j2;
import lt.vln.aj.airports.service.ImportCsvService;
import lt.vln.aj.airports.util.CsvUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Component
public class ImportCsvControllerHelper {

    public static final String IMPORT_CSV = "/import-csv";

    public ResponseEntity<ResponseMessage> importCsv(ImportCsvService importCsvService, MultipartFile file) {
        String responseText = "";
        String originalFilename = file.getOriginalFilename();
        if (CsvUtil.hasCsvFormat(file)) {
            try {
                importCsvService.importAll(file);
                responseText = "File \"" + originalFilename + "\" uploaded successfully.";
                log.info(responseText);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(responseText));
            } catch (Exception e) {
                responseText = "Could not upload \"" + originalFilename + "\" file.";
                log.error(responseText);
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(responseText));
            }
        }
        responseText = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(responseText));
    }

}
