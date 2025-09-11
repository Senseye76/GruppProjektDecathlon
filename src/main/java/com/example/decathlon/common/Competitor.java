package com.example.decathlon.common;

public class Competitor {
    private String name;
    private int[] scores = new int[17];  // Store scores for 17 event

    public Competitor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setScore(String discipline, int score) {
        switch (discipline) {
            // Decathlon Events
            case "Deca 100m":
                scores[0] = score;
                break;
            case "Deca 400m":
                scores[1] = score;
                break;
            case "Deca 1500m":
                scores[2] = score;
                break;
            case "Deca 110m Hurdles":
                scores[3] = score;
                break;
            case "Deca Long Jump":
                scores[4] = score;
                break;
            case "Deca High Jump":
                scores[5] = score;
                break;
            case "Deca Pole Vault":
                scores[6] = score;
                break;
            case "Deca Discus Throw":
                scores[7] = score;
                break;
            case "Deca Javelin Throw":
                scores[8] = score;
                break;
            case "Deca Shot Put":
                scores[9] = score;
                break;

            // Heptathlon Events
            case "Hep 100m Hurdles":
                scores[10] = score;
                break;
            case "Hep 200m":
                scores[11] = score;
                break;
            case "Hep 800m":
                scores[12] = score;
                break;
            case "Hep High Jump":
                scores[13] = score;
                break;
            case "Hep Long Jump":
                scores[14] = score;
                break;
            case "Hep Shot Put":
                scores[15] = score;
                break;
            case "Hep Javelin Throw":
                scores[16] = score;
                break;
        }
    }

    public Object[] getRowData() {
        int totalScore = 0;
        for (int score : scores) {
            totalScore += score;
        }

        Object[] rowData = new Object[scores.length + 2];  // Object array
        rowData[0] = name;  // Name is fine

        // Convert int[] scores to Integer[] and then copy to Object[]
        for (int i = 0; i < scores.length; i++) {
            rowData[i + 1] = scores[i];  // Converting int to Integer (auto-boxing)
        }

        rowData[scores.length + 1] = totalScore;  // Total score in the last column

        return rowData;
    }

}

