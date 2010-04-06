package ch9k.chat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Jens Panneel
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ch9k.chat.ConversationSubjectTest.class,ch9k.chat.ContactListTest.class,ch9k.chat.ConversationTest.class,ch9k.chat.ContactTest.class,ch9k.chat.ConversationManagerTest.class})
public class ChatSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}