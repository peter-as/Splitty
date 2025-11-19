package client.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;


public class CommonUtils {

    /**
     * Reads the currently used currency from the config file
     * @return the current currency
     */
    public static String readCurrency(Path path) {
        BufferedReader reader;
        try {
            reader = Files.newBufferedReader(path);
            reader.readLine();
            return reader.readLine().split(":")[1];

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Turns an integer into a string with a decimal dot in it
     * @param cost the integer
     * @return the string
     */
    public static String moneyToText(int cost) {
        String costString = Integer.toString(cost);
        if (costString.length() < 3) {
            for (int i = 0; i < 4 - costString.length(); i++) {
                costString += "0";
            }
        }
        return costString.substring(0, costString.length() - 2) + "."
                + costString.substring(costString.length() - 2);
    }
}
