/*
 * Gets user input and replies them
 */
package chatbot;

import static chatbot.ChatBot.clock;
import static chatbot.ChatBot.mc;
import java.io.IOException;
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
    private final Questions questions;
    private final ArrayList<String> greetings;
    private final ArrayList<String> jokes;
    private final ArrayList<String> goodbye;
    private final ArrayList<String> defaultReply;
    private final ArrayList<String> sorry;
    // RNG
    private final RNG rngReply;
    private final RNG rngTime;

    public Replies() {
        this.questions = new Questions("questions.ser");
        this.rngReply = new RNG();
        this.rngTime = new RNG();

        // Read stored replies from file
        greetings = Methods.readFile(System.getProperty("user.dir") + "\\replies\\greetings.txt");
        jokes = Methods.readFile(System.getProperty("user.dir") + "\\replies\\jokes.txt");
        goodbye = Methods.readFile(System.getProperty("user.dir") + "\\replies\\goodbye.txt");
        defaultReply = Methods.readFile(System.getProperty("user.dir") + "\\replies\\default.txt");
        sorry = Methods.readFile(System.getProperty("user.dir") + "\\replies\\sorry.txt");
    }

    public void getReply() {
        new Thread("Reply") {
            @Override
            public void run() {
                try {
                    // Get user input and filter it, if input is empty do nothing.
                    input = ChatBot.getInput();
                    if (input.equals("")) {
                        return;
                    }
                    filteredInput = Methods.filter(input);

                    // Display input
                    ChatBot.userPrint(input);

                    // Allow user to add questions and answer
                    if (filteredInput.equals("set question")) {
                        ChatBot.printf(questions.setQuestion());
                        ChatBot.resetInputField();
                        return;
                    }
                    // Checks if input is a set questions, reply with set answer if it is
                    if (!questions.answer(filteredInput).isEmpty()) {
                        ChatBot.printf(questions.answer(filteredInput));
                        ChatBot.resetInputField();
                        return;
                    }

                    // Commands that require String manipulation
                    if (filteredInput.contains("set alarm")) {
                        clock.setAlarmTime(input.substring(10));
                        ChatBot.printf("Alarm set at " + clock.getAlarmTime());
                    } else if (filteredInput.contains("encode")) {
                        ChatBot.printf(Methods.Decimal2Bin(input.substring(7)));
                    } else if (filteredInput.contains("decode")) {
                        String v1 = input.substring(7, input.length() - 2);
                        String v2 = input.substring(input.length() - 1);
                        ChatBot.printf(v1 + " converted to base 10 is " + Methods.decode(v1, v2));
                    } else if (filteredInput.contains("remove question")) {
                        ChatBot.printf(questions.rmQuestion(filteredInput.substring(16)));
                        // Normal replies
                    } else if (Methods.checkContains(filteredInput, "hello", "hi", "sup", "hey", "annyeong", "konichiwa")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf(greetings.get(rngReply.getNum(greetings.size())));
                    } else if (Methods.checkContains(filteredInput, "quote")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        ChatBot.printf(Methods.getQuote());
                        ChatBot.settypingStatus("");
                    } else if (Methods.checkContains(filteredInput, "bye", "see you", "zai jian")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf(goodbye.get(rngReply.getNum(goodbye.size())));
                    } else if (Methods.checkContains(filteredInput, "do you like")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf("I don\'t really have a preference.");
                    } else if (Methods.checkContains(filteredInput, "who are you", "who you")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf("I'm a chitty chatty little bot.");
                    } else if (Methods.checkContains(filteredInput, "what is your name", "how do i address you")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf("I am just called chatbot cause my creator have no creativity :(");
                    } else if (Methods.checkContains(filteredInput, "what do you do", "what can you do")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf("I can do quite a few things for example playing music. You can see more by typing \"help\"");
                    } else if (Methods.checkContains(filteredInput, "ok", "yes", "no", "right")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf("Okay");
                    } else if (Methods.checkContains(filteredInput, "are you real")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf("I am more real than most people.");
                    } else if (Methods.checkContains(filteredInput, "how are you", "how is it going")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf("I'm doing quite okay");
                    } else if (Methods.checkContains(filteredInput, "love you", "muacks", "xoxo")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf("Aww That's nice.");
                    } else if (Methods.checkContains(filteredInput, "how are you created", "what are you written in")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf("I am created in the Programming language called Java.");
                    } else if (Methods.checkContains(filteredInput, "sorry", "apologise")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf("" + sorry.get(rngReply.getNum(sorry.size())));
                    } else if (Methods.checkContains(filteredInput, "thanks", "xie xie")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(251) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf("No problem!\n");
                    } else if (Methods.checkContains(filteredInput, "joke", "cheer me up", "need motivation", "bored")) {
                        ChatBot.settypingStatus("Chatbot is typing...");
                        Thread.sleep(rngTime.getNum(501) + 500);
                        ChatBot.settypingStatus("");
                        ChatBot.printf("" + jokes.get(rngReply.getNum(jokes.size())));
                    } else {
                        commands();
                    }

                    ChatBot.resetInputField();
                } catch (InterruptedException ex) {
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
                    mc.Stop();
                    System.exit(0);
                    break;
                case "clear":
                    ChatBot.clearChatArea();
                    break;
                case "help":
                    ChatBot.print(help);
                    break;
                case "coinflip":
                    ChatBot.printf(Methods.coinFlip());
                    break;
                case "list questions":
                    ChatBot.print("List of User added Questions\n----------------------------------------------------\n");
                    ChatBot.print(questions.questions());
                    break;
                case "mc resume":
                    mc.Resume();
                    ChatBot.printf("Music resumed");
                    break;
                case "mc stop":
                    mc.Stop();
                    mc.stopped = true;
                    ChatBot.printf("Music stopped");
                    break;
                case "mc pause":
                    mc.Pause();
                    ChatBot.printf("Music paused");
                    break;
                case "mc next":
                    mc.Stop();
                    mc.next();
                    ChatBot.printf("Playing next song");
                    break;
                case "mc prev":
                    if (mc.songNo > 0) {
                        mc.Stop();
                        mc.prev();
                        ChatBot.printf("Playing previous song");
                    } else {
                        ChatBot.printf("No previous song");
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
                    ChatBot.printf("Music directory choosen " + mc.folder);
                    break;
                case "uv":
                    ChatBot.settypingStatus("Getting data...");
                    ChatBot.print(Methods.uvLevels());
                    ChatBot.settypingStatus("");
                    break;
                case "alarm":
                    if (!clock.getAlarmTime().equals("")) {
                        ChatBot.printf("Alarm set at " + clock.getAlarmTime());
                    } else {
                        ChatBot.printf("No alarm set");
                    }
                    break;
                case "dismiss alarm":
                    if (clock.getAlarmTime().equals("")) {
                        ChatBot.printf("No alarms to dismiss");
                    } else {
                        ChatBot.printf("Alarm dismissed");
                    }
                    clock.stopAlarm();
                    clock.setAlarmTime("");
                    break;
                case "mc change dir":
                    mc.changeDir();
                    ChatBot.printf("Music directory changed");
                    break;
                default:
                    ChatBot.printf(defaultReply.get(rngReply.getNum(defaultReply.size())));
                    break;
            } // switch
        } catch (IOException ex) {
            Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
