/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch9k.chat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jpanneel
 */
public class ConversationSubjectTest {

    public ConversationSubjectTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getSubjects method, of class ConversationSubject.
     */
    @Test
    public void testGetSubjects() {
        System.out.println("getSubjects");
        ConversationSubject instance = new ConversationSubject();
        String[] expResult = null;
        String[] result = instance.getSubjects();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}