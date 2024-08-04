package za.co.cas.API;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utility class for making HTTP requests to the Frankfurter API.
 */
public class Request {

    /**
     * Retrieves exchange rates data from the Frankfurter API for a specified currency symbol.
     * @param symbol The currency symbol (e.g., USD, EUR).
     * @return JSON string containing exchange rates data.
     * @throws RuntimeException If there's an issue with URI syntax, IO operation, or HTTP request.
     */
    public static String getRates(String symbol) {
        HttpRequest getExRates;
        HttpResponse<String> exRatesResponse;
        try {
            // Build HTTP GET request to retrieve exchange rates
            getExRates = HttpRequest.newBuilder()
                    .uri(new URI("https://api.frankfurter.app/latest?from=" + symbol.toUpperCase()))
                    .GET().build();

            // Send HTTP request and retrieve response
            try (HttpClient getResponse = HttpClient.newHttpClient()) {
                exRatesResponse = getResponse.send(getExRates, HttpResponse.BodyHandlers.ofString());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e); // Wrap and throw exceptions as RuntimeException
        }

        // Check if the response is successful (status code 200) and return body
//        if (exRatesResponse.statusCode() == 200)
        return exRatesResponse.body();
//        else
//            return ""; // Return empty string if response status code is not 200
    }

    /**
     * Retrieves supported currency symbols from the Frankfurter API.
     * @return JSON string containing supported currency symbols.
     * @throws RuntimeException If there's an issue with URI syntax, IO operation, or HTTP request.
     */
    public static String getSymbols() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest getCurrencies = null;
        try {
            // Build HTTP GET request to retrieve supported currency symbols
            getCurrencies = HttpRequest.newBuilder()
                    .uri(new URI("https://api.frankfurter.app/currencies"))
                    .GET().build();
        } catch (URISyntaxException ignored) {
        }
        HttpResponse<String> currenciesResponse = null;
        try {
            // Send HTTP request and retrieve response
            currenciesResponse = httpClient.send(getCurrencies, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e); // Wrap and throw exceptions as RuntimeException
        } finally {
            if (httpClient != null) {
                httpClient.close();// = null; // Or httpClient.close(); if close() method is available
            }
        }

        // Return response body containing supported currency symbols
        return currenciesResponse.body();
    }

    /**
     * Retrieves supported currency symbols as a LinkedTreeMap from the Frankfurter API.
     * @return LinkedTreeMap representing supported currency symbols.
     */
    public static LinkedTreeMap<?, ?> getSupported() {
        Gson gson = new Gson(); // Gson instance for JSON deserialization
        String symbolsJson = Request.getSymbols(); // Retrieve JSON string of supported symbols
        LinkedTreeMap<?, ?> supported = gson.fromJson(symbolsJson, LinkedTreeMap.class); // Deserialize JSON to LinkedTreeMap
        return supported; // Return deserialized LinkedTreeMap
    }
}
