package com.example.decathlon.heptathlon;

import com.example.decathlon.common.CalcTrackAndField;
import com.example.decathlon.common.InputResult;

import javax.swing.*;

public class HeptHightJump {

	private int score;
	private double A = 1.84523;
	private double B = 75;
	private double C = 1.348;
	boolean active = true;
	CalcTrackAndField calc = new CalcTrackAndField();
	InputResult inputResult = new InputResult(0);

	// Calculate the score based on distance and height. Measured in cenimeters.
	public int calculateResult(double distance) {

		while (active) {

			try {
                String message = "";
				// Acceptable values in cm
				if (distance < 75.7) {
                    message = "Value too low";
                    JOptionPane.showMessageDialog(null, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    active =false;
                    score = 0;
//					System.out.println("Value too low");
//					distance = inputResult.enterResult();
				} else if (distance > 270) {
                    message = "Value too high";
                    JOptionPane.showMessageDialog(null, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    active = false;
                    score = 0;
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
