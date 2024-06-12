package za.co.cas;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.print.attribute.HashDocAttributeSet;
import java.time.LocalDate;
import java.io.*;
import java.nio.file.Files;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FileHandler {
//    private static final Map<String, String> env = System.getenv();
    private static final String SP = File.separator;
    private static final String PATH = System.getProperty("user.dir") + SP + "Currency Converter" + SP + "src" + SP +
            "main" + SP + "java" + SP + "za" + SP + "co" + SP + "cas" + SP + "Symbols.json";
    private static final File SYMBOLS_FILE = new File(PATH);
    private static final Gson GSON = new Gson();

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

    private static boolean findSimilar(String currency) {
        LinkedTreeMap<?, ?> supported = (LinkedTreeMap) contents().get("supported");
        for (var value : supported.keySet()) {
            assert value instanceof String;
            if (((String) value).endsWith(currency))
                return true;
        }
        return false;
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

    private static void update(Symbol symbol) {
        String date = symbol.getDate();
        HashMap contents = getMap();
        LinkedTreeMap all = (LinkedTreeMap) contents.get("rates");
        all.put(symbol.getBase(), symbol.getRates());
        contents.put("rates", all);
        contents.put("date", date);
        writeString(Symbol.toJSON(contents, "  ", "\n",true));
    }

    public static boolean supportedCurrency(String currency) {
        LinkedTreeMap<?, ?> supported = (LinkedTreeMap) contents().get("supported");
        if (((Map)supported).containsKey(currency) || (supported).containsValue(currency) || findSimilar(currency))
            return true;
        else return false;
    }

    public static String symbol(String currency) {
        if (supportedCurrency(currency)) {
            HashMap<?, ?> supported = (HashMap) contents().get("supported");
            for (var key : supported.keySet()) {
                assert key instanceof String;
                var value = supported.get(key);
                assert value instanceof String;
                LinkedList<String> currencies = new LinkedList<>();
                LinkedList<String> symbols = new LinkedList<>();
                if (((String) value).endsWith(currency)) {
                    currencies.add((String) value);
                    symbols.add((String) key);
                }
                if (currencies.size() == 1) {
                    LinkedTreeMap<?, ?> rates = (LinkedTreeMap<?, ?>) contents().get("rates");
                    return symbols.getFirst();
                } else {
                    //TODO: ask for which symbol
                    System.out.println("Chose symbol:");
                    return symbols.getFirst();
                }
            }
        }
        return "";
    }

    public static HashMap<?, ?> getMap() {
        return contents();
    }

    @Nullable
    public static Symbol getSymbol(String currency) {
        if (supportedCurrency(currency)) {
            LinkedTreeMap<?, ?> supported = (LinkedTreeMap) contents().get("supported");
            for (var key : supported.keySet()) {
                assert key instanceof String;
                var value = supported.get(key);
                assert value instanceof String;
                LinkedList<String> currencies = new LinkedList<>();
                LinkedList<String> symbols = new LinkedList<>();
                if (((String) value).endsWith(currency)) {
                    currencies.add((String) value);
                    symbols.add((String) key);
                }
                if (currencies.size() == 1) {
                    LinkedTreeMap<?, ?> rates = (LinkedTreeMap<?, ?>) contents().get("rates");
                    return new Symbol(symbols.getFirst(),(LinkedTreeMap<?, ?>) rates.get(symbols.getFirst()));
                } else {
                    //TODO: ask for which symbol
                    System.out.println("Chose symbol:");
                    return new Symbol("ZAR");
                }
            }
        }
        return null;
    }

    public static void update() {
        create();
        HashMap<?, ?> contents = contents();
        if (contents.containsKey("supported")) {
            var supported = contents.get("supported");
            //NOTE: supported might be a LinkedTreeMap
            assert supported instanceof Map<?,?>;
            for (var key : ((Map<?, ?>) supported).keySet()) {
                assert key instanceof String;
                Symbol newSymbol = Symbol.create((String) key);
                update(newSymbol);
            }
        }
    }
}