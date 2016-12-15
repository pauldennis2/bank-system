package com.tiy;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by erronius on 12/3/2016.
 */
public class RetirementAccount extends BankAccount {
    public static final double DEFAULT_INTEREST_RATE = 1.1;

    public static final int DEFAULT_SLEEP_TIME = 120000;

    public RetirementAccount (String name, double initialBalance) {
        super(name, initialBalance);
        interestRate = DEFAULT_INTEREST_RATE;
        System.out.println("New Retirement account creating with interest " + interestRate);
        sleepTime = DEFAULT_SLEEP_TIME;
    }

    public RetirementAccount (String name, double initialBalance, double currentBalance) {
        super(name, initialBalance, currentBalance);
        interestRate = DEFAULT_INTEREST_RATE;
        System.out.println("New Retirement account creating with interest " + interestRate);
        sleepTime = DEFAULT_SLEEP_TIME;
    }

    public String getFileString () {
        String response =
                "account.name=" + this.getName() + "\n" +
                        "account.type=retirement\n" +
                        "account.initBalance=" + initialBalance + "\n" +
                        "account.currentBalance=" + currentBalance + "\n";
        return response;
    }

    public void applyInterest () {
        currentBalance *= interestRate;
    }

    // If the balance numbers are 10 quadrillion or higher they will display improperly
    public String toString () {
        String response = SafeScanner.fixedLengthString(this.getName(), 20);
        response += SafeScanner.fixedLengthString("Retirement", 16);
        response += SafeScanner.fixedLengthString(SafeScanner.displayDouble(initialBalance), 16);
        response += SafeScanner.fixedLengthString(SafeScanner.displayDouble(currentBalance), 16);
        return response;
    }
}
