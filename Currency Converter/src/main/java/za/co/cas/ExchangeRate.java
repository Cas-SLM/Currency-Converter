package za.co.cas;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;

import com.google.gson.Gson;
//import com.fasterxml.jackson.databind.

public class ExchangeRate {
    public void updateSymbols() {

    }

    public static String getSymbols() {
        HttpRequest getCurrencies;
        HttpResponse<String> currenciesResponse;
        try {
             getCurrencies = HttpRequest.newBuilder()
                    .uri(new URI("https://api.frankfurter.app/currencies"))
                    .GET().build();

            HttpClient getRequest = HttpClient.newHttpClient();
            currenciesResponse = getRequest.send(getCurrencies, BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return currenciesResponse.body();
    }

    public static String getRates(String symbol) {
        HttpRequest getExRates;
        HttpResponse<String> exRatesResponse;
        try {
            getExRates = HttpRequest.newBuilder()
                    .uri(new URI("https://api.frankfurter.app/latest?from=" + symbol.toUpperCase()))
                    .GET().build();
            HttpClient getResponse = HttpClient.newHttpClient();
            exRatesResponse = getResponse.send(getExRates, BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return exRatesResponse.body();

    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        String allSymbols = getSymbols();
        System.out.println(allSymbols);
        HashMap<String, String> symbols = gson.fromJson(allSymbols, HashMap.class);
        System.out.println(symbols);
        HashMap<String, Double> ZARRates = new HashMap<>();
        for (String symbol : symbols.keySet()) {
            ZARRates.put(symbol, null);
        }
        System.out.println(ZARRates);
        String RandRates = getRates("ZAR");
        System.out.println(RandRates);
        RateRequest ZAR = gson.fromJson(RandRates, RateRequest.class);
        ZAR.setSymbol();
        System.out.println(ZAR);
        System.out.println(ZAR.exchangeTo("AUD", 123));
    }

}
