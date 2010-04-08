package ch9k.eventpool;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Pieter De Baets
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ch9k.eventpool.EventTest.class,
        ch9k.eventpool.EventPoolTest.class,
        ch9k.eventpool.NetworkEventTest.class
})
public class EventpoolSuite {}
