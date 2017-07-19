
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author David
 */
public class test {

    public static void main(String[] args) {
        HashMap<String, String> qa = null;
        try {
            FileInputStream fileIn = new FileInputStream(Paths.get(".").toAbsolutePath().normalize().toString() + "/questions.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            qa = (HashMap<String, String>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.out.println("IOException");
        } catch (ClassNotFoundException c) {
            System.out.println("ClassNotFoundException");
        }
//        HashMap<String, String> qa = new HashMap<>();
//        qa.put("what is your name", "My name is Uvuvwevwevwe Onyetenyevwe Ugwemuhwem Osas");
//        qa.put("who are you", "A bot.");
//        qa.put("how are you", "I am fine thankyou");
//        try {
//            FileOutputStream fileOut = new FileOutputStream(Paths.get(".").toAbsolutePath().normalize().toString() + "/questions.ser");
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(qa);
//            out.close();
//            fileOut.close();
//            System.out.println("Serialized data is saved in questions.ser");
//        } catch (IOException i) {
//            i.printStackTrace();
//        }
        boolean exit = false;
        qa.remove("cb ");
        for (Map.Entry i : qa.entrySet()) {
            System.out.println(i.toString());
        }
        for (String s : qa.keySet()) {
            System.out.println(s);
        }
        
//        while (!exit) {
//            System.out.print("You: ");
//            Scanner in = new Scanner(System.in);
//            String input = in.nextLine();
//
//            if (input.contains("set question")) {
//                String question = JOptionPane.showInputDialog(null, "Question");
//                String answer = JOptionPane.showInputDialog(null, "Answer");
//                qa.put(question, answer);
//                try {
//                    FileOutputStream fileOut = new FileOutputStream(Paths.get(".").toAbsolutePath().normalize().toString() + "/questions.ser");
//                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
//                    out.writeObject(qa);
//                    out.close();
//                    fileOut.close();
//                    System.out.println("Serialized data is saved in questions.ser");
//                } catch (IOException i) {
//                    i.printStackTrace();
//                }
//            } else if (input.equalsIgnoreCase("exit")) {
//                System.out.println("Exiting");
//                exit = true;
//            } else {
//                for (Map.Entry m : qa.entrySet()) {
//                    String x = "" + m.getKey();
//                    if (input.contains(x)) {
//                        System.out.println(m.getValue());
//                    }
//                }
//            }
//        }
    } // main()

} // End test class
