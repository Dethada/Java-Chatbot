/*
 * Generates random numbers without repeating the previous number
 */
package chatbot;

/**
 *
 * @author David
 */
public class RNG extends java.util.Random {

    private int prev = 0, current = 0;

    public int getNum(int bound) {
        while (prev == current) {
            current = nextInt(bound);
        }
        prev = current;
        return current;
    }
}
