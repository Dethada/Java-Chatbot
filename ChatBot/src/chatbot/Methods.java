/*
 * The methods for the chatbot are here
 */
package chatbot;

import java.util.Random;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    } // end Decimal2Bin

    // Takes binary/hex value and converts to decimal
    public static int decode(String value, String base) {
        int intBase = Integer.parseInt(base);

        //Integer.valueOf("AB", 16);
        return Integer.valueOf(value, intBase);
    } // end decode

    // Flips a coin
    public static String coinFlip() {
        // new Random object
        Random randomGenerator = new Random();

        String[] flip = {"heads", "tails"};
        return (flip[randomGenerator.nextInt(2)]);
    } // end coinFlip()

    // Gets current uv levels
    public static String getData() {
        String json = "";

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet("https://api.data.gov.sg/v1/environment/uv-index");
            getRequest.addHeader("api-key", "EPIFphWuoUyovMc6GGbJTm91B4JzAkE5");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                return "Chatbot: received HTTP error code : " + response.getStatusLine().getStatusCode();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String output;
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                json = output;
            }

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {
            return "Chatbot: check your internet connection\n";
        } catch (IOException e) {
            return "Chatbot: check your internet connection\n";
        }

        Gson gson = new Gson();

        UV uvAPI = gson.fromJson(json, UV.class);
        Items[] array = uvAPI.getItems();
        String items = array[0] + "";
        JsonElement jelement = new JsonParser().parse(items);
        JsonArray jarray = jelement.getAsJsonArray();

        Index index;
        String level;
        String output = "";

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
        } // End for loop

        return output;
    } // End getData()

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
} // End Methods class
