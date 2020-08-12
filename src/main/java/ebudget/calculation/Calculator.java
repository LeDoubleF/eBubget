package ebudget.calculation;

import java.util.ArrayList;
import java.util.Observable;

public class Calculator extends Observable {

	public double calculateFinalBalance(double initialBalance, Double transactionSum) {
		double finalBalance = transactionSum + initialBalance;
		setChanged();
		notifyObservers(finalBalance);
		// TODO gerer les notification
		return finalBalance;
	}

	public static void AnalyseForecats(ArrayList<Double> balanceByMonth) {
		int from = 0, to = 0;
		ArrayList<String> monthList = new ArrayList<>();
		monthList.add("january");
		monthList.add("february");
		monthList.add("march");
		monthList.add("april");
		monthList.add("may");
		monthList.add("june");
		monthList.add("july");
		monthList.add("august");
		monthList.add("september");
		monthList.add("october");
		monthList.add("november");
		monthList.add("december");

		Double balance = 0.0;
		double initialAllowance = 0.0;
		double sumBalance = initialAllowance;
		for (String currentMonth : monthList) {

			balance = balanceByMonth.get(to);// requete sum(amount from forecast where +"val"==1;
			to++;
			System.out.println(currentMonth + " " + from + " " + to + " " + balance + " " + sumBalance);
			double allowance = sumBalance + balance;
			if (balance >= 0) {
				sumBalance = allowance;
				continue;
			} else {
				sumBalance = allowance;
				if (allowance >= 0) {
					from = to + 1;
					continue;
				} else {
					System.out.println("deficit du mois " + from + " jusqu'à " + to + " de " + allowance);
				}
			}
		}
	}

}
