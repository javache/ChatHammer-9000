/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author jpanneel
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ch9k.chat.ChatMessageTest.class,
    ch9k.chat.ConversationSubjectTest.class,
    ch9k.chat.ContactListTest.class,
    ch9k.chat.ConversationTest.class,
    ch9k.chat.ContactTest.class,
    ch9k.chat.ConversationManagerTest.class})

public class ChatSuite {}