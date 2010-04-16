package ch9k.eventpool;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of the WarningMessageEventTest class.
 * @author Jasper Van der Jeugt
 */
public class WarningMessageEventTest {
    /**
     * Test of the constructor.
     */
    @Test
    public void testWarningMessageEvent() {
        WarningMessageEvent event = new WarningMessageEvent(this,
                "Too much awesomeness detected.");
        assertSame(this, event.getSource());
        assertEquals("Too much awesomeness detected.",
                event.getWarningMessage());
    }
}
