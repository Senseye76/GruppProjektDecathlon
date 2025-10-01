package com.example.decathlon.heptathlon;

import com.example.decathlon.common.CalcTrackAndField;
import com.example.decathlon.common.InputResult;

import javax.swing.*;

public class Hep800M {

	private int score;
	private double A = 0.11193;
	private double B = 254;
	private double C = 1.88;
	boolean active = true;
	CalcTrackAndField calc = new CalcTrackAndField();
	InputResult inputResult = new InputResult(0);

	// Calculate the score based on time. All running events.
	public int calculateResult(double runningTime) {

		while (active) {

			try {
                String message = "";
				// Acceptable values.
				if (runningTime < 70) {
                    message = "Value too low";
                    JOptionPane.showMessageDialog(null, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    active =false;
                    score = 0;
//					System.out.println("Value too low");
//					runningTime = inputResult.enterResult();
				} else if (runningTime > 250.79) {
                    message = "Value too high";
                    JOptionPane.showMessageDialog(null, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    active = false;
                    score = 0;
					// competitor gets 1 point in 250.79 sec
//					System.out.println("Value too high");
//					runningTime = inputResult.enterResult();
				} else {
					score = calc.calculateTrack(A, B, C, runningTime);
					active = false;
				}
			} catch (Exception e) {

				System.out.println("Please enter numbers");
			}
		}
		System.out.println("The result is " + score);
		return score;
	}

}
