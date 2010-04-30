package ch9k.chat.event;

import ch9k.chat.Contact;
import ch9k.core.ChatApplication;
import java.net.UnknownHostException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContactEventFilterTest {
    @Before
    public void setUp() {
        ChatApplication.getInstance().performTestLogin();
    }

    /**
     * Test of accept method, of class ContactStatusEventFilter.
     */
    @Test
    public void testAccept() throws UnknownHostException {
        Contact contact1 = new Contact("JPanneel", null);
        Contact contact2 = new Contact("Javache", null);

        ContactEventFilter contactStatusEventFilter = new ContactEventFilter(contact1);
        ContactEvent contactStatusEvent1 = new ContactOnlineEvent(contact1);
        ContactEvent contactStatusEvent2 = new ContactOnlineEvent(contact2);

        assertTrue(contactStatusEventFilter.accept(contactStatusEvent1));
        assertFalse(contactStatusEventFilter.accept(contactStatusEvent2));
    }

}
