package ch9k.eventpool;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ch9k.eventpool.EventTest.class,
    ch9k.eventpool.EventPoolTest.class,
    ch9k.eventpool.NetworkEventTest.class,
    ch9k.eventpool.EventFilterTest.class,
    ch9k.eventpool.WarningMessageEventTest.class
})
public class EventpoolSuite {}
