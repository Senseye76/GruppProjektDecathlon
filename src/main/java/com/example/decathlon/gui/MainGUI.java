package com.example.decathlon.gui;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.awt.*;
import java.io.IOException;

import com.example.decathlon.common.Competitor;
import com.example.decathlon.deca.*;
import com.example.decathlon.excel.ExcelPrinter;

import static javax.swing.JOptionPane.showMessageDialog;


public class MainGUI {

    private JTextField nameField;
    private JTextField resultField;
    private JComboBox<String> disciplineBox;
    private JTextArea outputArea;

    private Competitor competitor;

    public static void main(String[] args) {
        new MainGUI().createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Track and Field Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        JPanel panel = new JPanel(new GridLayout(6, 1));

        // Input for competitor's name
        nameField = new JTextField(20);
        panel.add(new JLabel("Enter Competitor's Name:"));
        panel.add(nameField);

        // Dropdown for selecting discipline
        String[] disciplines = {
                "100m", "400m", "1500m", "110m Hurdles",
                "Long Jump", "High Jump", "Pole Vault",
                "Discus Throw", "Javelin Throw", "Shot Put"
        };

        disciplineBox = new JComboBox<>(disciplines);
        panel.add(new JLabel("Select Discipline:"));
        panel.add(disciplineBox);

        // Input for result
        resultField = new JTextField(10);
        panel.add(new JLabel("Enter Result:"));
        panel.add(resultField);

        // Button to calculate and display result
        JButton calculateButton = new JButton("Calculate Score");
        calculateButton.addActionListener(new CalculateButtonListener());
        panel.add(calculateButton);

        JButton exportButton = new JButton("Export to Excel");
        exportButton.addActionListener(new ExportButtonListener());  // New export button listener
        panel.add(exportButton);  // Add export button to the panel

        // Output area
        outputArea = new JTextArea(5, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane);

        frame.add(panel);
        frame.setVisible(true);
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String discipline = (String) disciplineBox.getSelectedItem();
            String resultText = resultField.getText();
            competitor = new Competitor(name);
            if (competitor == null) {
                competitor = new Competitor(name);  // Create a new competitor
            }

            try {
                double result = Double.parseDouble(resultText);

                int score = 0;
                switch (discipline) {
                    case "100m":
                        Deca100M deca100M = new Deca100M();
                        score = deca100M.calculateResult(result);
                        break;
                    case "400m":
                        Deca400M deca400M = new Deca400M();
                        score = deca400M.calculateResult(result);
                        break;
                    case "1500m":
                        Deca1500M deca1500M = new Deca1500M();
                        score = deca1500M.calculateResult(result);
                        break;
                    case "110m Hurdles":
                        Deca110MHurdles deca110MHurdles = new Deca110MHurdles();
                        score = deca110MHurdles.calculateResult(result);
                        break;
                    case "Long Jump":
                        DecaLongJump decaLongJump = new DecaLongJump();
                        score = decaLongJump.calculateResult(result);
                        break;
                    case "High Jump":
                        DecaHighJump decaHighJump = new DecaHighJump();
                        score = decaHighJump.calculateResult(result);
                        break;
                    case "Pole Vault":
                        DecaPoleVault decaPoleVault = new DecaPoleVault();
                        score = decaPoleVault.calculateResult(result);
                        break;
                    case "Discus Throw":
                        DecaDiscusThrow decaDiscusThrow = new DecaDiscusThrow();
                        score = decaDiscusThrow.calculateResult(result);
                        break;
                    case "Javelin Throw":
                        DecaJavelinThrow decaJavelinThrow = new DecaJavelinThrow();
                        score = decaJavelinThrow.calculateResult(result);
                        break;
                    case "Shot Put":
                        DecaShotPut decaShotPut = new DecaShotPut();
                        score = decaShotPut.calculateResult(result);
                        break;
                }

                // Update the competitor's score for the selected discipline
                competitor.setScore(discipline, score);

                if (score > 0) {
                    outputArea.append("Competitor: " + name + "\n");
                    outputArea.append("Discipline: " + discipline + "\n");
                    outputArea.append("Result: " + result + "\n");
                    outputArea.append("Score: " + score + "\n\n");
                    nameField.setText("");
                    resultField.setText("");
                    disciplineBox.setSelectedIndex(0);
                } else {
                    resultField.setText("");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for the result.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ExportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                exportToExcel();
                showMessageDialog(null, "Results exported successfully!", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                showMessageDialog(null, "Failed to export results to Excel.", "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportToExcel() throws IOException {
        String[][] data = new String[1][];
        int i = 0;

        Object[] rowData = competitor.getRowData(); // Get the competitor's row data

        // Ensure the array size matches the number of columns in rowData
        data[i] = new String[rowData.length];

        // Safely copy rowData to data array
        for (int j = 0; j < rowData.length; j++) {
            data[i][j] = (rowData[j] != null) ? rowData[j].toString() : "";  // Handle null values
        }


        ExcelPrinter printer = new ExcelPrinter("TrackAndFieldResults");
        printer.add(data, "Results");
        printer.write();
    }

}
