package za.co.cas;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class Symbol {
    private Double amount;
    private String base;
    private String symbol;
    private String name;
    private String date;
    private Map<?, ?> rates;

    public Symbol(String symbol, Map<?, ?> rates, String date) {
        this.amount = 1.0;
        this.base = symbol;
        this.date = date;
        this.rates = rates;
        setSymbol();
        setName();
    }

    public Symbol(String symbol) {
        Gson gson = new Gson();
        String rates = Request.getRates(symbol);
        Symbol newSymbol = gson.fromJson(rates, Symbol.class);
        this.amount = newSymbol.getAmount();
        this.base = newSymbol.getBase();
        this.date = newSymbol.getDate();
        this.rates = newSymbol.getRates();
        setSymbol();
        setName();
    }

    public Symbol(String symbol, Map<?, ?> rates) {
        this.amount = 1.0;
        this.base = symbol;
        LocalDate date = LocalDate.now(ZoneId.systemDefault());
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.rates = rates;
        setSymbol();
        setName();
    }

    public static Symbol create(String symbol) {
        String symbolRates = Request.getRates(symbol);
        Gson gson = new Gson();
        return gson.fromJson(symbolRates, Symbol.class);
    }

    public static Symbol fromFile(String symbol) {
        HashMap<?, ?> contents = FileHandler.getMap();
        if (FileHandler.supportedCurrency(symbol)) {
            LinkedTreeMap<?, ?> rates = (LinkedTreeMap<?, ?>) contents.get("rates");
            LinkedTreeMap<?, ?> supported = (LinkedTreeMap<?, ?>) contents.get("supported");
            String date = (String) contents.get("date");
            return new Symbol(symbol, rates, date);
        } else throw new IllegalArgumentException(symbol + " is not supported");
    }

    public Map<?, ?> toMap() {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this), HashMap.class);
    }

    /*private static String toMap(Map<?, ?> map, String space, String delimiter) {
        StringBuilder output = new StringBuilder();
        output.append("{").append(delimiter);
        int i = 0;
        int length = map.keySet().size();
        for (var key : map.keySet()) {
            var value = map.get(key);
            if (value instanceof Map<?, ?>) {
                if ((((Map<?, ?>) value)).size() == 1)
                    output.append(space).append(key).append(": ").append(toMap((Map<?, ?>) value, "", ""));
                else
                    output.append(space).append(key).append(": ").append(toMap((Map<?, ?>) value, space + "  ", delimiter));
            } else output.append(space).append(key).append(": ").append(value.toString());
            if (i < length - 1) output.append(",").append(delimiter);
            i++;
        }
        output.append(delimiter).append(space).append("}");
        return output.toString();
    }*/



    public String toJSON(){
        return toJSON(toMap(), "  ", "\n", true);
    }

    public static String toJSON(Map<?, ?> map, String space, String delimiter, boolean surround) {
        StringBuilder output = new StringBuilder();
        output.append("{").append(delimiter);
        int i = 0;
        int length = map.keySet().size();
        for (var key : map.keySet()) {
            var value = map.get(key);
            if (value instanceof Map<?, ?>) {
                if ((((Map<?, ?>) value)).size() == 1) {
                    output.append(space);
                    if (key instanceof String && surround) {
                        output.append('\"').append(key).append('\"');
                    } else {
                        output.append(key);
                    }
                    output.append(": ").append(toJSON((Map<?, ?>) value, "", "", surround));
                }
                else {
                    output.append(space);
                    if (key instanceof String && surround) {
                        output.append('\"').append(key).append('\"');
                    } else {
                        output.append(key);
                    }
                    output.append(": ").append(toJSON((Map<?, ?>) value, space + "  ", delimiter, surround));
                }
            } else {
                output.append(space);
                if (key instanceof String && surround) {
                    output.append('\"').append(key).append('\"');
                } else {
                    output.append(key);
                }
                output.append(": ");
                if (value instanceof String && surround) {
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

    public Double exchangeTo(String symbol, Integer value) {
        return exchangeTo(symbol, (double) value);
    }
    public Double exchangeTo(String symbol, Double value) {
        double exchanged = 0;
        if (rates.containsKey(symbol)) {
            var ratio = rates.get(symbol);
//            if (ratio instanceof Double) {
                exchanged = (double) ratio * value;
//            }
        }
        return exchanged;//.valueOf(exchanged);
    }

    public String getSymbol() {
        return symbol;
    }
    public void setSymbol() {
        this.symbol = Currency.getInstance(this.base).getSymbol();
    }

    public String getName() {
        return name;
    }
    public void setName() {
        this.name = Currency.getInstance(this.base).getDisplayName();
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, Double> getRates() {
        return (HashMap<String, Double>) rates;
    }
    public void setRates(HashMap<String, Double> rates) {
        this.rates = rates;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBase() {
        return base;
    }
    public void setBase(String base) {
        this.base = base;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return "Symbol{" +
                "name='" + this.name + '\'' +
//                "name='" + Currency.getInstance(this.base).getDisplayName() + '\'' +
                ", symbol='" + this.symbol + '\'' +
//                ", symbol='" + Currency.getInstance(this.base).getSymbol() + '\'' +
                ", base='" + this.base + '\'' +
                ", date='" + this.date + '\'' +
                ", amount=" + this.amount +
                ", rates=" + gson.fromJson(toJSON(this.rates, "", "", false), HashMap.class) +
                '}';
    }
}