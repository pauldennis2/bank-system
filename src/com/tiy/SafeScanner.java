package com.tiy;

/**
 * Created by erronius on 12/3/2016.
 */

import java.util.Scanner;
import java.io.InputStream;
import java.util.InputMismatchException;

public class SafeScanner {

    private Scanner scanner;
    public static final boolean DEBUG = true;

    public SafeScanner() {
        scanner = new Scanner (System.in);
    }

    public SafeScanner (InputStream sysIn) {
        scanner = new Scanner (sysIn);
    }

    public int nextIntSafe () {
        System.out.println("Please input an integer:");
        int response = 0;
        try {
            response = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("You did not enter an integer.");
            scanner.next();
            return nextIntSafe();
        }
        return response;
    }

    public int nextPosIntSafe () {
        int response = -1;
        System.out.println("Please input a positive integer (x > 0)");
        try {
            response = scanner.nextInt();
            if (response < 1) {
                return nextPosIntSafe();
            }
        } catch (InputMismatchException e) {
            System.out.println("You did not enter an integer.");
            scanner.next();
            return nextPosIntSafe();
        }
        return response;
    }

    public int nextIntInRange (int min, int max) {
        int response = nextIntSafe();
        while (response < min || response > max) {
            System.out.println("Number must be between " + min + " and " + max + " inclusive.");
            response = nextIntSafe();
        }
        return response;
    }

    public double nextDoubleSafe () {
        System.out.println("Please input a double/floating point number.");
        double response = 0.0;
        try {
            response = scanner.nextDouble();
        } catch (InputMismatchException e) {
            System.out.println("You did not enter a double.");
            scanner.next();
            return nextDoubleSafe();
        }
        return response;
    }

    public double nextPosDoubleSafe () {
        double response = -1.0;
        System.out.println("Please input a positive double/float (x > 0.0)");
        try {
            response = scanner.nextDouble();
            if (!(response > 0.0)) {
                return nextPosDoubleSafe();
            }
        } catch (InputMismatchException e) {
            System.out.println("You did not enter a double/float.");
            scanner.next();
            return nextPosDoubleSafe();
        }
        return response;
    }

    public String nextStringSafe () {
        //System.out.println("Please input a string");
        return scanner.next();
    }

    public boolean nextBoolSafe () {
        System.out.println("Please input a boolean value. (\"true\" or \"false\")");
        boolean response = true;
        try {
            response = scanner.nextBoolean();
        } catch (InputMismatchException e) {
            System.out.println("You did not enter a boolean.");
            scanner.next();
            nextBoolSafe();
        }
        return response;
    }

    public boolean nextYesNoAnswer () {
        String response = scanner.next();
        if (response.contains("y") || response.contains("Y")) {
            return true;
        } else if (response.contains("n") || response.contains("N")) {
            return false;
        } else {
            System.out.println("Yes/no response required. (y/n, Yeah, Yep, Nope, No, Nein)");
            return nextYesNoAnswer ();
        }
    }

    public static void clearScreen () {
        if (!DEBUG) {
            String systemInfo = System.getProperty("os.name");
            if (systemInfo.contains("Windows")) {
                try {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } catch (Exception exception) {
                }
            } else if (systemInfo.contains("Mac")) {
                System.out.print("\033[H\033[2J"); //This is safe in windows, it just doesn't clear the screen.
            } else {
                System.out.println("Unable to determine system info. Unable to clear screen.");
            }
        }
    }
    /*
        Returns a string of length. If input is longer, it will be chopped off
        If input is shorter the remainder will be filled with spaces
     */
    public static String fixedLengthString (String input, int length) { //This feels like it should already exist in String
        if (input.length() > length) {
            return input.substring(0, length);
        }
        while (input.length() < length) {
            input += " ";
        }
        return input;
    }

    public static String displayDouble (double d) { //This feels like it should already exist in Double
        String response = "";
        int i = (int) (d * 100);
        d = (double) i/100; // we should now have a double with at most 2 digits after the .
        response += d;
        return response;
    }
}
