package lt.vln.aj.airports.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportCsvService {

    void importAll(MultipartFile file) throws IOException;

}
