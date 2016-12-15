package com.tiy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Created by erronius on 12/3/2016.
 */
public class Bank {
    private String name;
    private TreeMap<String, User> userMap; //@Dom: Yes, I have a bizarre attraction to maps. I love them. Deal with it =)
    private LocalDateTime closingTime;

    public static final boolean DEBUG = true;

    public Bank (String name) {
        this.name = name;
        userMap = new TreeMap<String, User>();
        boolean success = initializeFromFile();
        if (success) {
            System.out.println(name + " successfully initialized.");
        } else {
            System.out.println("There was an error initializing bank \"" + name + "\".");
            System.out.println("Please see comments above.");
        }
    }

    /*public void close () {
        for (String userName : userMap.keySet()) {
            userMap.get(userName).close();
        }
        closingTime = LocalDateTime.now();
    }*/
    /*
        returns:
            true - Good Outcome. Either no file was found (in which case this is a new bank) or the file was read successfully
            false - Bad Outcome. A bank file WAS found but there was a problem reading it.
     */
    public boolean initializeFromFile () {
        try { //We will check if there is an existing file for this bank. If yes, load details.
            File bankFile = new File(name + ".txt");
            Scanner scanner = new Scanner(bankFile);
            String currentLine = scanner.nextLine();
            if (!currentLine.equals("bank.name=" + name)) {
                System.out.println("Name does not match file.");
                throw new ImproperFileFormatException();
            }
            currentLine = scanner.nextLine();
            if (!currentLine.startsWith("bank.totalBalance=")) {
                System.out.println("Expected token not found. Should read \"bank.totalBalance=\".");
                throw new ImproperFileFormatException();
            } else {
                try {
                    double totalBalance = Double.parseDouble(currentLine.split("=")[1]);
                    //Unused, could come back and checksum here
                } catch (NumberFormatException ex) {
                    System.out.println("Improper file format. Expected number.");
                    ex.printStackTrace();
                    return false;
                }
            }//Total balance initialized, name is checked. Now we can read users
            //It's possible for the bank to have no users, so we'll use if (scanner.hasNext())
            if (scanner.hasNext()) {
                currentLine = scanner.nextLine();
                for (String userName : currentLine.split(",")) {
                    userMap.put(userName, new User(userName));
                }
            }
            //Reading the previous closing time. Probably should always be there but we'll be careful anyway
            if (scanner.hasNext()) {
                currentLine = scanner.nextLine();
                LocalDateTime previousClosingTime = LocalDateTime.parse(currentLine);
                applyInterestToAccounts(previousClosingTime);
            }
            return true;
        } catch (FileNotFoundException ex) {
            System.out.println("No existing bank file found. This must be a new bank.");
            return true; //Fine outcome. See above
        } catch (ImproperFileFormatException ex) {
            System.out.println("Improper file format. See above.");
            ex.printStackTrace();
            return false;
        } 
        //return false; //Bad outcome, shouldn't get here.
    }

    private void applyInterestToAccounts (LocalDateTime previousClosingTime) {
        LocalDate currentDate = LocalDate.now();
        LocalDate closingDate = previousClosingTime.toLocalDate();


        if (DEBUG) {
            System.out.println("For debug purposes you can increase the number of days since this program has been run.");
            System.out.println("How many 'extra' days should be added (max 10)?");
            SafeScanner scanner = new SafeScanner(System.in);
            int response = scanner.nextIntInRange(0, 10);
            closingDate = closingDate.minus(response, ChronoUnit.DAYS);
        }

        long numDaysBetween = ChronoUnit.DAYS.between(closingDate, currentDate);
        System.out.println("It has been " + numDaysBetween + " days since this program was run.");


        for (String userName : userMap.keySet()) {
            User user = userMap.get(userName);
            TreeMap<String, BankAccount> userAccountMap = user.getAccountMap();
            for (String accountName : userAccountMap.keySet()) {
                BankAccount account = userAccountMap.get(accountName);
                for (int interestPeriodsIndex = 0; interestPeriodsIndex < numDaysBetween; interestPeriodsIndex++) {
                    account.applyInterest();
                }
            }
        }

    }

    public User findOrCreateUser (String userName) {
        if (!userMap.keySet().contains(userName)) {
            userMap.put(userName, new User(userName));
            System.out.println("Thank you for trying " + name);
            System.out.println("We are slightly more honest than most banks!");
        } else {
            System.out.println("Welcome back, " + userName + "!");
        }
        return userMap.get(userName);//By this point should reliably be in the map
    }
    //Unused. Distinct from "findOrCreateUser" in that it simply returns the User associated with the username (could be null)
    public User getUser (String userName) {
        return userMap.get(userName);
    }

    public double calcTotalBalance () {
        double response = 0.0;
        for (String userName : userMap.keySet()) {
            User user = userMap.get(userName);
            TreeMap<String, BankAccount> userAccountMap = user.getAccountMap();
            for (String accountName : userAccountMap.keySet()) {
                response += userAccountMap.get(accountName).getCurrentBalance();
            }
        }
        return response;
    }

    public void publishToFile() {
        try {
            File bankFile = new File(name + ".txt");
            FileWriter bankWriter = new FileWriter(bankFile);
            bankWriter.write("bank.name=" + name + "\n");

            bankWriter.write("bank.totalBalance=" + calcTotalBalance() + "\n");
            String names = "";
            if (userMap.size() != 0) {
                for (String userName : userMap.keySet()) {
                    names += userName + ",";
                }
                names = names.substring(0, names.length() - 1); //This is a quick way to chop off that last messy comma
                bankWriter.write(names);
            }
            closingTime = LocalDateTime.now();
            bankWriter.write("\n" + closingTime.toString());
            bankWriter.close();
        } catch (Exception exception) {
            System.out.println("Exception while writing to file ...");
            exception.printStackTrace();
        }
        for (String userName : userMap.keySet()) {
            userMap.get(userName).publishToFile();
        }
    }

    public String getName () {
        return name;
    }
}
