package za.co.cas.Files;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import za.co.cas.API.Request;
import za.co.cas.Symbols.Symbol;

import java.time.LocalDate;
import java.io.*;
import java.nio.file.Files;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FileHandler {
    private static final String SP = File.separator;
    private static final String PATH = System.getProperty("user.dir") + SP + "Currency Converter" + SP + "src" + SP +
            "main" + SP + "java" + SP + "za" + SP + "co" + SP + "cas" + SP + "Symbols" + SP + "Symbols.json";
    private static final File SYMBOLS_FILE = new File(PATH);
    private static final Gson GSON = new Gson();
    private Map<?, ?> contents; // = new LinkedTreeMap<>();
    private String date;
    private LinkedTreeMap<String, LinkedTreeMap> rates;

    public LinkedTreeMap<String, String> getSupported() {
        return supported;
    }

    private LinkedTreeMap<String, String> supported;

    public FileHandler() {
        contents = contents();
        date = (String) contents.get("date");
        rates = (LinkedTreeMap<String, LinkedTreeMap>) contents().get("rates");
        supported = (LinkedTreeMap<String, String>) contents().get("supported");
    }

    private static void create() {
        try {
            SYMBOLS_FILE.createNewFile();
            HashMap contents = contents();
            String date = LocalDate.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!contents.containsKey("rates")) {
                contents.put("rates", new LinkedTreeMap<>());
            }
            if (!contents.containsKey("date")) contents.put("date", date);
            else {
                String fileDate = (String) contents.get("date");
                LocalDate dt1 = null;
                try {
                    dt1 = LocalDate.parse(fileDate);
                } catch (DateTimeParseException e) {
                    dt1 = LocalDate.now(ZoneId.systemDefault()).minusDays(1);
                }
                LocalDate dt2 = LocalDate.parse(date);
                if ( dt2.isAfter(dt1)) {
                    contents.put("date", date);
                }
            }
            if (!contents.containsKey("supported")) {
                LinkedTreeMap<?, ?> supported = Request.getSupported();
                contents.put("supported", supported);
            }
            HashSet<?> keyset = new HashSet<>(contents.keySet());
            for (var key : keyset) {
                if (key instanceof String) {
                    if (!key.equals("rates") && !key.equals("date") && !key.equals("supported")) {
                        contents.remove(key);
                    }
                } else {
                    contents.remove(key);
                }
            }
        } catch (IOException e) {
            boolean done = true;
        }
    }

    private static HashMap<?, ?> contents() {
        return GSON.fromJson(read(), HashMap.class);
    }

    public Map<String, String> findSimilar(String currency) {
        HashMap<String, String> similar = new HashMap<>();
        for (var key : supported.keySet()) {
            String value = supported.get(key).toLowerCase();
            if (((String) key).toLowerCase().endsWith(currency.toLowerCase())) {
                similar.put((String) key, value);
            } else if (value.toLowerCase().endsWith(currency.toLowerCase())) {
                similar.put((String) key, value);
            }
        }
        return similar;
    }

    private static String read() {
        StringBuilder output = new StringBuilder();
        if (SYMBOLS_FILE.canRead()) {
            try {
                BufferedReader fileReader = new BufferedReader(new FileReader(SYMBOLS_FILE));
                String line;
                while ((line = fileReader.readLine()) != null) {
                    output.append(line.strip());
                }
            } catch (IOException e) {
                output.append("{}");
            };
        } else {
            output.append("{}");
        }
        return output.toString();
    }

    private static String readstring() {
        String output;
        try {
            output = Files.readString(SYMBOLS_FILE.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    private static void writeString(String text) {
        StringBuilder output = new StringBuilder();
        try {
            output.append(Files.writeString(SYMBOLS_FILE.toPath(), text));
        } catch (IOException e) {
            output.append("");
        }
    }

    private void update(Symbol symbol) {
        String date = symbol.getDate();
        HashMap contents = contents();
        LinkedTreeMap all = (LinkedTreeMap) contents.get("rates");
        all.put(symbol.getBase(), symbol.getRates());
        contents.put("rates", all);
        contents.put("date", date);
        writeString(Symbol.toJSON(contents, "  ", "\n",true));
    }

    public void update() {
        create();
        HashMap<?, ?> contents = contents();
        String today = LocalDate.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (contents.containsKey("supported")) {
            if (!contents.get("date").equals(today)) {
                var supported = contents.get("supported");
                assert supported instanceof Map<?, ?>;
                for (var key : ((Map<?, ?>) supported).keySet()) {
                    assert key instanceof String;
                    Symbol newSymbol = Symbol.create((String) key);
                    update(newSymbol);
                }
            }
        }
    }

    public Symbol getSymbol(String symbol) {
        Symbol newSymbol = null;
        if (supported.containsKey(symbol)) {
                newSymbol = new Symbol(symbol, rates.get(symbol) ,date);
        } else {
            try {
                Process process = Runtime.getRuntime().exec("ping api.frankfurter.app");
                int result = process.waitFor();
                if (result == 0) {
                    newSymbol = new Symbol(symbol);
                } else {
                    int x = 0;
                }
            } catch (IOException | InterruptedException e) {
                int x = 0;
            }
        }
        return newSymbol;

    }
}