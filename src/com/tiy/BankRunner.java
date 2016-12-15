package com.tiy;

import java.util.LinkedList;
import java.util.TreeMap;

public class BankRunner {

    private Bank bank;
    private SafeScanner scanner;

    public static void main(String[] args) {
	    BankRunner runner = new BankRunner();
        runner.run();
    }

    public BankRunner () {
    }

    public void run () {
        try {
            bank = new Bank("FirstPaulBank");
            scanner = new SafeScanner(System.in);
            mainMenu();
            bank.publishToFile();
            InterestThread.closeThreads();
        } catch (Exception ex) {
            ex.printStackTrace();
            InterestThread.closeThreads();//Threads will still close if we encounter an unexpected runtime error.
        }
    }

    public void mainMenu () {
        SafeScanner.clearScreen();
        while (true) {
            System.out.println("Welcome to " + bank.getName() + ". Please enter username (or \"exit\" to exit)");
            String response = scanner.nextStringSafe();
            if (response.equals("exit")) {
                System.out.println("Thank you for your business - have a nice day!");
                break;
            } else {
                User user = bank.findOrCreateUser(response); //Only added if not already in the map
                BankAccount account = accountActionsMenu(user);
                if (account != null) {
                    accountActionsMenu(user, account);
                }
                bank.publishToFile();
            }
        }
    }

    public BankAccount accountSelectionMenu (User user) {
        BankAccount account = null;
        SafeScanner.clearScreen();
        System.out.println("Welcome to the account selection menu, " + user.getName() + ".");
        TreeMap<String, BankAccount> accountMap = user.getAccountMap();
        if (accountMap.size() == 0) {
            System.out.println("You don't appear to have any accounts with us. Create one now?");
            if (scanner.nextYesNoAnswer()) {
                accountCreationMenu(user);
            } else {
                System.out.println("Awww... well, come back later!");
            }
        } else {
            System.out.println("Here are your accounts:");//Account name can only be 20 chars
            System.out.println("Account Name\t\tAccount Type\tInitial Balance\tCurrent Balance");
            TreeMap<String, BankAccount> userAccountMap = user.getAccountMap();
            for (String accountName : userAccountMap.keySet()) {
                System.out.println(userAccountMap.get(accountName));
            }
            System.out.println("Which account would you like to use?");
            account = userAccountMap.get(scanner.nextStringSafe());
            if (account == null) {
                System.out.println("Account not found");
            }
        }
        return account;
    }

    public BankAccount accountActionsMenu (User user) {
        System.out.println("What would you like to do?");
        System.out.println("1. Select account");
        System.out.println("2. Create new account");
        System.out.println("3. Return to main menu");

        int response = scanner.nextIntInRange(1, 3);

        switch (response) {
            case 1:
                return accountSelectionMenu(user);
            case 2:
                accountCreationMenu(user);
                return null;
            case 3:
                return null;
        }
        return null;
    }

    public BankAccount transferSelectionMenu (User user, BankAccount transferringAccount) {
        String transferringAccountName = transferringAccount.getName();
        BankAccount account = null;
        SafeScanner.clearScreen();
        TreeMap<String, BankAccount> userAccountMap = user.getAccountMap();
        if (userAccountMap.size() < 2) {
            System.out.println("You'd need another account with us to make a transfer. Create one now?");
            if (scanner.nextYesNoAnswer()) {
                accountCreationMenu(user);
            } else {
                System.out.println("Awww... well, come back later!");
            }
        } else {
            System.out.println("Here are your other accounts:");//Account name can only be 20 chars
            System.out.println("Account Name\t\tAccount Type\tInitial Balance\tCurrent Balance");
            for (String accountName : userAccountMap.keySet()) {
                if (!accountName.equals(transferringAccountName)) {
                    System.out.println(userAccountMap.get(accountName));
                }
            }
            System.out.println("Which account would you like to transfer to?");
            String response = scanner.nextStringSafe();
            if (!response.equals(transferringAccountName)) {
                account = userAccountMap.get(response);
            } else {
                account = null;
                System.out.println("Cannot transfer money from an account to itself.");
            }
            if (account == null) {
                System.out.println("Account not found/identity account.");
            }
        }
        return account;
    }

    public void accountActionsMenu (User user, BankAccount account) {
        SafeScanner.clearScreen();
        System.out.println("What would you like to do with account \"" + account.getName() + "\"?");
        System.out.println(account);

        System.out.println("1. Deposit funds");
        System.out.println("2. Withdraw funds");
        System.out.println("3. Transfer funds");
        System.out.println("4. Exit to main menu");
        int response = scanner.nextIntInRange(1, 4);

        switch (response) {
            case 1:
                System.out.println("How much would you like to deposit?");
                double depositAmount = scanner.nextPosDoubleSafe();
                account.depositMoney(depositAmount);
                System.out.println("Deposited " + depositAmount + ". Thank you!");
                accountActionsMenu(user, account);
                break;

            case 2:
                System.out.println("How much would you like to withdraw?");
                double withdrawAmount = scanner.nextPosDoubleSafe();
                double actualAmount = account.withdrawMoney(withdrawAmount);
                System.out.println("Withdrew " + actualAmount + ".");
                accountActionsMenu(user, account);
                break;

            case 3:
                BankAccount transferReceiver = transferSelectionMenu(user, account);
                if (transferReceiver != null) {
                    System.out.println("How much would you like to transfer?");
                    double transferAmount = scanner.nextPosDoubleSafe();
                    account.transferMoney(transferAmount, transferReceiver);
                }
                accountActionsMenu(user, account);
                break;

            case 4:
                System.out.println("Transferring to main menu");
                break;
        }
    }

    public void accountCreationMenu (User user) {
        System.out.println("That's wonderful! What type of account would you like to create?");
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
        System.out.println("3. Retirement Account");
        int accountType = scanner.nextIntInRange(1, 3);

        System.out.println("What would you like to call the account?");
        String accountName = scanner.nextStringSafe();
        System.out.println("And how much would you like to deposit in this account?");
        double initialBalance = scanner.nextPosDoubleSafe();
        BankAccount account = null;
        Thread thread;
        switch (accountType) {
            case 1:
                account = new CheckingAccount(accountName, initialBalance);
                break;

            case 2:
                account = new SavingsAccount(accountName, initialBalance);
                break;

            case 3:
                account = new RetirementAccount(accountName, initialBalance);
                break;

            default:
                //Should not be reached
                System.out.println("Houston we have a problem");
        }
        user.addAccount(account);
    }
}
