package ch9k.chat.events;

import ch9k.chat.Contact;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jens Panneel
 */
public class ContactStatusChangeEventTest {

    /**
     * Test of getNewStatus method, of class ContactStatusChangeEvent.
     */
    @Test
    public void testGetNewStatus() {
        Contact contact = new Contact("JPanneel", null, true);
        String status = "Ben naar de WC";
        ContactStatusChangeEvent contactStatusChangeEvent = new ContactStatusChangeEvent(contact, status);
        assertEquals(status, contactStatusChangeEvent.getNewStatus());
    }

}