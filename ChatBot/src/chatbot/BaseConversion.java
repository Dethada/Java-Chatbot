/*
 * The methods for the chatbot are here
 */
package chatbot;

import java.util.Random;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author David_2
 */
public class BaseConversion {

    // Takes a decimal number as arg then returns binary and
    // hex value of the decimal as a string
    public void Decimal2Bin() {
        // Keep window on top
        final JDialog onTop = new JDialog();
        onTop.setAlwaysOnTop(true);

        String value = JOptionPane.showInputDialog(onTop, "Enter a decimal");
        int intValue = Integer.parseInt(value);
        if (intValue == JOptionPane.CANCEL_OPTION || intValue == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }

        // Converts and prints the answer
        JOptionPane.showMessageDialog(onTop, intValue + " in binary: " + Integer.toBinaryString(intValue) + "\n" + intValue
                + " in hexadecimal: " + (Integer.toHexString(intValue)).toUpperCase());
    }

    // Takes binary/hex value and converts to decimal
    public void decode() {
        // Keep window on top
        final JDialog onTop = new JDialog();
        onTop.setAlwaysOnTop(true);

        String value = JOptionPane.showInputDialog(onTop, "Enter a binary/hex number");

        String base = JOptionPane.showInputDialog(onTop, "Enter the base (2 for binary, 16 for hex)");
        int intBase = Integer.parseInt(base);
        if (intBase == JOptionPane.CANCEL_OPTION || intBase == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }

        //Integer.valueOf("AB", 16);
        JOptionPane.showMessageDialog(onTop, Integer.valueOf(value, intBase));
    }

    // Flips a coin
    public String coinFlip() {
        // new Random object
        Random randomGenerator = new Random();

        String[] flip = {"heads", "tails"};
        return (flip[randomGenerator.nextInt(2)]);
    }
}
