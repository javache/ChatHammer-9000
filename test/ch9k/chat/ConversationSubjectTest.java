package ch9k.chat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jens Panneel
 */
public class ConversationSubjectTest {
    private ConversationSubject conversationSubject;
    private Conversation conversation;
    private String[] subjects;

    public ConversationSubjectTest() {
    }

    @Before
    public void setUp() {
        conversation = new Conversation(new Contact("JPanneel", null, true), true);
        subjects = new String[2];
        subjects[0] = "Hey";
        subjects[1] = "Hoi!";
        conversationSubject = new ConversationSubject(conversation, subjects);
    }

    @After
    public void tearDown() {
        conversationSubject = null;
        conversation = null;
        subjects = null;
    }

    /**
     * Test of getSubjects method, of class ConversationSubject.
     */
    @Test
    public void testGetSubjects() {
        System.out.println("getSubjects");
        String[] subjects2 = conversationSubject.getSubjects();
        assertEquals(subjects2.length, subjects.length);
        assertEquals(subjects2[0], subjects[0]);
        assertEquals(subjects2[1], subjects[1]);
    }

     /**
     * Test of getConversation method, of class ConversationSubject.
     */
    @Test
    public void testGetConversation() {
        System.out.println("getConversation");
        assertEquals(conversationSubject.getConversation(), conversation);
    }
}