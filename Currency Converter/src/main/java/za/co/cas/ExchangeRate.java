package za.co.cas;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
//import com.fasterxml.jackson.databind.

public class ExchangeRate {
    public void updateSymbols() {

    }

    @NotNull
    public static String map(@NotNull Map<?, ?> map, String space, String delimiter) {
        StringBuilder output = new StringBuilder();
        output.append("{").append(delimiter);
        int i = 0;
        int length = map.keySet().size();
        for (var key : map.keySet()) {
            var value = map.get(key);
            if (value instanceof Map<?, ?>) {
                if ((((Map<?, ?>) value)).size() == 1)
                    output.append(space).append(key).append(": ").append(map((Map<?, ?>) value, "", ""));
                else
                    output.append(space).append(key).append(": ").append(map((Map<?, ?>) value, space + "  ", delimiter));
            } else output.append(space).append(key).append(": ").append(value.toString());
            if (i < length - 1) output.append(",").append(delimiter);
            i++;
        }
        output.append(delimiter).append(space).append("}");
        return output.toString();
    }

    @NotNull
    public static String toJSON(@NotNull Map<?, ?> map, String space, String delimiter) {
        StringBuilder output = new StringBuilder();
        output.append("{").append(delimiter);
        int i = 0;
        int length = map.keySet().size();
        for (var key : map.keySet()) {
            var value = map.get(key);
            if (value instanceof Map<?, ?>) {
                if ((((Map<?, ?>) value)).size() == 1) {
                    output.append(space);
                    if (key instanceof String) {
                        output.append('\"').append(key).append('\"');
                    } else {
                        output.append(key);
                    }
                    output.append(": ").append(toJSON((Map<?, ?>) value, "", ""));
                }
                else {
                    output.append(space);
                    if (key instanceof String) {
                        output.append('\"').append(key).append('\"');
                    } else {
                        output.append(key);
                    }
                    output.append(": ").append(toJSON((Map<?, ?>) value, space + "  ", delimiter));
                }
            } else {
                output.append(space);
                if (key instanceof String) {
                    output.append('\"').append(key).append('\"');
                } else {
                    output.append(key);
                }
                output.append(": ");
                if (value instanceof String) {
                    output.append('\"').append(value.toString()).append('\"');
                } else {
                    output.append(value.toString());
                }
            }
            if (i < length - 1) output.append(",").append(delimiter);
            i++;
        }
        output.append(delimiter).append(space).append("}");
        return output.toString();
    }

    public static void main(String[] args) {
//        Gson gson = new Gson();
//        String allSymbols = Request.getSymbols();
//        HashMap<String, String> Symbols = gson.fromJson(allSymbols, HashMap.class);
//        for (String key : Symbols.keySet()) {
//            FileHandler.update(new Symbol(key));
//        }
//        System.out.println("Supported Symbols: " + allSymbols);
//        HashMap<String, String> symbols = gson.fromJson(allSymbols, HashMap.class);
//        System.out.println("Symbols with names: " + symbols);

        Symbol ZAR = new Symbol("ZAR");
        System.out.println("Symbol: " + ZAR);
        System.out.println("Symbol map: " + map(ZAR.toMap(), "  ", "\n"));
        System.out.println("Symbol json: " + Symbol.toJSON(ZAR.toMap(), "  ", "\n", true));
        System.out.println("ZAR to AUD: " + ZAR.exchangeTo("AUD", 123));
    }

}