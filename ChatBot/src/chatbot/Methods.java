/*
 * The methods for the chatbot are here
 */
package chatbot;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author David
 */
public class Methods {

    // Takes a decimal number as arg then returns binary and
    // hex value of the decimal as a string
    public static String Decimal2Bin(String value) {
        int intValue = Integer.parseInt(value);

        // Converts and prints the answer
        return intValue + " in binary: " + Integer.toBinaryString(intValue) + ", " + intValue
                + " in hexadecimal: " + (Integer.toHexString(intValue)).toUpperCase();
    }

    // Takes binary/hex value and converts to decimal
    public static int decode(String value, String base) {
        int intBase = Integer.parseInt(base);

        //Integer.valueOf("AB", 16);
        return Integer.valueOf(value, intBase);
    }

    // Flips a coin
    public static String coinFlip() {
        Random randomGenerator = new Random();

        String[] flip = {"Heads", "Tails"};
        return (flip[randomGenerator.nextInt(2)]);
    }

    // Sends get request to url
    public static String getData(String url, String header, String value) {
        String json = "", output;

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(url);
            getRequest.addHeader(header, value);

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                return "Chatbot: received HTTP error code : " + response.getStatusLine().getStatusCode();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            while ((output = br.readLine()) != null) {
                json = output;
            }

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {
            return "Chatbot: check your internet connection\n";
        } catch (IOException e) {
            return "Chatbot: Error IOException occured\n";
        }

        return json;
    }

    // Gets current uv levels
    public static String uvLevels() {
        Gson gson = new Gson();
        String output;
        String data = getData("https://api.data.gov.sg/v1/environment/uv-index", "api-key", "EPIFphWuoUyovMc6GGbJTm91B4JzAkE5");

        // Returns error message if any
        if (data.contains("Chatbot:")) {
            return data;
        }

        UV uvAPI = gson.fromJson(data, UV.class);
        Index[] indexArray = uvAPI.getItems()[0].getIndex();

        output = "";

        for (Index index : indexArray) {
            int uvLevel = Integer.parseInt(index.getValue());
            int intTime = Integer.parseInt(index.getTimestamp().substring(11, 13));
            String am_pm = "AM", level;

            // Check is am or pm
            if (intTime >= 12) {
                am_pm = "PM";
                intTime -= 12;
                if (intTime == 0) {
                    intTime = 12;
                }
            }

            // Checks uv level
            if (uvLevel <= 3) {
                level = "Low";
            } else if (uvLevel <= 5) {
                level = "Mod";
            } else if (uvLevel <= 7) {
                level = "High";
            } else if (uvLevel <= 10) {
                level = "Very High";
            } else {
                level = "Extreme";
            }

            output += "UV index at " + intTime + " " + am_pm + " is " + uvLevel + " (" + level + ")\n";
        }

        return output;
    }

    public static String getQuote() {
        String data, author;
        Gson gson = new Gson();

        data = getData("https://api.forismatic.com/api/1.0/?method=getQuote&lang=en&format=jsonp&jsonp=?", "", "");
        if (data.contains("Chatbot:")) {
            return data;
        }

        data = data.substring(2, data.length() - 1);
        Quote quote = gson.fromJson(data, Quote.class);
        if (quote.getQuoteAuthor().equals("")) {
            author = "Unknown";
        } else {
            author = quote.getQuoteAuthor();
        }

        return "\"" + quote.getQuoteText() + "\"" + " - " + author;
    }

    // Reads replies from file and stores into ArrayList
    public static ArrayList<String> readFile(String path) {
        ArrayList<String> array = new ArrayList<>();

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String line;

            while ((line = br.readLine()) != null) {
                array.add(line);
            }
            br.close();

        } catch (IOException e) {
            System.out.println("ERROR: unable to read file " + path);
        }

        return array;
    }

    // Switches panels
    public static void changePanel(javax.swing.JPanel mainPanel, javax.swing.JPanel panel) {
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();

        mainPanel.add(panel);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    // Checks if the main string provided contains the strings to be checked
    public static boolean checkContains(String main, String... checks) {
        for (String s : checks) {
            if (main.contains(s)) {
                return true;
            }
        }
        return false;
    }

    // Filters out non-alphanumeric characters and replaces shortforms
    public static String filter(String text) {
        String filtered;

        filtered = " " + text.toLowerCase().replaceAll("[^A-Za-z0-9' ]", "") + " ";
        filtered = filtered.replaceAll("(\\ u +)", " you ");
        filtered = filtered.replaceAll("(\\ r +)", " are ");
        filtered = filtered.replaceAll("(\\ thx +)", " thanks ");
        filtered = filtered.replaceAll("(\\ ur +)", " your ");
        filtered = filtered.replaceAll("(\\ k +)", " ok ");
        filtered = filtered.replaceAll("(\\ okay +)", " ok ");
        filtered = filtered.replaceAll("(\\ y +)", " why ");
        filtered = filtered.replaceAll("(\\ wat +)", " what ");
        filtered = filtered.replaceAll("(\\ srry +)", " sorry ");
        filtered = filtered.replaceAll("(\\ luv +)", " love ");
        filtered = filtered.replaceAll("(\\ yea +)", " yes ");
        filtered = filtered.replaceAll("(\\ ye +)", " yes ");
        filtered = filtered.replaceAll("(\\ yeah +)", " yes ");
        filtered = filtered.trim();

        return filtered;
    }

} // End Methods class
