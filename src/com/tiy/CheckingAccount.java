package com.tiy;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by erronius on 12/3/2016.
 */
public class CheckingAccount extends BankAccount {

    public CheckingAccount (String name, double initialBalance) {
        super(name, initialBalance);
    }

    public CheckingAccount (String name, double initialBalance, double currentBalance) {
        super(name, initialBalance, currentBalance);
    }

    public void applyInterest () {
        System.out.println("Checking accounts do not generate interest. You're in the wrong place, bub.");
    }

    public String getFileString () {
        String response =
        "account.name=" + this.getName() + "\n" +
        "account.type=checking\n" +
        "account.initBalance=" + initialBalance + "\n" +
        "account.currentBalance=" + currentBalance + "\n";
        return response;
    }

    // If the balance numbers are 10 quadrillion or higher they will display improperly
    public String toString () {
        String response = SafeScanner.fixedLengthString(this.getName(), 20);
        response += SafeScanner.fixedLengthString("Checking", 16);
        response += SafeScanner.fixedLengthString(SafeScanner.displayDouble(initialBalance), 16);
        response += SafeScanner.fixedLengthString(SafeScanner.displayDouble(currentBalance), 16);
        return response;
    }
}
