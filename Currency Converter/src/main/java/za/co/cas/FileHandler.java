package za.co.cas;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.time.LocalDate;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class FileHandler {
    private static final Map<String, String> env = System.getenv();
//    private static final String CURRENT_DIR = System.getProperty("user.dir");
    private static String sp = File.separator;
    private static final String PATH = System.getProperty("user.dir") + sp + "Currency Converter" + sp + "src" + sp +
            "main" + sp + "java" + sp + "za" + sp + "co" + sp + "cas" + sp + "Symbols.json";
    private static File SymbolsFile = new File(PATH);

    public static String read() {
        StringBuilder output = new StringBuilder();
        if (SymbolsFile.canRead()) {
            try {
                BufferedReader fileReader = new BufferedReader(new FileReader(SymbolsFile));
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

    public static String readstring() {
        String output;
        try {
            output = Files.readString(SymbolsFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    public static String writeString(String text) {
        StringBuilder output = new StringBuilder();
        try {
            output.append(Files.writeString(SymbolsFile.toPath(), text));
        } catch (IOException e) {
            output.append("");
        }
        return output.toString();
    }

    public static void write() {

    }

    public static void update(Symbol symbol) {
        Gson gson = new Gson();
        String date = symbol.getDate();
        HashMap contents = gson.fromJson(read(), HashMap.class);
        if (!contents.containsKey("rates")) {
            contents.put("rates", new LinkedTreeMap<>());
        }
        if (!contents.containsKey("date")) contents.put("date", date);
        else {
            String fileDate = (String) contents.get("date");
            LocalDate dt1 = LocalDate.parse(fileDate);
            LocalDate dt2 = LocalDate.parse(date);
            if ( dt2.isBefore(dt1)) {
                contents.put("date", date);
            }
        }
        if (!contents.containsKey("supported")) {
            LinkedTreeMap<?, ?> supported = gson.fromJson(Request.getSymbols(), LinkedTreeMap.class);
            contents.put("supported", supported);
        }
        HashSet<?> keyset = new HashSet<>(contents.keySet());
        for (var key : keyset) {
            if (key instanceof String) {
                if (!((String) key).equals("rates") && !((String) key).equals("date") && !((String) key).equals("supported")) {
                    contents.remove(key);
                }
            } else {
                contents.remove(key);
                }
        }
        LinkedTreeMap all = (LinkedTreeMap) contents.get("rates");
        all.put(symbol.getBase(), symbol.getRates());
        contents.put("rates", all);
        contents.put("date", date);
        writeString(Symbol.toJSON(contents, "  ", "\n",true));
    }

    public static void main(String[] args) {
        /*File json = new File(PATH);
        try {
            if (json.createNewFile()) {
                System.out.println(json.getName() + " was created");
            } else {
                System.out.println(json.getName() + " already exists");
                System.out.println(json.getAbsolutePath());
                System.out.println(json.getAbsoluteFile());
            }
        } catch (IOException e) {
            System.out.println("Couldn't do the file thing");
        }
        System.out.println(CURRENT_DIR);*/
        System.out.println(read());
    }
}