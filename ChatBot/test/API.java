/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//        URL url = new URL("https://developers.data.gov.sg/environment/uv-index");
//        String query = "'date_time': '2017-05-03T12:45:00', 'date': '2017-05-03', 'api-key': '123'";
/**
 *
 * @author David
 */
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.io.*;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

public class API {

    public static void main(String[] args) {
        new API().testIt();
    }

    private void testIt() {

        String https_url = "https://api.data.gov.sg/v1/environment/uv-index";
        String json = "{'date_time': '2017-05-03T12:45:00', 'date': '2017-05-03', 'api-key': '123'}";
        URL url;
        try {

            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setDoOutput(true);

            //add reuqest header
            con.setRequestMethod("POST");
            con.addRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("", json);

            OutputStream os = con.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            //dumpl all cert info
            //print_https_cert(con);
            //dump all the content
            print_content(con);

            con.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void print_https_cert(HttpsURLConnection con) {

        if (con != null) {

            try {

                System.out.println("Response Code : " + con.getResponseCode());
                System.out.println("Cipher Suite : " + con.getCipherSuite());
                System.out.println("\n");

                Certificate[] certs = con.getServerCertificates();
                for (Certificate cert : certs) {
                    System.out.println("Cert Type : " + cert.getType());
                    System.out.println("Cert Hash Code : " + cert.hashCode());
                    System.out.println("Cert Public Key Algorithm : "
                            + cert.getPublicKey().getAlgorithm());
                    System.out.println("Cert Public Key Format : "
                            + cert.getPublicKey().getFormat());
                    System.out.println("\n");
                }

            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void print_content(HttpsURLConnection con) throws IOException {
        InputStream is;
        if (con != null) {

            try {
                if (con.getResponseCode() == 400) {
                    is = con.getErrorStream();
                } else {
                    is = con.getInputStream();
                }
                System.out.println(is);
                System.out.println("****** Content of the URL ********");
                BufferedReader br
                        = new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null) {
                    System.out.println(input);
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
