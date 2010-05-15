package ch9k.plugins.liteanalyzer;

import ch9k.core.settings.Settings;
import ch9k.plugins.TextAnalyzer;
import ch9k.plugins.TextAnalyzerPreferencePane;
import ch9k.plugins.TextAnalyzerTester;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class LiteTextAnalyzerTest {    
    /**
     * Test of getSubject method, of class LiteTextAnalyzer.
     */
    @Test
    public void testGetSubject() {
        Settings settings = new Settings();
        settings.setInt(TextAnalyzerPreferencePane.MAX_SUBJECTS, 3);
        settings.setInt(TextAnalyzerPreferencePane.MAX_MESSAGES, 5);
        TextAnalyzer analyzer = new LiteTextAnalyzer(null, null, settings);
        String[] messages = {
            "Mushroom Mushroom Mushroom",
            "Badger Badger",
            "The a The a The a The a"
        };
        List<String> subjects = analyzer.getSubjects(messages);
        assertEquals("mushroom", subjects.get(0));
        assertEquals("badger", subjects.get(1));
        assertEquals(2, subjects.size());
    }

    /**
     * Test of getFrequencyMap method, of class LiteTextAnalyzer.
     */
    @Test
    public void testGetFrequencyMap() {
        TextAnalyzerTester tester = new TextAnalyzerTester();
        tester.testGetFrequencyMap(
                new LiteTextAnalyzer(null, null, new Settings()));
    }
}
