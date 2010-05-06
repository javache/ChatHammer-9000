package ch9k.chat;

import java.net.UnknownHostException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConversationSubjectTest {
    private ConversationSubject conversationSubject;
    private String[] subjects;

    @Before
    public void setUp() throws UnknownHostException {
        subjects = new String[] { "subject-a", "subject-b" };
        conversationSubject = new ConversationSubject(subjects);
    }

    /**
     * Test of getSubjects method, of class ConversationSubject.
     */
    @Test
    public void testGetSubjects() {
        assertArrayEquals(subjects, conversationSubject.getSubjects());
    }
}
