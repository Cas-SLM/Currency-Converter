package za.co.cas;

import com.google.gson.internal.LinkedTreeMap;
import za.co.cas.Files.FileHandler;
import za.co.cas.Symbols.Symbol;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CurrencyConverter extends JFrame {

    private final JTextField inputField;
    private final JTextField outputField;
    private final JComboBox<String> fromCurrency;
    private final JComboBox<String> toCurrency;
    private final JButton convertButton;
    Symbol toSymbol, fromSymbol;

    public CurrencyConverter() {
        FileHandler file = new FileHandler();
        LinkedTreeMap<String, String> supported = file.getSupported();
        ArrayList<Symbol> symbols = new ArrayList<>(){{
            for (String key : supported.keySet()) {
                add(file.getSymbol(key));
            }
        }};
        Thread updater = new Thread(file::update, "Symbols File Updater");
        updater.setDaemon(true);
        try {
            URL url = new URL("https://api.frankfurter.app/");
            URLConnection connection = url.openConnection();
            connection.connect();
            updater.start();
            new Thread(() -> {{
                    while (updater.isAlive()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            continue;
                        }
                    }
                }}).start();
        } catch (IOException e) {
            System.out.println("Can't connect to: api.frankfurter.app");
        }

        setTitle("Currency Converter");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputField = new JTextField("1",10);
        outputField = new JTextField(10);
        outputField.setEditable(false);
        outputField.setHorizontalAlignment(JTextField.CENTER);
        String[] currencies = new String[symbols.size()];
        int i = 0;
        for (Symbol symbol : symbols) {
            currencies[i] = symbol.getBase() + ": " + symbol.getName();
            i++;
        }

        fromCurrency = new JComboBox<>(currencies);
        fromSymbol = file.getSymbol(getBase(fromCurrency));
        toCurrency = new JComboBox<>(currencies);
        toCurrency.setSelectedIndex(1);
        toSymbol = file.getSymbol(getBase(toCurrency));
        convertButton = new JButton("Convert");

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Double amount = Double.parseDouble(inputField.getText());
                    outputField.setText(String.format("%s %.2f",toSymbol.getSymbol(), fromSymbol.exchangeTo(toSymbol, amount)));
                } catch (NullPointerException | NumberFormatException err) {
                    outputField.setText("");
                }
            }
        });

        inputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateButtonState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateButtonState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateButtonState();
            }

            private void updateButtonState() {
                if (inputField.getText().isBlank()) {
                    convertButton.setEnabled(false);
                } else if (inputField.getText().matches("(\\w+)?([^\\d])")) {
                    convertButton.setEnabled(false);
                } else {
                    convertButton.setEnabled(inputField.getText().strip().matches("(\\d+)?(\\d+.\\d+)|(\\d+)"));
                }
            }
        });

        toCurrency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                toSymbol = file.getSymbol(getBase(toCurrency));
            }
        });

        fromCurrency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                fromSymbol = file.getSymbol(getBase(fromCurrency));
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("From:"));
        panel.add(fromCurrency);
        panel.add(new JLabel("To:"));
        panel.add(toCurrency);
        panel.add(new JLabel("Amount:"));
        panel.add(inputField);

        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.add(new JLabel("Converted Amount:"), BorderLayout.WEST);
        outputPanel.add(outputField, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
        add(convertButton, BorderLayout.SOUTH);
    }

    private static String getBase(JComboBox<String> dropbox) {
        return dropbox.getSelectedItem().toString().substring(0, 3);
    }

    /*public static void main(String[] args) {
        new CurrencyConverter().setVisible(true);
    }*/
}