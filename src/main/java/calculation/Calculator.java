package calculation;

import java.util.Observable;

public class Calculator extends Observable {

	public double calculateFinalBalance(double initialBalance, Double transactionSum) {
		double finalBalance = transactionSum + initialBalance;
		setChanged();
		notifyObservers(finalBalance);
		// TODO gerer les notification
		return finalBalance;
	}
}
