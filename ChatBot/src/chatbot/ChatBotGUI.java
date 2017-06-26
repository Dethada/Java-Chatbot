/*
 * GUI for the chatbot
 */
package chatbot;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author David
 */
public class ChatBotGUI {

    JTextArea textArea;
    JTextField textField;
    JFrame frame;

    public static void main(String[] args) {
        ChatBotGUI gui = new ChatBotGUI();
        gui.go();
    }

    public void go() {
        String[] start = {"Hi nice to meet you I am the first generation chat bot."};

        // Create frame
        frame = new JFrame();

        // Set font
        Font font = new Font("Verdana", Font.PLAIN, 30);

        // Create panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.gray);
        frame.getContentPane().add(panel);

        // Create label
        JLabel label = new JLabel("You:");
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setFont(font);
        panel.add(label);

        // Create textfield
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(1000, 70));
        textField.setFont(font);
        panel.add(textField);

        // Send button
        JButton sendButton = new JButton("Send");
        sendButton.setToolTipText("Click to send");
        sendButton.setPreferredSize(new Dimension(80, 60));
        panel.add(sendButton);

        // Clear button
        JButton clearButton = new JButton("Clear");
        clearButton.setToolTipText("Click to clear text on screen");
        clearButton.setPreferredSize(new Dimension(80, 60));
        panel.add(clearButton);

        // Enter to send
        frame.getRootPane().setDefaultButton(sendButton);

        // Action listener for send button
        sendButton.addActionListener(new MyStartListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatbot();
                textField.setText("");
                textField.requestFocus();
            }
        });
        clearButton.addActionListener(new MyStartListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });

        // Create text area
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFont(font);

        // Create scroller
        JScrollPane scroller = new JScrollPane(textArea);
        scroller.setPreferredSize(new Dimension(1200, 1000));
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        DefaultCaret caret = (DefaultCaret) textArea.getCaret(); // Autoscroll
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panel.add(scroller);

        // Set frame stuff
        frame.setSize(new Dimension(1500, 1200));
        frame.setLocationRelativeTo(null);
        frame.setTitle("Chat bot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("E:\\Programming\\Java ChatBot\\ChatBot\\test\\Bot-64.png"));

        textArea.append("Bot: " + start[0] + "\n");
    }

    private class MyStartListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Pass
        }
    }

    public void chatbot() {
        String fileGreetings = System.getProperty("user.dir") + "\\replies\\greetings.txt";
        String fileJokes = System.getProperty("user.dir") + "\\replies\\jokes.txt";

        // For random numbers
        Random randomGenerator = new Random();
        DateTimeFormatter date = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        DateTimeFormatter time = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        LocalDateTime currentDateTime = LocalDateTime.now();
        Methods methods = new Methods();
        ArrayList<String> greetings = new ArrayList<>();
        ArrayList<String> jokes = new ArrayList<>();

        // Replies
        String help = "Bot:        Commands avaliable\n"
                + "--------------------------------------------\n"
                + "help	  - displays this message\n"
                + "clear	  - clears the screen\n"
                + "date time\t  - shows the current date and time\n"
                + "date 	  - shows the current date\n"
                + "time	  - shows the current time\n"
                + "encode	  - converts a decimal number to binary/hexadecimal\n"
                + "decode	  - converts a binary/hexadecimal number to decimal\n"
                + "coin flip\t  - flips a coin\n"
                + "joke\t  - tells a joke\n"
                + "exit	  - closes the program\n";
        String[] defaultReply = {"What chu say boi", "Talk shit get hit", "I don't understand", "Do Rei Me Fa So Fucking Done with you.",
            "Are you speaking english?"};
        String[] goodbye = {"Okay bye~", "See you again", "Goodbye"};

        // Read replies from file for greetings
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileGreetings)));
            String line;

            while ((line = br.readLine()) != null) {
                greetings.add(line);
            }
            br.close();

        } catch (IOException e) {
            System.out.println("ERROR: unable to read file " + fileGreetings);
        }

        // Read replies from file for jokes
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileJokes)));
            String line;

            while ((line = br.readLine()) != null) {
                jokes.add(line);
            }
            br.close();

        } catch (IOException e) {
            System.out.println("ERROR: unable to read file " + fileGreetings);
        }

        String input = textField.getText();
        String lowerCaseInput = input.toLowerCase();
        textArea.append("You: " + input + "\n");
        switch (lowerCaseInput) {
            // Commands
            case "exit":
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                break;
            case "clear":
                textArea.setText("");
                break;
            case "help":
                textArea.append(help);
                break;
            case "date time":
            case "date and time":
                textArea.append("Bot: Today is " + date.format(currentDateTime) + " and the time now is " + time.format(currentDateTime) + "\n");
                break;
            case "date":
            case "what is the date":
                textArea.append("Bot: Today is " + date.format(currentDateTime) + "\n");
                break;
            case "time":
            case "what is the time now":
                textArea.append("Bot: The time is " + time.format(currentDateTime) + "\n");
                break;
            case "encode":
                methods.Decimal2Bin();
                break;
            case "decode":
                methods.decode();
                break;
            case "coin flip":
                textArea.append("Bot: " + methods.coinFlip() + "\n");
                break;
            // Convos
            case "bye":
                textArea.append("Bot: " + goodbye[randomGenerator.nextInt(3)] + "\n");
                break;
            case "okay":
                textArea.append("Bot: Okay\n");
                break;
            // Questions
            case "what is an issue":
                textArea.append("Bot: An issue is something that can be debated.\n");
                break;
            case "what is a stakeholder":
                textArea.append("Bot: People who are related/affected by the issue.\n");
                break;
            case "what is your name":
                textArea.append("Bot: I dont\'t have a name\n");
                break;
            // Greetings
            case "hey":
            case "sup":
            case "hello":
            case "hi":
                textArea.append("Bot: " + greetings.get(randomGenerator.nextInt(greetings.size())) + "\n");
                break;
            // Nice stuff
            case "sorry":
                textArea.append("Bot: Naww don't be!\n");
                break;
            case "i love you":
                textArea.append("Bot: Aww love you too <3\n");
                break;
            case "how are you doing":
            case "how are you":
                textArea.append("Bot: I'm doing well thankyou\n");
                break;
            case "thanks":
            case "thx":
                textArea.append("Bot: No problem!\n");
                break;
            // Misc
            case "cheer time":
                textArea.append("Bot: ECHO AFTER ME AH\n");
                break;
            case "joke":
            case "tell me a joke":
                textArea.append("Bot: " + jokes.get(randomGenerator.nextInt(jokes.size())) + "\n");
                break;
            default:
                textArea.append("Bot: " + defaultReply[randomGenerator.nextInt(5)] + "\n");
                break;
        }
    }
}
