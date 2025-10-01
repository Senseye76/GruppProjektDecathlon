package com.example.decathlon.common;

import java.util.Scanner;

public class InputResult {
	double inputResult = 0;

    public InputResult(double result){
        this.inputResult=result;
    }

    public double getInputResult(){return inputResult;}

	public double enterResult() {
		Scanner sc = new Scanner(System.in);
		boolean active = true;
		while (active) {
			System.out.println("Please enter the result:");
			try {

				inputResult = Double.parseDouble(sc.nextLine());
				active = false;
			} catch (Exception e) {
				System.out.println("Invalid input, try again.");
				
			}

		}
		return inputResult;
	}

	public double returnResult() {
		return inputResult;
	}
}
