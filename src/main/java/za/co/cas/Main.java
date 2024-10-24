package za.co.cas;

import za.co.cas.Files.FileHandler;
import za.co.cas.Symbols.Symbol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static Thread updater;
    private static boolean updated;
    private static boolean connected = false;
    private static final BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) {
        String target = "https://api.frankfurter.app/";
        FileHandler file     = new FileHandler();
        updater = new Thread(file::update, "Symbols File Updater");
        updater.setDaemon(true);
        updated = false;
        try {
            URL url = new URL(target);
            URLConnection connection = url.openConnection();
            connection.connect();
            connected = true;
            updater.start();
            new Thread(() -> {
                {
                    while (updater.isAlive()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            continue;
                        }
                    }
                    updated = true;
                }
            }).start();
        } catch (MalformedURLException e) {
                connected = false;
            } catch (IOException e) {
            connected = false;
            System.out.println("Can't connect to: " + target);
        }
        boolean on = true;
            try {
                String input;
                System.out.println("Do want to run the GUI version, Yes or No?");
                input = inputReader.readLine();
                switch (input.toLowerCase()) {
                    case "yes", "y":
                        CurrencyConverter converter = new CurrencyConverter();
                        converter.setVisible(true);
                        break;
                    case "no", "n":
                        do {
                            convert(file);
                            System.out.println("Do you want to continue? (Y/no)");
                            input = inputReader.readLine();
                            switch (input.toLowerCase()) {
                                case "yes":
                                case "y":
                                    on = true;
                                    break;
                                case "no":
                                case "n":
                                    on = false;
                                    break;
                                default:
                                    continue;
                            }
                        } while (on);
                        break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    private static void convert(FileHandler file) {
        Symbol from = new Symbol("None", new HashMap<>(), "None");
        try {
            boolean done = false;
            do {
                System.out.println("Currency to convert from: ");
                String in = inputReader.readLine();
                if (in.equalsIgnoreCase("exit"))
                    break;
                HashMap<String, String> similar = (HashMap<String, String>) file.findSimilar(in);
                if (similar.size() == 1) {
                    ArrayList<String> keys = new ArrayList<>(similar.keySet());
                    from = file.getSymbol(keys.getFirst());
                    done = true;
                } else if (similar.isEmpty()) {
                    System.out.println("Currency is not supported: " + in);
                    continue;
                } else {
                    from = getSymbol(similar, inputReader, file);
                    done = true;
                }
            } while (!done);
        } catch (IOException e) {
            int i = 0;
        }

        Symbol to= new Symbol("None", new HashMap<>(), "None");
        try {
            boolean done = false;
            do {
                System.out.println("Currency to convert to: ");
                String in = inputReader.readLine();
                if (in.equalsIgnoreCase("exit"))
                    break;
                HashMap<String, String> similar = (HashMap<String, String>) file.findSimilar(in);
                if (similar.size() == 1) {
                    ArrayList<String> keys = new ArrayList<>(similar.keySet());
                    to = file.getSymbol(keys.getFirst());
                    done = true;
                } else if (similar.isEmpty()) {
                    System.out.println("Currency is not supported: " + in);
                    continue;
                } else {
                    to = getSymbol(similar, inputReader, file);
                    done = true;
                }
            } while (!done);
        } catch (IOException e) {
            int i = 0;
        }
        double amount = 0d;
        try {
            String temp;
            do {
                System.out.println("Enter amount to convert: ");
                temp = inputReader.readLine();
            } while (!temp.matches("\\d+"));
            amount = Double.parseDouble(temp);
        } catch (IOException e) {
            int i = 0;
        }

        System.out.println(from.getSymbol()+ String.format("%.2f", amount) + " -> " + to.getSymbol() + String.format("%.2f", from.exchangeTo(to, amount)));
    }

    private static Symbol getSymbol(HashMap<String, String> similar, BufferedReader inputReader, FileHandler file) throws IOException {
        String in;
        ArrayList<String> keys = new ArrayList<>(similar.keySet());
        StringBuilder output = new StringBuilder();
        for (int i = 0, j = 1; i < similar.size(); i++, j++) {
            String key = keys.get(i);
            String value = similar.get(key);
            output.append(j).append(". ").append(key).append(" -> ").append(value);
            if (j < similar.size()) {
                output.append("\n");
            }
        }
        System.out.println(output);
        System.out.println("Select Currency: Eg. USD");
        in = inputReader.readLine();
        return file.getSymbol(in);
    }

}