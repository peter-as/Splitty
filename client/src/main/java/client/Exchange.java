package client;

import client.utils.ServerUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.UnexpectedException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Exchange money from one currency to another.
 */
public class Exchange {

    private ServerUtils server;
    private static final String filePath = "client/src/main/java/client/rates/";

    /**
     * Constructor for the exchange
     * @param server the server
     */
    public Exchange(ServerUtils server) {
        this.server = server;
    }

    /**
     * Exchanges from one currency to another
     * @param from The currency the amount is exchanged from
     * @param to The currency the amount is exchanged to
     * @param amount The amount itself that we want to exchange
     * @return The calculated amount for the other currency
     * @throws IOException exception if something goes wrong with the request
     * @throws InterruptedException exception for if the request got interrupted
     */
    public int exchangeCurrencies(String from, String to, int amount, Date d) {
        String date = extractDate(d);
        JsonNode exchangeRates;
        File f = new File(filePath + date + ".txt"); //the file which should have the exchange rates
        try {
            if (!f.isFile()) { //checks if the file exists
                exchangeRates = server.getRates(date); //fetches the exchange rates
                toFile(exchangeRates, f); //saves it to a file
                if (!exchangeRates.has(to) || !exchangeRates.has(from)) {
                    //throws an exception if either of the currencies are unknown
                    throw new UnexpectedException("No currency with this name exists!");
                }
            } else { //reads it from the file
                exchangeRates = readDate(f);
            }
        } catch (Exception e) {
            return 0;
        }
        //Since the API only allows conversions from USD,
        // first have to convert to USD
        float differenceToUsd = exchangeRates.get(from).floatValue();
        float newValue = amount / differenceToUsd;
        //Then fetch the exchange rate from USD to the other currency.
        float difference = exchangeRates.get(to).floatValue();
        return Math.round(difference * newValue);
    }

    /**
     * Reads the exchange rates from a file
     * @param f the file we read it from
     * @return the json object made from the file
     * @throws IOException exception for IO
     */
    private static JsonNode readDate(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        String text = reader.readLine();
        return new ObjectMapper().readTree(text);
    }

    /**
     * Turns the fetched JSON object into a file
     * @param request the JSON object
     * @param f the file we want to save it to
     * @throws IOException exception for IO
     */
    private static void toFile(JsonNode request, File f) throws IOException {
        FileWriter writer = new FileWriter(f);
        writer.write(request.toString());
        writer.flush();
        writer.close();
    }

    /**
     * Turns a Date object into its string version
     * @param date the date object
     * @return the extracted date string
     */
    public String extractDate(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        if (today(date)
                && calendar.get(Calendar.HOUR_OF_DAY) < 3) {
            date = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        }
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * Checks if the date is today
     * @param date the date
     * @return if it is today or not
     */
    public boolean today(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String time1 = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH)
                + "-" + cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(new Date());
        String time2 = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH)
                + "-" + cal.get(Calendar.DAY_OF_MONTH);
        return time1.equals(time2);
    }
}

