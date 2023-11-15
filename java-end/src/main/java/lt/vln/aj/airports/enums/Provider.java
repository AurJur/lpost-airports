package lt.vln.aj.airports.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lt.vln.aj.airports.entity.AirportEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Provider {

    TURKISH_AIRLINES("turkishairlines", "Turkish Airlines"),
    RYANAIR("ryanair", "Ryanair"),
    LUFTHANSA("lufthansa", "Lufthansa"),
    AIRBALTIC("airbaltic", "airBaltic"),
    WIZZAIR("wizzair", "Wizz Air");

    private final String text;
    private final String brandName;

    public static Provider ofText(String text) {
        return Arrays.stream(values()).filter(p -> p.getText().equals(text)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No Provider defined for text: " + text));
    }

    public static List<Provider> resolveActiveProviders(AirportEntity ae) {
        List<Provider> activeProviders = new ArrayList<>();
        if (ae.getWizzair()) activeProviders.add(WIZZAIR);
        if (ae.getRyanair()) activeProviders.add(RYANAIR);
        if (ae.getAirbaltic()) activeProviders.add(AIRBALTIC);
        if (ae.getLufthansa()) activeProviders.add(LUFTHANSA);
        if (ae.getTurkishAirlines()) activeProviders.add(TURKISH_AIRLINES);
        return activeProviders;
    }

}
