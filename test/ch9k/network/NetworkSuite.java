package ch9k.network;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ch9k.network.ConnectionManagerTest.class,
        ch9k.network.ConnectionTest.class,
        ch9k.network.DataSocketTest.class,
        ch9k.network.EventWriterTest.class,
        ch9k.network.RealLifeTest.class
})
public class NetworkSuite {}
