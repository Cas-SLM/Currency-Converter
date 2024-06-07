package za.co.cas;

import java.util.Currency;
import java.util.HashMap;

public class RateRequest {
    private Double amount;
    private String base;
    private String symbol;
    private HashMap<String, Double> rates;


    public String exchangeTo(String symbol, Integer value) {
        return exchangeTo(symbol, (double) value);
    }
    public String exchangeTo(String symbol, Double value) {
        double exchanged = 0;
        if (rates.containsKey(symbol)) {
            exchanged = rates.get(symbol) * value;
        }
        return String.format("%.2f <- %f", exchanged, exchanged);//.valueOf(exchanged);
    }

    public String getSymbol() {
        return symbol;
    }
    public void setSymbol() {
        this.symbol = Currency.getInstance(this.base).getSymbol();
    }
    private String date;


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

    public HashMap<String, Double> getRates() {
        return rates;
    }

    public void setRates(HashMap<String, Double> rates) {
        this.rates = rates;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RateRequest{" +
                "amount=" + amount +
                ", symbol='" + symbol + '\'' +
//                ", base='" + base + '\'' +
//                ", date='" + date + '\'' +
                ", rates=" + rates +
                '}';
    }
}
