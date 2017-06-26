//        URL url = new URL("https://api.data.gov.sg/v1/environment/uv-index");
//        String query = "'date_time': '2017-05-03T12:45:00', 'date': '2017-05-03', 'api-key': 'EPIFphWuoUyovMc6GGbJTm91B4JzAkE5'";
// =https%3A%2F%2Fapi.data.gov.sg%2Fv1%2Fenvironment%2Fuv-index%3Fdate_time%3D2017-05-16T18%253A45%253A00%26date%3D2017-05-16&_=1494940371474
// https://api.data.gov.sg/v1/environment/uv-index?date_time=2017-05-16T18%3A45%3A00&date=2017-05-16&_=1494940371474

/**
 *
 * @author David
 */
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class restAPI {

    public final static void main(String[] args) {

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
                System.out.println(output);
            }

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }
}
