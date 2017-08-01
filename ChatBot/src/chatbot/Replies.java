/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbot;

import static chatbot.ChatBot.clock;
import static chatbot.ChatBot.doc;
import static chatbot.ChatBot.mc;
import static java.awt.Color.orange;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

/**
 *
 * @author David
 */
public class Replies {

    private final javax.swing.JTextField questionField;
    private final javax.swing.JTextField answerField;

    private HashMap<String, String> questionAnswer;
    private final ArrayList<String> greetings;
    private final ArrayList<String> jokes;
    private final ArrayList<String> goodbye;
    private final ArrayList<String> defaultReply;
    private final ArrayList<String> sorry;
    // RNG
    private final RNG rngReply;
    private final RNG rngTime;

    public Replies() {
        questionField = new javax.swing.JTextField();
        answerField = new javax.swing.JTextField();
        this.rngReply = new RNG();
        this.rngTime = new RNG();


        // Read stored replies from file
        greetings = Methods.readFile(System.getProperty("user.dir") + "\\replies\\greetings.txt");
        jokes = Methods.readFile(System.getProperty("user.dir") + "\\replies\\jokes.txt");
        goodbye = Methods.readFile(System.getProperty("user.dir") + "\\replies\\goodbye.txt");
        defaultReply = Methods.readFile(System.getProperty("user.dir") + "\\replies\\default.txt");
        sorry = Methods.readFile(System.getProperty("user.dir") + "\\replies\\sorry.txt");
    }

    public void inputFunction() {
        new Thread("Reply") {
            @Override
            public void run() {
                String input, filteredInput;
                try {
                    // Get user input and filter it, if input is empty do nothing.
                    input = ChatBot.getInput();
                    if (input.equals("")) {
                        return;
                    }
                    filteredInput = Methods.filter(input);

                    // Display input
                    doc.insertString(doc.getLength(), "You: " + input + "\n", orange);

                    // Allow user to add question and answer
                    if (filteredInput.equals("set question")) {
                        Object[] msg = {"Question:", questionField, "Answer:", answerField};

                        int option = JOptionPane.showConfirmDialog(null, msg, "Set Question", JOptionPane.OK_CANCEL_OPTION);
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
                                    doc.insertString(doc.getLength(), "Chatbot: Question successfully added" + "\n", null);
                                } catch (IOException i) {
                                    doc.insertString(doc.getLength(), "Chatbot: IOException Error" + "\n", null);
                                }
                            } else {
                                doc.insertString(doc.getLength(), "Chatbot: Invalid input" + "\n", null);
                            }
                        } else {
                            doc.insertString(doc.getLength(), "Chatbot: Cancelled" + "\n", null);
                            questionField.setText("");
                            answerField.setText("");
                            ChatBot.resetInputField();
                            return;
                        }
                        ChatBot.resetInputField();
                        return;
                    }
                    // Checks if input is a set question, reply with set answer if it is
                    for (Map.Entry question : questionAnswer.entrySet()) {
                        String key = "" + question.getKey();
                        if (filteredInput.equals(key)) {
                            doc.insertString(doc.getLength(), "Chatbot: " + question.getValue() + "\n", null);
                            ChatBot.resetInputField();
                            return;
                        }
                    }

                    // Commands that require String manipulation
                    if (filteredInput.contains("set alarm")) {
                        clock.setAlarmTime(input.substring(10));
                        doc.insertString(doc.getLength(), "Chatbot: Alarm set at " + clock.getAlarmTime() + "\n", null);
                    } else if (filteredInput.contains("encode")) {
                        String value = input.substring(7);
                        doc.insertString(doc.getLength(), "Chatbot: " + Methods.Decimal2Bin(value) + "\n", null);
                    } else if (filteredInput.contains("decode")) {
                        String v1 = input.substring(7, input.length() - 2);
                        String v2 = input.substring(input.length() - 1);
                        doc.insertString(doc.getLength(), "Chatbot: " + v1 + " converted to base 10 is " + Methods.decode(v1, v2) + "\n", null);
                    } else if (filteredInput.contains("remove question")) {
                        String x = filteredInput.substring(16);
                        boolean valid = false;
                        for (String key : questionAnswer.keySet()) {
                            if (x.equals(key)) {
                                questionAnswer.remove(x);
                                valid = true;
                                try {
                                    FileOutputStream fileOut = new FileOutputStream(Paths.get(".").toAbsolutePath().normalize().toString() + "/questions.ser");
                                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                                    out.writeObject(questionAnswer);
                                    out.close();
                                    fileOut.close();
                                    doc.insertString(doc.getLength(), "Chatbot: Question successfully removed" + "\n", null);
                                } catch (IOException i) {
                                    doc.insertString(doc.getLength(), "Chatbot: IOException Error" + "\n", null);
                                }
                                break;
                            }
                        }
                        if (!valid) {
                            doc.insertString(doc.getLength(), "Chatbot: Error No such question" + "\n", null);
                        }
                        // Normal replies
                    } else if (Methods.checkContains(filteredInput, "hello", "hi", "sup", "hey", "annyeong", "konichiwa")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: " + greetings.get(rngReply.getNum(greetings.size())) + "\n", null);
                    } else if (Methods.checkContains(filteredInput, "quote")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        doc.insertString(doc.getLength(), "Chatbot: " + Methods.getQuote() + "\n", null);
                        ChatBot.settypingStatus("");
                    } else if (Methods.checkContains(filteredInput, "bye", "see you", "zai jian")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: " + goodbye.get(rngReply.getNum(goodbye.size())) + "\n", null);
                    } else if (Methods.checkContains(filteredInput, "do you like")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: I don\'t really have a preference.\n", null);
                    } else if (Methods.checkContains(filteredInput, "who are you", "who you")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: I'm a chitty chatty little bot.\n", null);
                    } else if (Methods.checkContains(filteredInput, "what is your name", "how do i address you")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: I am just called chatbot cause my creator have no creativity :(\n", null);
                    } else if (Methods.checkContains(filteredInput, "what do you do", "what can you do")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: I can do quite a few things for example playing music. You can see more by typing \"help\"\n", null);
                    } else if (Methods.checkContains(filteredInput, "ok", "yes", "no", "right")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: Okay\n", null);
                    } else if (Methods.checkContains(filteredInput, "are you real")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: I am more real than most people.\n", null);
                    } else if (Methods.checkContains(filteredInput, "how are you", "how is it going")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: I'm doing quite okay\n", null);
                    } else if (Methods.checkContains(filteredInput, "love you", "muacks", "xoxo")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: Aww That's nice.\n", null);
                    } else if (Methods.checkContains(filteredInput, "how are you created", "what are you written in")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: I am created in the Programming language called Java.\n", null);
                    } else if (Methods.checkContains(filteredInput, "sorry", "apologise")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: " + sorry.get(rngReply.getNum(sorry.size())) + "\n", null);
                    } else if (Methods.checkContains(filteredInput, "thanks", "xie xie")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: No problem!\n", null);
                    } else if (Methods.checkContains(filteredInput, "joke", "cheer me up", "need motivation", "bored")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(501) + 500);
                        ChatBot.settypingStatus("");
                        doc.insertString(doc.getLength(), "Chatbot: " + jokes.get(rngReply.getNum(jokes.size())) + "\n", null);
                    } else {
                        commands();
                    }

                    ChatBot.resetInputField();
                } catch (BadLocationException | InterruptedException ex) {
                    Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
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
                + "new quote\t\t\t\t\t- Shows a random quote\n"
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
                    mc.Stop();
                    System.exit(0);
                    break;
                case "clear":
                    chatArea.setText("");
                    break;
                case "help":
                    doc.insertString(doc.getLength(), help, null);
                    break;
                case "coinflip":
                    doc.insertString(doc.getLength(), "Chatbot: " + Methods.coinFlip() + "\n", null);
                    break;
                case "list questions":
                    doc.insertString(doc.getLength(), "List of User added Questions\n----------------------------------------------------\n", null);
                    for (String s : questionAnswer.keySet()) {
                        doc.insertString(doc.getLength(), s + "\n", null);
                    }
                    break;
                case "mc resume":
                    mc.Resume();
                    doc.insertString(doc.getLength(), "Chatbot: Music resumed\n", null);
                    break;
                case "mc stop":
                    mc.Stop();
                    mc.stopped = true;
                    doc.insertString(doc.getLength(), "Chatbot: Music stopped\n", null);
                    break;
                case "mc pause":
                    mc.Pause();
                    doc.insertString(doc.getLength(), "Chatbot: Music paused\n", null);
                    break;
                case "mc next":
                    mc.Stop();
                    mc.next();
                    doc.insertString(doc.getLength(), "Chatbot: Playing next song\n", null);
                    break;
                case "mc prev":
                    if (mc.songNo > 0) {
                        mc.Stop();
                        mc.prev();
                        doc.insertString(doc.getLength(), "Chatbot: Playing previous song\n", null);
                    } else {
                        doc.insertString(doc.getLength(), "Chatbot: No previous song\n", null);
                    }
                    break;
                case "mc dir":
                    try {
                        mc.chooseDir();
                    } catch (IndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(null, "No playable files", "Error", 0);
                    } catch (NullPointerException ex) {
                        JOptionPane.showMessageDialog(null, "No such directory", "Error", 0);
                    }
                    doc.insertString(doc.getLength(), "Chatbot: Music directory choosen " + mc.folder + "\n", null);
                    break;
                case "uv":
                    typingStatus.setText("Getting data...");
                    doc.insertString(doc.getLength(), Methods.uvLevels(), null);
                    typingStatus.setText("");
                    break;
                case "alarm":
                    if (!clock.getAlarmTime().equals("")) {
                        doc.insertString(doc.getLength(), "Chatbot: Alarm set at " + clock.getAlarmTime() + "\n", null);
                    } else {
                        doc.insertString(doc.getLength(), "Chatbot: No alarm set\n", null);
                    }
                    break;
                case "dismiss alarm":
                    if (clock.getAlarmTime().equals("")) {
                        doc.insertString(doc.getLength(), "Chatbot: No alarms to dismiss\n", null);
                    } else {
                        doc.insertString(doc.getLength(), "Chatbot: Alarm dismissed\n", null);
                    }
                    clock.stopAlarm();
                    clock.setAlarmTime("");
                    break;
                case "mc change dir":
                    mc.changeDir();
                    doc.insertString(doc.getLength(), "Chatbot: Music directory changed\n", null);
                    break;
                default:
                    doc.insertString(doc.getLength(), "Chatbot: " + defaultReply.get(rngReply.getNum(defaultReply.size())) + "\n", null);
                    break;
            } // switch
        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
