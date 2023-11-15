package lt.vln.aj.airports.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AirportType {

    HELIPORT("heliport"),
    SMALL_AIRPORT("small_airport"),
    SEAPLANE_BASE("seaplane_base"),
    BALLOONPORT("balloonport"),
    MEDIUM_AIRPORT("medium_airport"),
    LARGE_AIRPORT("large_airport"),
    CLOSED("closed"); //TODO how this should be treated? Ignored?

    private final String text;

    public static AirportType ofText(String text) {
        return Arrays.stream(values()).filter(t -> t.getText().equals(text)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No AirportType for text: " + text));
    }

}
