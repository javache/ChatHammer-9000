package ch9k.chat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ch9k.chat.event.ConversationEventFilterTest.class
    ,ch9k.chat.event.ConversationEventTest.class
    ,ch9k.chat.event.NewChatMessageEventTest.class
    ,ch9k.chat.event.ContactEventFilterTest.class
    ,ch9k.chat.event.ContactStatusEventTest.class
    ,ch9k.chat.event.NewConversationSubjectEventTest.class
    ,ch9k.chat.ChatMessageTest.class
    ,ch9k.chat.ConversationSubjectTest.class
    ,ch9k.chat.ContactListTest.class
    ,ch9k.chat.ConversationTest.class         
    ,ch9k.chat.ContactTest.class
    ,ch9k.chat.ConversationManagerTest.class
})

public class ChatSuite {}
