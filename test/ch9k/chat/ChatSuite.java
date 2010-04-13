package ch9k.chat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Jens Panneel
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ch9k.chat.events.ConversationEventFilterTest.class
    ,ch9k.chat.events.ConversationEventTest.class
    ,ch9k.chat.events.NewChatMessageEventTest.class
    ,ch9k.chat.events.ContactEventFilterTest.class
    ,ch9k.chat.events.ContactStatusEventTest.class
    ,ch9k.chat.events.NewConversationSubjectEventTest.class
    ,ch9k.chat.ChatMessageTest.class
    ,ch9k.chat.ConversationSubjectTest.class
    ,ch9k.chat.ContactListTest.class
    ,ch9k.chat.ConversationTest.class         
    ,ch9k.chat.ContactTest.class
    ,ch9k.chat.ConversationManagerTest.class
})

public class ChatSuite {}
