package za.co.cas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static Thread updater;
    private static boolean updated;

    public static void main(String[] args) {
        updater = new Thread(FileHandler::update);
        updater.setName("JSON updater");
        updater.start();
        boolean done = false;
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        while (!done) {
            try {
                System.out.println("Enter currency you want to convert from: ");
                String strFrom = inputReader.readLine();
                Symbol from = getSymbol(strFrom);
                System.out.println("Enter currency you want to convert to: ");
                String strTo = inputReader.readLine();
                System.out.println("Enter amount you want to convert: ");
                String strAmount = inputReader.readLine();
                System.out.println(strFrom + " : " + strAmount + " -> " + strTo + " : " + "x");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static Symbol getSymbol(String currency) {
        updated = !updater.isAlive();
        if (updated) {
            return FileHandler.getSymbol(currency);
        } else {
            if (FileHandler.supportedCurrency(currency))
                return Symbol.create(FileHandler.symbol(currency));
            else return new Symbol(FileHandler.symbol(currency));
        }
    }
}