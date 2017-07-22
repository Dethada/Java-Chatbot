/*
 * The methods for the chatbot are here
 */
package chatbot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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

        String[] flip = {"heads", "tails"};
        return (flip[randomGenerator.nextInt(2)]);
    }

    // Gets current uv levels
    public static String getData() {
        String json = "", output;
        Index index;
        String level;
        Gson gson = new Gson();

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet("https://api.data.gov.sg/v1/environment/uv-index");
            getRequest.addHeader("api-key", "EPIFphWuoUyovMc6GGbJTm91B4JzAkE5");

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

        UV uvAPI = gson.fromJson(json, UV.class);
        Items[] array = uvAPI.getItems();
        String items = array[0] + "";
        JsonElement jelement = new JsonParser().parse(items);
        JsonArray jarray = jelement.getAsJsonArray();

        output = "";

        for (int i = 0; i < jarray.size(); i++) {
            index = gson.fromJson(jarray.get(i), Index.class);

            String time = index.getTimestamp();
            time = time.substring(11, time.length() - 12); // formats the time
            String morn_night = "AM";
            int intTime = Integer.parseInt(time);

            // Check is am or pm
            if (intTime >= 12) {
                morn_night = "PM";
                intTime -= 12;
                if (intTime == 0) {
                    intTime = 12;
                }
            }

            // Checks uv level
            if (Integer.parseInt(index.getValue()) <= 3) {
                level = "Low";
            } else if (Integer.parseInt(index.getValue()) <= 5) {
                level = "Mod";
            } else if (Integer.parseInt(index.getValue()) <= 7) {
                level = "High";
            } else if (Integer.parseInt(index.getValue()) <= 10) {
                level = "Very High";
            } else {
                level = "Extreme";
            }

            output += "UV index at " + intTime + " " + morn_night + " is " + index.getValue() + " (" + level + ")\n";
        } // for

        return output;
    }

    public static String getQuote() {
        String json = "", output, author;
        Gson gson = new Gson();

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet("https://api.forismatic.com/api/1.0/?method=getQuote&lang=en&format=jsonp&jsonp=?");

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

        json = json.substring(2, json.length() - 1);
        Quote quote = gson.fromJson(json, Quote.class);
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
        for (String s:checks) {
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
        filtered = filtered.replaceAll("(\\ k +)", " okay ");
        filtered = filtered.replaceAll("(\\ y +)", " why ");
        filtered = filtered.replaceAll("(\\ wat +)", " what ");
        filtered = filtered.replaceAll("(\\ srry +)", " sorry ");
        filtered = filtered.replaceAll("(\\ luv +)", " love ");
        filtered = filtered.trim();

        return filtered;
    }

} // End Methods class
