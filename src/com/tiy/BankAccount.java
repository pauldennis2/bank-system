package com.tiy;

import java.time.LocalDateTime;

/**
 * Created by erronius on 12/3/2016.
 */
public abstract class BankAccount {

    private String name; //Primary identifier
    //Example: "Paul's Checking Account", "Paul's Second Checking Account", "PaulRetirementAccount"
    double initialBalance;
    double currentBalance;

    double interestRate = 0.0;

    LocalDateTime creationDateTime;
    LocalDateTime lastTransactionDateTime;
    //Transaction includes: initialize account, deposit money, transfer money, withdraw money
    //Transaction does not include: viewing money (or other variables) through getter(s), accruing interest

    int sleepTime;

    //@Dom Again, if I were doing this assignment outside the parameters of the class, I would not use inheritance
    //Since the only thing that changes in the subclasses is the interest rate. I'd represent that with a variable

    //Creating a new account ONLY
    public BankAccount (String name, double initialBalance) {
        this.name = name;
        this.initialBalance = initialBalance;
        currentBalance = initialBalance;
        interestRate = 0;
        creationDateTime = LocalDateTime.now();
        lastTransactionDateTime = LocalDateTime.now();//For our purposes we are counting account creation as a transaction
    }

    //Creating a new account (values should be same) or loading a previous account
    public BankAccount (String name, double initialBalance, double currentBalance) {
        this.name = name;
        this.initialBalance = initialBalance;
        this.currentBalance = currentBalance;
        creationDateTime = LocalDateTime.now();
        lastTransactionDateTime = LocalDateTime.now();//For our purposes we are counting account creation as a transaction
    }

    public String getName () {
        return name;
    }

    public int getSleepTime () {
        return sleepTime;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public void depositMoney (double amount) {
        lastTransactionDateTime = LocalDateTime.now(); //A zero deposit will sort of fool this.
        currentBalance += amount;
    }

    //Attempts to withdraw money from the account, returning either the amount requested, or
    //if balance is insufficient, the currentBalance
    public double withdrawMoney (double amount) {
        if (amount > currentBalance) {
            System.out.println("Insufficient balance in " + name + " to withdraw " + amount + ".");
            System.out.println("Withdrew current balance of " + currentBalance + " instead.");
            double d = currentBalance;
            currentBalance = 0.0;
            return d;
        }
        currentBalance -= amount;
        lastTransactionDateTime = LocalDateTime.now();
        return amount;
    }

    public double transferMoney (double amount, BankAccount otherAccount) {
        double amountTransferred = this.withdrawMoney(amount);
        otherAccount.depositMoney(amountTransferred);
        lastTransactionDateTime = LocalDateTime.now();
        //The other account's deposit method will update its last transaction
        return amountTransferred;
    }

    public abstract String getFileString();
    public abstract String toString ();
    public abstract void applyInterest();

    //Helper method called from User
    public static BankAccount createAccount (String name, String type, double initBalance, double currentBalance) {
        switch (type) {
            case "checking":
                return new CheckingAccount(name, initBalance, currentBalance);

            case "savings":
                SavingsAccount act = new SavingsAccount(name, initBalance, currentBalance);
                System.out.println("BankAccount.createAccount() is creating new account:\n" + act);
                return act;

            case "retirement":
                return new RetirementAccount(name, initBalance, currentBalance);

            default:
                System.out.println("Account type is improper. Expected one of the following:");
                System.out.println("checking\nsavings\nretirement");
                System.out.println("Received: \"" + type + "\"");
        }
        System.out.println("Returning null");
        return null;
    }
}
