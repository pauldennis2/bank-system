package com.tiy;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;

/**
 * Created by erronius on 12/5/2016.
 */
public class InterestThread implements Runnable{

    public static final boolean DEBUG = false;

    private static ArrayList<Thread> threads = new ArrayList<Thread>();

    BankAccount account;
    private int sleepTime;

    //Params: the account to watch, and how often to "remind" it to do interest
    public InterestThread (BankAccount account) {
        this.sleepTime = account.getSleepTime();
        this.account = account;
        Thread wrapperThread = new Thread(this);
        threads.add(wrapperThread);
        wrapperThread.start();
    }

    public static void closeThreads() {
        for (Thread thread: threads) {
            thread.interrupt();
        }
    }

    public void run() {
        try {
            while (true) {
                Thread.sleep(sleepTime);
                if (DEBUG) {
                    System.out.println("Interest report, Before:");
                    System.out.println(account);
                }
                account.applyInterest();
                if (DEBUG) {
                    System.out.println("After");
                    System.out.println(account);
                }
            }
        } catch (InterruptedException ex) {
            System.out.println("Thread closing.");
        }
    }
}
