/*
 * Json parser
 */
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author David
 */
public class moreTesting {

    public static void main(String[] args) {
        Gson gson = new Gson();

        try (Reader reader = new FileReader("E:\\Programming\\Java ChatBot\\ChatBot\\json.json")) {

            // Convert JSON to Java Object
            moreTesting staff = gson.fromJson(reader, moreTesting.class);
            System.out.println(staff);

            // Convert JSON to JsonElement, and later to String
            JsonElement json = gson.fromJson(reader, JsonElement.class);
            String jsonInString = gson.toJson(json);
            System.out.println(jsonInString);
        } catch (IOException e) {
            e.printStackTrace();
        }

    } // End main

} // End Class
