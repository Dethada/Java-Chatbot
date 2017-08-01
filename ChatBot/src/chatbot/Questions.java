/*
 * Allow users to add their own questions and answers
 */
package chatbot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author David
 */
public class Questions {

    private final javax.swing.JTextField questionField;
    private final javax.swing.JTextField answerField;
    private HashMap<String, String> questionAnswer;

    public Questions(String filename) {
        questionField = new javax.swing.JTextField();
        answerField = new javax.swing.JTextField();

        // Read questions from file
        try {
            FileInputStream fileIn = new FileInputStream(Paths.get(".").toAbsolutePath().normalize().toString() + "/" + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            questionAnswer = (HashMap<String, String>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            JOptionPane.showMessageDialog(null, filename + " might be missing", "Error", 0);
        }
    }

    // Add question and answer
    public String setQuestion() {
        Object[] input = {"Question:", questionField, "Answer:", answerField};

        int option = JOptionPane.showConfirmDialog(null, input, "Set Question", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String questionText = Methods.filter(questionField.getText());
            String answerText = answerField.getText();
            if (!questionText.isEmpty() && !answerText.isEmpty()) {
                questionAnswer.put(questionText, answerText);
                questionField.setText("");
                answerField.setText("");
                try {
                    FileOutputStream fileOut = new FileOutputStream(Paths.get(".").toAbsolutePath().normalize().toString() + "/questions.ser");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(questionAnswer);
                    out.close();
                    fileOut.close();
                    return "Question successfully added";
                } catch (IOException i) {
                    return "IOException Error";
                }
            } else {
                return "Invalid input";
            }
        } else {
            questionField.setText("");
            answerField.setText("");
            return "Cancelled";
        }
    }

    // Remove a question
    public String rmQuestion(String question) {
        for (String key : questionAnswer.keySet()) {
            if (question.equals(key)) {
                questionAnswer.remove(question);
                try {
                    FileOutputStream fileOut = new FileOutputStream(Paths.get(".").toAbsolutePath().normalize().toString() + "/questions.ser");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(questionAnswer);
                    out.close();
                    fileOut.close();
                    return "Question successfully removed";
                } catch (IOException i) {
                    return "IOException Error";
                }
            }
        }
        return "Error no such question";
    }

    // Checks if input is a set question, reply with set answer if it is
    // Returns a empty string if no matching answer is found
    public String answer(String filteredInput) {
        for (Map.Entry question : questionAnswer.entrySet()) {
            if (filteredInput.equals(question.getKey())) {
                return "" + question.getValue();
            }
        }
        return "";
    }

    // List all questions
    public String questions() {
        String output = "";
        for (String s : questionAnswer.keySet()) {
            output += s + "\n";
        }
        return output;
    }
}
