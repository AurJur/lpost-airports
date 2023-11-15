package lt.vln.aj.airports.service.helper;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.log4j.Log4j2;
import lt.vln.aj.airports.enums.Provider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
public class ExternalPriceProvider implements PriceProvider {

    @Value("${price.provider.host}")
    private String priceProviderHost;

    private static final String priceProviderEndpoint = "/item/price";

    // Empty Optional is when external service returned no price for particular provider-airportId pair.
    private static final Map<String, Optional<Integer>> priceCache = new ConcurrentHashMap<>();

    @Override
    public Optional<Integer> getPrice(Provider provider, int airportId) {
        String cacheKey = getCacheKey(provider, airportId);

        Optional<Integer> cachedPrice = priceCache.get(cacheKey);
        if (cachedPrice != null) {
            return cachedPrice;
        } else {
            Optional<Integer> fetchedPrice = fetchPriceFromExternalService(provider, airportId);
            priceCache.put(cacheKey, fetchedPrice);
            return fetchedPrice;
        }
    }

    private String getCacheKey(Provider provider, int airportId) {
        return provider.getText() + "_" + airportId;
    }

    private Optional<Integer> fetchPriceFromExternalService(Provider provider, int airportId) {
        String url = priceProviderHost + priceProviderEndpoint + "/" + provider.getText() + "/" + airportId;

        try {
            WebClient webClient = WebClient.create();
            log.info("Will try: " + url);
            Mono<Integer> responseMono = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(jsonNode -> jsonNode.get("value").asInt());

            // Block to get the result
            Integer price = responseMono.block();
            return Optional.ofNullable(price);
        } catch (Exception e) {//TODO: what if network issues? The result should not go into the cache map.
            log.info("GET " + url + " resulted in exception: ", e);
            return Optional.empty();
        }
    }

}