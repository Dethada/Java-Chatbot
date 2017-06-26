
import java.util.Random;



/**
 *
 * @author David
 */
public class test {

    public static void main(String[] args) {
        Random randomnum = new Random();
        for (int i = 0; i <= 20; i++) {
            System.out.println(randomnum.nextInt(1001) + 500);
        }
    }
}
