/*
 * testing stage of chatbot
 */


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author David
 */
public class ChatBot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // For random numbers
        Random randomGenerator = new Random();
        DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        DateTimeFormatter date = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        DateTimeFormatter time = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Scanner
        Scanner input = new Scanner(System.in);

        String[] start = {"Hi nice to meet you I am the first generation chat bot."};
        String[] defaultReply = {"What chu say boi", "Talk shit get hit", "I don't understand", "Do Rei Me Fa So Fucking Done with you.",
            "Are you speaking english?"};
        String[] greetings = {"Hello!", "Greetings!", "Nice to meet you!"};
        String[] talkShit = {"Fuck off", "SMD"};
        String[] jokes = {"My sister bet me a hundred dollars I couldn't build a car out of spaghetti.\n     You should\'ve seen her face as I drove pasta!",
            "You."};
        String[] goodbye = {"Okay bye~", "See you again", "Goodbye"};

        System.out.println("Bot: " + start[0]);

        OUTER:
        while (true) {
            System.out.print("You: ");
            String ans = input.nextLine();
            String lowerCaseAns = ans.toLowerCase().replaceAll("[^a-z0-9]+", " ");
            switch (lowerCaseAns) {
                case "bye":
                    System.out.println("Bot: " + goodbye[randomGenerator.nextInt(3)]);
                    break OUTER;
                // Questions
                case "what is an issue":
                    System.out.println("Bot: An issue is something that can be debated.");
                    break;
                case "what is a stakeholder":
                    System.out.println("Bot: People who are related/affected by the issue.");
                    break;
                case "what is your name":
                    System.out.println("Bot: I dont\'t have a name");
                    break;
                // Greetings
                case "hey":
                case "sup":
                case "hello":
                case "hi":
                    System.out.println("Bot: " + greetings[randomGenerator.nextInt(3)]);
                    break;
                // Nice stuff
                case "i love you":
                    System.out.println("Bot: Aww love you too <3");
                    break;
                case "how are you":
                    System.out.println("Bot: I'm doing well thankyou");
                    break;
                case "thanks":
                case "thx":
                    System.out.println("Bot: No problem!");
                    break;
                // talk shit
                case "you suck":
                case "cb":
                case "knn":
                case "fk u":
                case "fuck":
                case "fuck you":
                    System.out.println("Bot: " + talkShit[randomGenerator.nextInt(2)]);
                    break;
                // Misc
                case "date and time":
                    System.out.println("Bot: " + dateTime.format(currentDateTime));
                    break;
                case "what is the date":
                    System.out.println("Bot: " + date.format(currentDateTime));
                    break;
                case "what is the time now":
                    System.out.println("Bot: " + time.format(currentDateTime));
                    break;
                case "something":
                    System.out.println("Bot: It's too late to say something\n     it\'s too late~");
                    break;
                case "cheer time":
                    System.out.println("Bot: ECHO AFTER ME AH");
                    break;
                case "tell me a joke":
                    System.out.println("Bot: " + jokes[randomGenerator.nextInt(2)]);
                    break;
                default:
                    System.out.println("Bot: " + defaultReply[randomGenerator.nextInt(5)]);
                    break;
            }
        }

        System.exit(0);
    }

    public static int randomNumber(int max) {
        Random randomGenerator = new Random();
        int number = randomGenerator.nextInt(max);
        int prevNumber = number;

        while (prevNumber == number) {
            number = randomGenerator.nextInt(max);
        }

        return number;
    }

}
