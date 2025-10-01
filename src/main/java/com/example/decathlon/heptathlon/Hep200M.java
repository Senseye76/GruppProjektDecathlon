package com.example.decathlon.heptathlon;

import com.example.decathlon.common.CalcTrackAndField;
import com.example.decathlon.common.InputResult;

import javax.swing.*;

public class Hep200M {

	private int score;
	private double A = 4.99087;
	private double B = 42.5;
	private double C = 1.81;
	boolean active = true;
	CalcTrackAndField calc = new CalcTrackAndField();
	InputResult inputResult = new InputResult(0);

	// Calculate the score based on time. All running events.
	public int calculateResult(double runningTime) {

		while (active) {

			try {
                String message = "";
				// Acceptable values.
				if (runningTime < 14) {
                    message = "Value too low";
                    JOptionPane.showMessageDialog(null, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    active =false;
                    score = 0;
//					System.out.println("Value too low");
//					runningTime = inputResult.enterResult();
				} else if (runningTime > 42.08) {
                    message = "Value too high";
                    JOptionPane.showMessageDialog(null, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    active = false;
                    score = 0;
					// get 1 point in 42.08sec
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
