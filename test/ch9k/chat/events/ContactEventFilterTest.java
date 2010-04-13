package ch9k.chat.events;

import ch9k.chat.Contact;
import java.net.UnknownHostException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jens Panneel
 */
public class ContactEventFilterTest {

    public ContactEventFilterTest() {
    }

    /**
     * Test of accept method, of class ContactStatusEventFilter.
     */
    @Test
    public void testAccept() throws UnknownHostException {
        Contact contact1 = new Contact("JPanneel", null, true);
        Contact contact2 = new Contact("Javache", null, true);

        ContactEventFilter contactStatusEventFilter = new ContactEventFilter(contact1);
        ContactEvent contactStatusEvent1 = new ContactOnlineEvent(contact1);
        ContactEvent contactStatusEvent2 = new ContactOnlineEvent(contact2);

        assertTrue(contactStatusEventFilter.accept(contactStatusEvent1));
        assertFalse(contactStatusEventFilter.accept(contactStatusEvent2));
    }

}