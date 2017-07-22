/*
 * Generates random numbers without repeating the previous number
 */
package chatbot;

import java.util.Random;

/**
 *
 * @author David
 */
public class RNG {

    private int prev = 0, current = 0;
    private final Random rng = new Random();

    public int getNum(int x) {
        while (prev == current) {
            current = rng.nextInt(x);
        }
        prev = current;
        return current;
    }
}
