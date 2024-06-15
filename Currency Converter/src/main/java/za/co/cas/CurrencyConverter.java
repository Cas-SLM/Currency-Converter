package za.co.cas;

import com.google.gson.internal.LinkedTreeMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CurrencyConverter extends JFrame {

    private JTextField inputField;
    private JTextField outputField;
    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JButton convertButton;

    public CurrencyConverter() {
        FileHandler file = new FileHandler();
        LinkedTreeMap<String, String> supported = file.getSupported();
        ArrayList<String> curries = new ArrayList<>(){{
            for (String key : supported.keySet()) {
                add(key + ": " + supported.get(key));
            }
        }};
        Thread updater = new Thread(file::update, "Symbols File Updater");
        updater.setDaemon(true);
        boolean connected = false;
        try {
            URL url = new URL("api.frankfurter.app");
            URLConnection connection = url.openConnection();
            connection.connect();
            connected = true;
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
        } catch (MalformedURLException e) {
            connected = false;
        } catch (IOException e) {
            connected = false;
            System.out.println("Can't connect to: api.frankfurter.app");
        }

        setTitle("Currency Converter");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputField = new JTextField(10);
        outputField = new JTextField(10);
        outputField.setEditable(false);
        String[] currencies = curries.toArray(new String[0]);//{"USD", "EUR", "GBP", "JPY", "INR"}; //TODO: ADD SUPPORTED CURRENCIES
        fromCurrency = new JComboBox<>(currencies);
        toCurrency = new JComboBox<>(currencies);
        convertButton = new JButton("Convert");

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Conversion logic would go here
                // For now, just copy the input to output as a placeholder
                String input = inputField.getText();
                outputField.setText(input);
            }
        });

        // Create panels for layout
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

        // Add components to the frame
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);
        add(convertButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CurrencyConverter().setVisible(true);
            }
        });
    }
}