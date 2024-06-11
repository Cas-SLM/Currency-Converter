package za.co.cas;

import com.google.gson.Gson;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class Symbol {
    private Double amount;
    private String base;
    private String symbol;
    private String name;
    private String date;
    private HashMap<String, Double> rates;
    private Gson gson;


    public Double exchangeTo(String symbol, Integer value) {
        return exchangeTo(symbol, (double) value);
    }
    public Double exchangeTo(String symbol, Double value) {
        double exchanged = 0;
        if (rates.containsKey(symbol)) {
            exchanged = rates.get(symbol) * value;
        }
        return exchanged;//.valueOf(exchanged);
    }

    public Symbol(String symbol) {
        gson = new Gson();
        Symbol newSymbol = gson.fromJson(RateRequest.getRates(symbol), Symbol.class);
        newSymbol.setSymbol();
        newSymbol.setName();
        this.amount = newSymbol.getAmount();
        this.base = newSymbol.getBase();
        this.date = newSymbol.getDate();
        this.name = newSymbol.getName();
        this.symbol = newSymbol.getSymbol();
        this.rates = newSymbol.getRates();
    }

    public Map<?, ?> map() {
        return gson.fromJson(gson.toJson(this), HashMap.class);
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
        return rates;
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
        return "Symbol{" +
                "name='" + this.name + '\'' +
                ", symbol='" + this.symbol + '\'' +
                ", base='" + this.base + '\'' +
//                ", date='" + this.date + '\'' +
                ", amount=" + this.amount +
                ", rates=" + gson.fromJson(ExchangeRate.map(this.rates, "", ""), HashMap.class) +
                '}';
    }
}