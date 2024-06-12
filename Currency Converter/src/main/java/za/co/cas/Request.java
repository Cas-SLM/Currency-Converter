package za.co.cas;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Request {
    public static String getRates(String symbol) {
        HttpRequest getExRates;
        HttpResponse<String> exRatesResponse;
        try {
            getExRates = HttpRequest.newBuilder()
                    .uri(new URI("https://api.frankfurter.app/latest?from=" + symbol.toUpperCase()))
                    .GET().build();
            try (HttpClient getResponse = HttpClient.newHttpClient()) {
                exRatesResponse = getResponse.send(getExRates, HttpResponse.BodyHandlers.ofString());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (exRatesResponse.statusCode() == 200)
            return exRatesResponse.body();
        else
            return "";
    }

    public static String getSymbols() {
        HttpRequest getCurrencies;
        HttpResponse<String> currenciesResponse;
        try {
            getCurrencies = HttpRequest.newBuilder()
                    .uri(new URI("https://api.frankfurter.app/currencies"))
                    .GET().build();

            HttpClient getRequest = HttpClient.newHttpClient();
            currenciesResponse = getRequest.send(getCurrencies, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return currenciesResponse.body();
    }

    public static LinkedTreeMap<?, ?> getSupported() {
        Gson GSON = new Gson();
        LinkedTreeMap<?, ?> supported = GSON.fromJson(Request.getSymbols(), LinkedTreeMap.class);
        return supported;
    }
}