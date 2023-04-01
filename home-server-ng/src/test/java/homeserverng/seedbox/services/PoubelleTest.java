package homeserverng.seedbox.services;

import org.junit.Test;

public class PoubelleTest {

    @Test
    public void test() {
        String test = "123;45;56;254;111";

        int[] finalTemplate = new int[8];
        int currentLine = 0;
        int lastIndex = 0;
        while(lastIndex != -1) {
            int nextIndex = test.indexOf(';', lastIndex);
            String rawValue;
            if (nextIndex != -1) {
                rawValue = test.substring(lastIndex, nextIndex);
            } else {
                rawValue = test.substring(lastIndex);
            }
            finalTemplate[currentLine] =  Integer.parseInt(rawValue);
            currentLine++;
            lastIndex = (nextIndex == -1) ? -1 : nextIndex + 1;
        }
        System.out.println(finalTemplate);
    }
}
