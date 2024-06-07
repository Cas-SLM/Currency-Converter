package za.co.cas;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Symbol {
//    private final String symbol;
//    private final String name;
//    private final Double rate;
//    private final String base;

    public static String getRates(String symbol) {
        HttpRequest getExRates;
        HttpResponse<String> exRatesResponse;
        try {
            getExRates = HttpRequest.newBuilder()
                    .uri(new URI("https://api.frankfurter.app/latest?from=" + symbol.toUpperCase()))
                    .GET().build();
            HttpClient getResponse = HttpClient.newHttpClient();
            exRatesResponse = getResponse.send(getExRates, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return exRatesResponse.body();

    }


}
