package za.co.cas.API;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static za.co.cas.API.Request.*;

public class RequestTest {
    private static String symbol;
    private static String response;
    private static LocalDate today = LocalDate.now(ZoneId.systemDefault());

    void setupRates(String Symbol) {
        response = getRates(Symbol);
    }

    void setupSymbols() {
        response = getSymbols();
    }

    @Test
    void invalidSymbol() {
        setupRates("INVALID");
        assertNotNull(response);
        assertEquals("{\"message\":\"not found\"}", response);
    }

    @Test
    void validSymbol() {
        setupRates("ZAR");
        assertNotNull(response);
        assertNotEquals("{\"message\":\"not found\"}", response);
    }

    @Test
    void requestDate() {
        setupRates("USD");
        assertNotNull(response);
        assertNotEquals("{\"message\":\"not found\"}", response);
        assertTrue(response.contains("\"date\""));

        String flag = "(\\d+-\\d+-\\d+)";
        Matcher matcher = Pattern.compile(flag).matcher(response);
        assertTrue(matcher.find());

        String date = matcher.group(1);
        LocalDate expectedDate = today.minusDays(4);
        LocalDate actualDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assertFalse(expectedDate.isAfter(actualDate));
    }

    @Test
    void baseTest() {
        setupRates("AUD");
        assertNotNull(response);
        assertNotEquals("{\"message\":\"not found\"}", response);
        assertTrue(response.contains("\"base\""));

        String flag = "(\"base\":\"\\w+\")?([A-Z]{3})";
        Matcher matcher = Pattern.compile(flag).matcher(response);
        assertTrue(matcher.find());

        String base = matcher.group(0);
        assertEquals("AUD", base);
    }

    @Test
    void exchangeRatesTest() {
        setupRates("JPY");
        assertNotNull(response);
        assertNotEquals("{\"message\":\"not found\"}", response);

        String flag = "\"[A-Z]+\":\\d+.\\d+";
        Matcher matcher = Pattern.compile(flag).matcher(response);
        assertTrue(matcher.find());
    }

    @Test
    void symbolsTest() {
        setupSymbols();
        assertNotNull(response);
        assertNotEquals("{\"message\":\"not found\"}", response);

        String flag = "\"[A-Z]+\":\"([\\w\\s]+)\"";
        Matcher matcher = Pattern.compile(flag).matcher(response);
        assertTrue(matcher.find());

    }
}

