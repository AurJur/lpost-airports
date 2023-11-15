package lt.vln.aj.airports.util;

import lombok.experimental.UtilityClass;
import lt.vln.aj.airports.exception.CustomParseException;
import org.apache.commons.csv.CSVFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@UtilityClass
public class CsvUtil {

    private final String type = "text/csv";
    public final CSVFormat csvFormat = CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).setTrim(true).build();

    public boolean hasCsvFormat(MultipartFile file) {
        return type.equals(file.getContentType()) || Objects.equals(file.getContentType(), "application/vnd.ms-excel");
    }

    public <T> T parseAllowNull(Class<T> type, String value) throws CustomParseException {
        if (value == null || value.isEmpty()) {
            return null;
        } else {
            return switch (type.getSimpleName()) {
                case "Float" -> type.cast(Float.valueOf(value));
                case "Integer" -> type.cast(Integer.valueOf(value));
                default ->
                        throw new CustomParseException("Invalid type: " + type.getSimpleName() + " or value: " + value);
            };
        }
    }

}