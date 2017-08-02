
import chatbot.Question;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author David
 */
public class test {

    private final ArrayList<Question> questionz;

    public test() {
        this.questionz = new ArrayList<>();
    }

    public void doShit() {
        questionz.add(new Question("testing", "abc, def, ghi"));
        System.out.println(writeAnswers());
    }

    public static void main(String[] args) {
        test t = new test();
        t.doShit();
    }

    private String writeAnswers() {
        try {
            FileOutputStream fileOut = new FileOutputStream(Paths.get(".").toAbsolutePath().normalize().toString() + "/questions.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(questionz);
            out.close();
            fileOut.close();
            return "Write successful";
        } catch (IOException i) {
            i.printStackTrace();
            return "";
        }
    }

} // End test class
