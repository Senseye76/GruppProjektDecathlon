package com.example.decathlon.deca;

import com.example.decathlon.common.CalcTrackAndField;
import com.example.decathlon.common.InputResult;

import javax.swing.*;

public class DecaHighJump {

	private int score;
	private double A = 0.8465;
	private double B = 75;
	private double C = 1.42;
	boolean active = true;
	CalcTrackAndField calc = new CalcTrackAndField();
	InputResult inputResult = new InputResult();

	// Calculate the score based on distance and height. Measured in centimeters.
	public int calculateResult(double distance) {

		while (active) {

			try {
                String message = "";
				// Acceptable values.
				if (distance < 0) {
                    message = "Value too low";
                    JOptionPane.showMessageDialog(null, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    active =false;
                    score = -1; //-1 indicates that no results should be displayed in the outputbox
//					System.out.println("Value too low");
//					distance = inputResult.enterResult();
				} else if (distance > 100) {
                    message = "Value too high";
                    JOptionPane.showMessageDialog(null, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    active = false;
                    score = -1; //-1 indicates that no results should be displayed in the outputbox
//					System.out.println("Value too high");
//					distance = inputResult.enterResult();
				} else {

					score = calc.calculateField(A, B, C, distance);
					active = false;
				}
			} catch (Exception e) {

				System.out.println("Please enter numbers");
			}
		}
		System.out.println("The result is: " + score);
		return score;
	}

}
