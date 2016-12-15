package com.tiy;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by erronius on 12/3/2016.
 */
public class SavingsAccount extends BankAccount {

    public static final double DEFAULT_INTEREST_RATE = 1.05;
    public static final int DEFAULT_SLEEP_TIME = 10000;

    public SavingsAccount (String name, double initialBalance) {
        super(name, initialBalance);
        interestRate = DEFAULT_INTEREST_RATE;
        sleepTime = DEFAULT_SLEEP_TIME;
    }

    public SavingsAccount (String name, double initialBalance, double currentBalance) {
        super(name, initialBalance, currentBalance);
        interestRate = DEFAULT_INTEREST_RATE;
        sleepTime = DEFAULT_SLEEP_TIME;
    }

    public void applyInterest () {
        currentBalance *= interestRate;
    }

    public String getFileString () {
        String response =
                "account.name=" + this.getName() + "\n" +
                "account.type=savings\n" +
                "account.initBalance=" + initialBalance + "\n" +
                "account.currentBalance=" + currentBalance + "\n";
        return response;
    }

    // If the balance numbers are 10 quadrillion or higher they will display improperly
    public String toString () {
        String response = SafeScanner.fixedLengthString(this.getName(), 20);
        response += SafeScanner.fixedLengthString("Savings", 16);
        response += SafeScanner.fixedLengthString(SafeScanner.displayDouble(initialBalance), 16);
        response += SafeScanner.fixedLengthString(SafeScanner.displayDouble(currentBalance), 16);
        return response;
    }
}
