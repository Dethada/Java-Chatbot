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

    int prev = 0, current = 0;
    Random rng = new Random();

    public int getNum(int x) {
        while (prev == current) {
            current = rng.nextInt(x);
        }
        prev = current;
        return current;
    }
}
