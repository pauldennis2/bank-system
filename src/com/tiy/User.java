package com.tiy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Created by erronius on 12/3/2016.
 */
public class User {
    private String name;
    private TreeMap<String, BankAccount> accountMap; //Put should not be called from anywhere except addAccount()
    //private LinkedList<Thread> interestThreads;

    public User (String name) {
        this.name = name;
        accountMap = new TreeMap<String, BankAccount>();
        //interestThreads = new LinkedList<Thread>();

        boolean success = initializeFromFile();
        if (success) {
            System.out.println(name + " successfully initialized.");
        } else {
            System.out.println("There was an error initializing user \"" + name + "\".");
            System.out.println("Please see comments above.");
        }
    }

    /*public void close () {
        for (Thread thread : interestThreads) {
            thread.interrupt();
        }
    }*/

    /*
        returns:
            true - Good Outcome. Either no file was found (in which case this is a new user) or the file was read successfully
            false - Bad Outcome. A user file WAS found but there was a problem reading it.
     */
    public boolean initializeFromFile () {
        try { //We will check if there is an existing file for this user. If yes, load details.
            File userFile = new File(name + ".txt");
            Scanner scanner = new Scanner(userFile);
            if (!scanner.nextLine().equals("user.name=" + name)) throw new AssertionError();
            while (scanner.hasNext()) {
                String accountName;
                String accountType;
                String accountInitBalance;
                String accountCurBalance;
                try {
                    accountName = scanner.nextLine();
                    accountType = scanner.nextLine();
                    accountInitBalance = scanner.nextLine();
                    accountCurBalance = scanner.nextLine();
                } catch (NoSuchElementException ex) {
                    System.out.println("File is improperly formatted. Incorrect number of tokens.");
                    ex.printStackTrace();
                    return false;
                }
                try { //Start outer (checks line starting tokens) try/catch block
                    if (!accountName.startsWith("account.name") ||
                        !accountType.startsWith("account.type") ||
                        !accountInitBalance.startsWith("account.initBalance") ||
                        !accountCurBalance.startsWith("account.currentBalance")) {
                        throw new ImproperFileFormatException();
                    } else {
                        accountName = accountName.split("=")[1];
                        accountType = accountType.split("=")[1];
                        try { //Start inner try/catch block (checks that numbers are properly formatted)
                            double initBalance = Double.parseDouble(accountInitBalance.split("=")[1]);
                            double curBalance = Double.parseDouble(accountCurBalance.split("=")[1]);
                            BankAccount account = BankAccount.createAccount(accountName, accountType, initBalance, curBalance);
                            if (account != null) {
                                this.addAccount(account);
                            } else {
                                return false; //BankAccount.createAccount(params) should already have generated an error message.
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("File improperly formatted. Expected a number, did not find.");
                            ex.printStackTrace();
                            return false;
                        }//End inner try/catch block
                    }
                } catch (ImproperFileFormatException ex){
                    System.out.println("File improperly formatted. Missing/incorrect line start tokens.");
                    ex.printStackTrace();
                    return false;
                } //End outer try/catch block
            } //End while(scanner.hasNext())
        } catch (FileNotFoundException ex) {
            System.out.println("No existing user file found. This must be a new user.");
            return true; //This is OK outcome. See function description
        }
        return true;
    }

    public void addAccount (BankAccount account) {
        accountMap.put(account.getName(), account);
        /*if (account.interestRate != 0.0) {
            Thread thread = new Thread(new InterestThread(account));
            thread.start();
            interestThreads.add(thread);
        }*/
        if (account.interestRate != 0.0) {
            new InterestThread(account);
        }
    }

    public void publishToFile () {
        try {
            File userFile = new File(name + ".txt");
            FileWriter userWriter = new FileWriter(userFile);
            userWriter.write("user.name=" + name + "\n");
            for (String accountName : accountMap.keySet()) {
                userWriter.write(accountMap.get(accountName).getFileString());
            }
            userWriter.close();
        } catch (Exception exception) {
            System.out.println("Exception while writing to file ...");
        }
    }

    public String getName () {
        return name;
    }

    public TreeMap<String, BankAccount> getAccountMap () {
        return accountMap;
    }
}
