package ch9k.chat.events;

import ch9k.chat.Contact;
import ch9k.core.ChatApplication;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jens Panneel
 */
public class ContactStatusEventTest {
    @Before
    public void setUp() {
        ChatApplication.getInstance().performTestLogin();
    }
    
    /**
     * Test of getNewStatus method, of class ContactStatusChangeEvent.
     */
    @Test
    public void testGetNewStatus() {
        Contact contact = new Contact("JPanneel", null, true);
        String status = "Ben naar de WC";
        ContactStatusEvent contactStatusChangeEvent = new ContactStatusEvent(contact, status);
        assertEquals(status, contactStatusChangeEvent.getNewStatus());
    }
}
