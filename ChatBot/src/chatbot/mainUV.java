/*
 * Gets live uv index data
 */
package chatbot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author David
 */
public class mainUV {

    static String json;

    public static void main(String[] args) {
        getData();
    }

    public static void getData() {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet("https://api.data.gov.sg/v1/environment/uv-index");
            getRequest.addHeader("api-key", "EPIFphWuoUyovMc6GGbJTm91B4JzAkE5");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                json = output;
            }

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
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
            
            output = "UV index at " + intTime + " " + morn_night + " is " + index.getValue() + " (" + level + ")\n";
            System.out.println(output);
            //System.out.println("UV index at " + intTime + " " + morn_night + " is " + index.getValue() + " (" + level + ")");
        } // End for loop

        //System.out.println(items);
    } // End getData()
} // End class
