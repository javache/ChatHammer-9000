package ch9k.plugins;

import java.util.Map;
import static org.junit.Assert.*;

/**
 * Class to help testing of different TextAnalyzer's.
 * @author Jasper Van der Jeugt
 */
public class TextAnalyzerTester {
    /**
     * Test of getFrequencyMap method, of any TextAnalyzer.
     */
    public void testGetFrequencyMap(TextAnalyzer instance) {
        /* Initialize messages. */
        String[] messages = {
            "Time, has told me",
            "You're a rare rare find",
            "A troubled cure",
            "For a troubled mind"
        };

        /* Get the map. */
        Map<String, Integer> frequencyMap = instance.getFrequencyMap(messages);

        /* Do some checking. */
        assertEquals(1, frequencyMap.get("time").intValue());
        assertEquals(2, frequencyMap.get("troubled").intValue());
        assertEquals(2, frequencyMap.get("rare").intValue());
        assertNull(frequencyMap.get("mushroom"));
    }
}
