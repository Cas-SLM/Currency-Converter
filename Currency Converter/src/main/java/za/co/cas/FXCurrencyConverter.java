/*
package za.co.cas;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FXCurrencyConverter extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Currency Converter");

        // Create a grid pane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Create labels
        Label fromCurrencyLabel = new Label("From Currency:");
        grid.add(fromCurrencyLabel, 0, 0);

        Label toCurrencyLabel = new Label("To Currency:");
        grid.add(toCurrencyLabel, 0, 1);

        Label amountLabel = new Label("Amount:");
        grid.add(amountLabel, 0, 2);

        Label resultLabel = new Label("Result:");
        grid.add(resultLabel, 0, 3);

        // Create combo boxes for currency selection
        ComboBox<String> fromCurrencyComboBox = new ComboBox<>();
        fromCurrencyComboBox.getItems().addAll("USD", "EUR", "GBP", "JPY", "AUD");
        grid.add(fromCurrencyComboBox, 1, 0);

        ComboBox<String> toCurrencyComboBox = new ComboBox<>();
        toCurrencyComboBox.getItems().addAll("USD", "EUR", "GBP", "JPY", "AUD");
        grid.add(toCurrencyComboBox, 1, 1);

        // Create text field for amount input
        TextField amountTextField = new TextField();
        grid.add(amountTextField, 1, 2);

        // Create a text field to display the result
        TextField resultTextField = new TextField();
        resultTextField.setEditable(false);
        grid.add(resultTextField, 1, 3);

        // Create convert button
        Button convertButton = new Button("Convert");
        grid.add(convertButton, 1, 4);

        // Add action event to convert button
        convertButton.setOnAction(e -> {
            // Logic for currency conversion will be added here
            resultTextField.setText("Conversion logic not implemented");
        });

        // Create a scene and set it on the stage
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}*/