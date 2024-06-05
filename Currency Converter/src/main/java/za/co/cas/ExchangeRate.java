package za.co.cas;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Objects;

import com.google.gson.Gson;
//import com.fasterxml.jackson.databind.

public class ExchangeRate {
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
        System.out.println(currenciesResponse.body());

        return currenciesResponse.body();
    }

    public static void updateSymbols() {

    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        var symbols = gson.fromJson(getSymbols(), HashMap.class);
    }

}
