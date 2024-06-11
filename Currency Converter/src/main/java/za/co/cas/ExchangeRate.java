package za.co.cas;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<?, ?> getRates(String symbol) {
        Gson gson = new Gson();
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
        return gson.fromJson(exRatesResponse.body(), HashMap.class);
    }

    public static String map(Map<?, ?> map, String space, String delimiter) {
        StringBuilder output = new StringBuilder();
        output.append("{").append(delimiter);
        int i = 0;
        int length = map.keySet().size();
        for (var key : map.keySet()) {
            if (map.get(key) instanceof Map<?, ?>) {
                if ((((Map<?, ?>) map.get(key))).size() == 1)
                    output.append(space).append(key).append(": ").append(map((Map<?, ?>) map.get(key), "", ""));
                else
                    output.append(space).append(key).append(": ").append(map((Map<?, ?>) map.get(key), space + "  ", delimiter));
            } else output.append(space).append(key).append(": ").append(map.get(key).toString());
            if (i < length - 1) output.append(",").append(delimiter);
            i++;
        }
        output.append(delimiter).append(space).append("}");
        return output.toString();
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        String allSymbols = getSymbols();
        System.out.println("Supported Symbols: " + allSymbols);
        HashMap<String, String> symbols = gson.fromJson(allSymbols, HashMap.class);
        System.out.println("Symbols with names: " + symbols);
//        HashMap<String, Map<?, ?>> ZARRates = new HashMap<>();
//        for (String symbol : symbols.keySet()) {
//            ZARRates.put(symbol, getRates(symbol));
//        }
//        System.out.println("Exchange Rates Per Symbol : \n  " + map(ZARRates, "  ", "\n"));

        Symbol ZAR = new Symbol("ZAR");
        Map<?, ?> LocalZAR = getRates("ZAR");
        System.out.println("RateRequest request: " + ZAR);
        System.out.println("Infile request: " + LocalZAR);
        System.out.println("RateRequest map: " + map(ZAR.map(), "  ", "\n"));
        System.out.println("Infile map: " + map(LocalZAR, "  ", "\n"));
        System.out.println("ZAR to AUD: " + ZAR.exchangeTo("AUD", 123));
    }

}