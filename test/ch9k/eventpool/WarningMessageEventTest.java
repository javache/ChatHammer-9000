package ch9k.eventpool;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of the WarningMessageEventTest class.
 */
public class WarningMessageEventTest {
    /**
     * Test of the constructor.
     */
    @Test
    public void testWarningMessageEvent() {
        WarningEvent event = new WarningEvent(this,
                "Too much awesomeness detected.");
        assertSame(this, event.getSource());
        assertEquals("Too much awesomeness detected.",
                event.getMessage());
    }
}
