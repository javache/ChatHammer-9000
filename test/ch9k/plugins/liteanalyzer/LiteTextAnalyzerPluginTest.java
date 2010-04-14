package ch9k.plugins.liteanalyzer;

import ch9k.plugins.TextAnalyzer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Jasper Van der Jeugt
 */
public class LiteTextAnalyzerPluginTest {    
    /**
     * Test of getSubject method, of class LiteTextAnalyzerPlugin.
     */
    @Test
    public void testGetSubject() {
        TextAnalyzer analyzer = new LiteTextAnalyzerPlugin();
        String[] messages = {"Mushroom Mushroom Mushroom", "Badger Badger"};
        String[] subjects = analyzer.getSubjects(messages);
        assertEquals("mushroom", subjects[0]);
        assertEquals("badger", subjects[1]);
    }
}
