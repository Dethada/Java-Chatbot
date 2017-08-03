/*
 * Gets user input and replies them
 */
package chatbot;

import static chatbot.GUI.clock;
import static chatbot.GUI.player;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author David
 */
public class Replies {

    private String input;
    private String filteredInput;
    private ArrayList<Question> questions;
    private final ArrayList<String> greetings;
    private final ArrayList<String> jokes;
    private final ArrayList<String> goodbye;
    private final ArrayList<String> defaultReply;
    private final ArrayList<String> sorry;
    // RNG
    private final RNG rngReply;
    private final RNG rngTime;
    private final javax.swing.JTextField questionField;
    private final javax.swing.JTextField answerField;

    public Replies() {
        this.rngReply = new RNG();
        this.rngTime = new RNG();
        questionField = new javax.swing.JTextField();
        answerField = new javax.swing.JTextField();

        // Read stored replies from file
        greetings = Methods.readFile(System.getProperty("user.dir") + "\\replies\\greetings.txt");
        jokes = Methods.readFile(System.getProperty("user.dir") + "\\replies\\jokes.txt");
        goodbye = Methods.readFile(System.getProperty("user.dir") + "\\replies\\goodbye.txt");
        defaultReply = Methods.readFile(System.getProperty("user.dir") + "\\replies\\default.txt");
        sorry = Methods.readFile(System.getProperty("user.dir") + "\\replies\\sorry.txt");

        //Read question objects from file
        try {
            FileInputStream fileIn = new FileInputStream(Paths.get(".").toAbsolutePath().normalize().toString() + "/questions.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            questions = (ArrayList<Question>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            JOptionPane.showMessageDialog(null, "questions.ser might be missing", "Error", 0);
        }
    }

    public void getReply() {
        new Thread("Reply") {
            @Override
            public void run() {
                try {
                    // Get user input and filter it, if input is empty do nothing.
                    input = GUI.getInput();
                    if (input.equals("")) {
                        return;
                    }
                    filteredInput = Methods.filter(input);

                    // Display input
                    GUI.userPrintf(input);

                    // Allow user to add questions and answer
                    if (filteredInput.equals("set question")) {
                        Object[] input = {"Question:", questionField, "Answer:", answerField};

                        int option = JOptionPane.showConfirmDialog(null, input, "Set Question", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            String questionText = Methods.filter(questionField.getText());
                            String answerText = answerField.getText();
                            if (!questionText.isEmpty() && !answerText.isEmpty()) {
                                // If question already exist remove it
                                for (int i = 0; i < questions.size(); i++) {
                                    if (questionText.equals(questions.get(i).getQuestion())) {
                                        questions.remove(i);
                                    }
                                }
                                questions.add(new Question(questionText, answerField.getText()));

                                questionField.setText("");
                                answerField.setText("");
                                writeObject();
                                GUI.printf("Question successfully added");
                            } else {
                                GUI.printf("Invalid input");
                            }
                        } else {
                            questionField.setText("");
                            answerField.setText("");
                            GUI.printf("Cancelled");
                        }
                        GUI.resetInputField();
                        return;
                    }
                    // Checks if input is a set questions, reply with set answer if it is
                    for (int i = 0; i < questions.size(); i++) {
                        if (filteredInput.equals(questions.get(i).getQuestion())) {
                            String[] y = questions.get(i).getAnswer();
                            if (y.length > 1) {
                                GUI.printf(y[rngReply.getNum(y.length)]);
                            } else {
                                GUI.printf(y[0]);
                            }
                            GUI.resetInputField();
                            return;
                        }
                    }

                    // Commands that require String manipulation
                    if (filteredInput.contains("set alarm")) {
                        clock.setAlarmTime(input.substring(10));
                        GUI.printf("Alarm set at " + clock.getAlarmTime());
                    } else if (filteredInput.contains("encode")) {
                        GUI.printf(Methods.Decimal2Bin(input.substring(7)));
                    } else if (filteredInput.contains("decode")) {
                        String v1 = input.substring(7, input.length() - 2);
                        String v2 = input.substring(input.length() - 1);
                        GUI.printf(v1 + " converted to base 10 is " + Methods.decode(v1, v2));
                    } else if (filteredInput.contains("remove question")) {
                        for (int i = 0; i < questions.size(); i++) {
                            if (filteredInput.substring(16).equals(questions.get(i).getQuestion())) {
                                questions.remove(i);
                                GUI.printf("Question successfully removed");
                            }
                        }
                        // Normal replies
                    } else if (Methods.checkContains(filteredInput, "hello", "hi", "sup", "hey", "annyeong", "konichiwa")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf(greetings.get(rngReply.getNum(greetings.size())));
                    } else if (Methods.checkContains(filteredInput, "quote")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        GUI.printf(Methods.getQuote());
                        GUI.settypingStatus("");
                    } else if (Methods.checkContains(filteredInput, "bye", "see you", "zai jian")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf(goodbye.get(rngReply.getNum(goodbye.size())));
                    } else if (Methods.checkContains(filteredInput, "do you like")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf("I don\'t really have a preference.");
                    } else if (Methods.checkContains(filteredInput, "who are you", "who you")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf("I'm a chitty chatty little bot.");
                    } else if (Methods.checkContains(filteredInput, "what is your name", "how do i address you")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf("I am just called chatbot cause my creator have no creativity :(");
                    } else if (Methods.checkContains(filteredInput, "what do you do", "what can you do")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf("I can do quite a few things for example playing music. You can see more by typing \"help\"");
                    } else if (Methods.checkContains(filteredInput, "are you real")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf("I am more real than most people.");
                    } else if (Methods.checkContains(filteredInput, "how are you", "how is it going")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf("I'm doing quite okay");
                    } else if (Methods.checkContains(filteredInput, "love you", "muacks", "xoxo")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf("Aww That's nice.");
                    } else if (Methods.checkContains(filteredInput, "how are you created", "what are you written in")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf("I am created in the Programming language called Java.");
                    } else if (Methods.checkContains(filteredInput, "sorry", "apologise")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf("" + sorry.get(rngReply.getNum(sorry.size())));
                    } else if (Methods.checkContains(filteredInput, "thanks", "xie xie")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf("No problem!\n");
                    } else if (Methods.checkContains(filteredInput, "joke", "cheer me up", "need motivation", "bored")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(501) + 500);
                        GUI.settypingStatus("");
                        GUI.printf("" + jokes.get(rngReply.getNum(jokes.size())));
                    } else if (Methods.checkContains(filteredInput, "ok", "yes", "no", "right")) {
                        GUI.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        GUI.settypingStatus("");
                        GUI.printf("Okay");
                    } else {
                        commands();
                    }

                    GUI.resetInputField();
                } catch (InterruptedException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
    }

    private void commands() {
        // Help msg
        String help = "ChatBot:    Commands avaliable\n"
                + "----------------------------------------------------\n"
                + "help\t\t\t\t\t\t- Displays this message\n"
                + "clear\t\t\t\t\t\t- Clears the screen\n"
                + "encode\t\t\t\t\t- Converts decimal number to binary/hex\n"
                + "decode <bin/hex> <base>\t\t- Converts a binary/hex to decimal\n"
                + "set question\t\t\t\t- Set/update a question\n"
                + "list questions\t\t\t\t- Lists all the set questions\n"
                + "uv\t\t\t\t\t\t- Shows the UV readings of the day\n"
                + "quote\t\t\t\t\t- Shows a random quote\n"
                + "coinflip\t\t\t\t\t- Flips a coin\n"
                + "joke\t\t\t\t\t\t- Tells a joke\n"
                + "mc dir\t\t\t\t\t- Choose your music directory\n"
                + "mc stop\t\t\t\t\t- Stops the music\n"
                + "mc pause\t\t\t\t\t- Pause the music\n"
                + "mc resume\t\t\t\t\t- Resume the music\n"
                + "mc next\t\t\t\t\t- Plays the next song\n"
                + "mc prev\t\t\t\t\t- Plays the previous song\n"
                + "mc change dir\t\t\t\t- Change music dir\n"
                + "alarm\t\t\t\t\t\t- Displays any alarm set\n"
                + "set alarm 00:00:00 AM/PM\t\t- Set at entered time\n"
                + "dismiss alarm\t\t\t\t- Dismiss any alarm set\n"
                + "exit\t\t\t\t\t\t- Exits the program\n";

        try {
            // Send replies
            switch (filteredInput) {
                // Commands
                case "exit":
                    player.stop();
                    System.exit(0);
                    break;
                case "clear":
                    GUI.clearChatArea();
                    break;
                case "help":
                    GUI.print(help);
                    break;
                case "coinflip":
                    GUI.printf(Methods.coinFlip());
                    break;
                case "list questions":
                    GUI.print("List of User added Questions\n----------------------------------------------------");
                    for (Question q : questions) {
                        GUI.print(q.getQuestion());
                    }
                    break;
                case "mc resume":
                    player.resume();
                    GUI.printf("Music resumed");
                    break;
                case "mc stop":
                    player.stop();
                    player.setStopped(true);
                    GUI.printf("Music stopped");
                    break;
                case "mc pause":
                    player.pause();
                    GUI.printf("Music paused");
                    break;
                case "mc next":
                    player.stop();
                    player.next();
                    GUI.printf("Playing next song");
                    break;
                case "mc prev":
                    if (player.getSongNo() > 0) {
                        player.stop();
                        player.prev();
                        GUI.printf("Playing previous song");
                    } else {
                        GUI.printf("No previous song");
                    }
                    break;
                case "mc dir":
                    try {
                        player.chooseDir();
                    } catch (IndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(null, "No playable files", "Error", 0);
                    } catch (NullPointerException ex) {
                        JOptionPane.showMessageDialog(null, "No such directory", "Error", 0);
                    }
                    GUI.printf("Music directory choosen " + player.getFolder());
                    break;
                case "uv":
                    GUI.settypingStatus("Getting data...");
                    GUI.print(Methods.uvLevels());
                    GUI.settypingStatus("");
                    break;
                case "alarm":
                    if (!clock.getAlarmTime().equals("")) {
                        GUI.printf("Alarm set at " + clock.getAlarmTime());
                    } else {
                        GUI.printf("No alarm set");
                    }
                    break;
                case "dismiss alarm":
                    if (clock.getAlarmTime().equals("")) {
                        GUI.printf("No alarms to dismiss");
                    } else {
                        GUI.printf("Alarm dismissed");
                    }
                    clock.stopAlarm();
                    clock.setAlarmTime("");
                    break;
                case "mc change dir":
                    player.changeDir();
                    GUI.printf("Music directory changed");
                    break;
                default:
                    GUI.printf(defaultReply.get(rngReply.getNum(defaultReply.size())));
                    break;
            } // switch
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String writeObject() {
        try {
            FileOutputStream fileOut = new FileOutputStream(Paths.get(".").toAbsolutePath().normalize().toString() + "/questions.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(questions);
            out.close();
            fileOut.close();
            return "Write successful";
        } catch (IOException i) {
            return "IOException Error";
        }
    }

}
