package com.example.decathlon.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.IOException;

import com.example.decathlon.common.Competitor;
import com.example.decathlon.deca.*;
import com.example.decathlon.excel.ExcelPrinter;
import com.example.decathlon.heptathlon.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class MainGUI {

    private JTextField nameField;
    private JTextField resultField;
    private JComboBox<String> disciplineBox;
    private JTextArea outputArea;

    private JRadioButton decathlonRadio;
    private JRadioButton heptathlonRadio;

    private JButton addCompetitorButton;
    private JButton calculateButton;
    private JButton exportButton;

    private JTable snapshotTable;
    private DefaultTableModel tableModel;

    private final Map<String, Competitor> competitors = new LinkedHashMap<>();

    private static final String[] DECA_DISCIPLINES = {
            "100m", "Long Jump", "Shot Put", "High Jump", "400m",
            "110m Hurdles", "Discus Throw", "Pole Vault", "Javelin Throw", "1500m"
    };

    private static final String[] HEPTA_DISCIPLINES = {
            // Classic women heptathlon order
            "100m Hurdles", "Hep High Jump", "Hep Shot Put", "200m",
            "Hep Long Jump", "Hep Javelin Throw", "800m"
    };

    // Keep currently active set
    private String[] activeDisciplines = DECA_DISCIPLINES;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Track and Field Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(960, 640);

        // ==== TOP: Controls panel ============================================================
        JPanel controls = new JPanel();
        controls.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 6, 4, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        // First row panel (mode + name + add competitor)
        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));

        // Mode toggle (Decathlon/Heptathlon)
        decathlonRadio = new JRadioButton("Decathlon", true);
        heptathlonRadio = new JRadioButton("Heptathlon");
        ButtonGroup group = new ButtonGroup();
        group.add(decathlonRadio);
        group.add(heptathlonRadio);

        ActionListener modeListener = e -> {
            if (decathlonRadio.isSelected()) {
                activeDisciplines = DECA_DISCIPLINES;
            } else {
                activeDisciplines = HEPTA_DISCIPLINES;
            }
            refreshDisciplineBox();
            rebuildSnapshotTableColumns();
            refreshSnapshotTableData();
        };
        decathlonRadio.addActionListener(modeListener);
        heptathlonRadio.addActionListener(modeListener);

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        modePanel.add(new JLabel("Mode:"));
        modePanel.add(decathlonRadio);
        modePanel.add(heptathlonRadio);

        nameField = new JTextField(16);
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        namePanel.add(new JLabel("Name:"));
        namePanel.add(nameField);

        addCompetitorButton = new JButton("Add Competitor");
        addCompetitorButton.addActionListener(e -> addCompetitor());

        firstRow.add(modePanel);
        firstRow.add(namePanel);
        firstRow.add(addCompetitorButton);

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 6;
        gc.weightx = 1;
        controls.add(firstRow, gc);

        // Second row panel (discipline + result + buttons)
        JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));

        disciplineBox = new JComboBox<>(DECA_DISCIPLINES);
        JPanel discPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        discPanel.add(new JLabel("Discipline:"));
        discPanel.add(disciplineBox);

        resultField = new JTextField(8);
        JPanel resPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        resPanel.add(new JLabel("Result:"));
        resPanel.add(resultField);

        calculateButton = new JButton("Calculate Score");
        calculateButton.addActionListener(new CalculateButtonListener());

        exportButton = new JButton("Export to Excel");
        exportButton.addActionListener(new ExportButtonListener());

        secondRow.add(discPanel);
        secondRow.add(resPanel);
        secondRow.add(calculateButton);
        secondRow.add(exportButton);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 6;
        gc.weightx = 1;
        controls.add(secondRow, gc);

        frame.add(controls, BorderLayout.NORTH);

        // ==== CENTER: Output & Snapshot ======================================================
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setResizeWeight(0.4);

        // Output area (log)
        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(outputArea);

        // Snapshot table at bottom
        tableModel = new DefaultTableModel();
        snapshotTable = new JTable(tableModel);
        snapshotTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        rebuildSnapshotTableColumns();
        refreshSnapshotTableData();
        JScrollPane tableScroll = new JScrollPane(snapshotTable);

        split.setTopComponent(logScroll);
        split.setBottomComponent(tableScroll);

        frame.add(split, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void addCompetitor() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showMessageDialog(null, "Enter a name first.", "Missing name", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (competitors.containsKey(name)) {
            showMessageDialog(null, "Competitor already exists.", "Duplicate", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Competitor c = new Competitor(name);
        competitors.put(name, c);
        outputArea.append("Added competitor: " + name + "\n\n");
        refreshSnapshotTableData();
    }

    private void refreshDisciplineBox() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(activeDisciplines);
        disciplineBox.setModel(model);
        if (model.getSize() > 0) model.setSelectedItem(model.getElementAt(0));
    }

    private void rebuildSnapshotTableColumns() {
        // Columns: Name + active disciplines + Total
        Vector<String> cols = new Vector<>();
        cols.add("Name");
        Collections.addAll(cols, activeDisciplines);
        cols.add("Total");
        tableModel.setDataVector(new Vector<>(), cols);
    }

    private void refreshSnapshotTableData() {
        tableModel.setRowCount(0);
        for (Competitor c : competitors.values()) {
            tableModel.addRow(buildRowFor(c));
        }
        resizeColumnsToFit();
    }

    private Vector<Object> buildRowFor(Competitor c) {
        Vector<Object> row = new Vector<>();
        row.add(c.getName());
        int total = 0;
        for (String d : activeDisciplines) {
            Integer v = clientScores.getOrDefault(c.getName(), Collections.emptyMap()).get(d);
            row.add(v == null ? "" : v);
            if (v != null) total += v;
        }
        row.add(total);
        return row;
    }

    // client-side mirror of scores
    private final Map<String, Map<String, Integer>> clientScores = new LinkedHashMap<>();

    private void putClientScore(String name, String discipline, int score) {
        clientScores.computeIfAbsent(name, k -> new LinkedHashMap<>()).put(discipline, score);
    }

    private int computeClientTotal(String name) {
        Map<String, Integer> m = clientScores.get(name);
        if (m == null) return 0;
        int sum = 0;
        for (Integer v : m.values()) if (v != null) sum += v;
        return sum;
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a competitor name first.", "Missing name", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Competitor competitor = competitors.computeIfAbsent(name, Competitor::new);

            String discipline = (String) disciplineBox.getSelectedItem();
            String resultText = resultField.getText().trim();

            try {
                double result = Double.parseDouble(resultText);

                int score = calculateScoreForDiscipline(discipline, result);

                competitor.setScore(discipline, score);
                putClientScore(name, discipline, score);

                // Log details including total automatically
                int total = computeClientTotal(name);
                outputArea.append("Competitor: " + name + "\n");
                outputArea.append("Discipline: " + discipline + "\n");
                outputArea.append("Result: " + result + "\n");
                outputArea.append("Score: " + score + "\n");
                outputArea.append("Total so far: " + total + "\n\n");

                resultField.setText("");
                disciplineBox.setSelectedIndex(0);

                upsertRow(name);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for the result.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid Discipline", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error while calculating score: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void upsertRow(String name) {
        int nameCol = 0;
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            Object v = tableModel.getValueAt(r, nameCol);
            if (name.equals(v)) {
                int total = 0;
                for (int i = 0; i < activeDisciplines.length; i++) {
                    String d = activeDisciplines[i];
                    Integer sc = clientScores.getOrDefault(name, Collections.emptyMap()).get(d);
                    tableModel.setValueAt(sc == null ? "" : sc, r, 1 + i);
                    if (sc != null) total += sc;
                }
                tableModel.setValueAt(total, r, 1 + activeDisciplines.length);
                return;
            }
        }
        tableModel.addRow(buildRowFor(competitors.get(name)));
    }

    private int calculateScoreForDiscipline(String discipline, double result) {
        switch (discipline) {
            // ===== Decathlon =====
            case "100m":
                return new Deca100M().calculateResult(result);
            case "400m":
                return new Deca400M().calculateResult(result);
            case "1500m":
                return new Deca1500M().calculateResult(result);
            case "110m Hurdles":
                return new Deca110MHurdles().calculateResult(result);
            case "Long Jump":
                return new DecaLongJump().calculateResult(result);
            case "High Jump":
                return new DecaHighJump().calculateResult(result);
            case "Pole Vault":
                return new DecaPoleVault().calculateResult(result);
            case "Discus Throw":
                return new DecaDiscusThrow().calculateResult(result);
            case "Javelin Throw":
                return new DecaJavelinThrow().calculateResult(result);
            case "Shot Put":
                return new DecaShotPut().calculateResult(result);

            // ===== Heptathlon (assuming Hepta classes exist) =====
            // ===== Heptathlon (you need corresponding classes in your codebase) =====
            case "100m Hurdles":
                return new Hep100MHurdles().calculateResult(result);
            case "200m":
                return new Hep200M().calculateResult(result);
            case "800m":
                return new Hep800M().calculateResult(result);
            // Reuse field event classes if same formulas (else create Hepta* variants)
            case "Hep Long Jump":
                return new HeptLongJump().calculateResult(result);
            case "Hep High Jump":
                return new HeptHightJump().calculateResult(result);
            case "Hep Javelin Throw":
                return new HeptJavelinThrow().calculateResult(result);
            case "Hep Shot Put":
                return new HeptShotPut().calculateResult(result);
        }

        throw new

                IllegalArgumentException("Unknown discipline: " + discipline);
    }

    private class ExportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                exportToExcel();
            } catch (IOException ex) {
                showMessageDialog(null, "Failed to export results to Excel.", "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void exportToExcel() throws IOException {
        if (tableModel.getRowCount() == 0) {
            showMessageDialog(null, "Failed to export results to Excel. Nothing added yet", "Export Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


// Build export from what you SEE in the snapshot table (robust against internal storage)
        int rows = tableModel.getRowCount();
        int cols = tableModel.getColumnCount();


// +1 row for headers
        Object[][] data = new Object[rows + 1][cols];


// Headers
        for (int c = 0; c < cols; c++) {
            data[0][c] = tableModel.getColumnName(c);
        }


// Body
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Object val = tableModel.getValueAt(r, c);
                data[r + 1][c] = (val == null) ? "" : val; // keep numbers as Integer/Double if present
            }
        }


        ExcelPrinter printer = new ExcelPrinter("TrackAndFieldResults");
        printer.add(data, "Results");
        printer.write();
        showMessageDialog(null, "Results exported successfully!", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resizeColumnsToFit() {
        for (int c = 0; c < snapshotTable.getColumnCount(); c++) {
            int width = 75; // min
            for (int r = 0; r < snapshotTable.getRowCount(); r++) {
                Component comp = snapshotTable.prepareRenderer(snapshotTable.getCellRenderer(r, c), r, c);
                width = Math.max(comp.getPreferredSize().width + 16, width);
            }
            snapshotTable.getColumnModel().getColumn(c).setPreferredWidth(width);
        }
    }
}
