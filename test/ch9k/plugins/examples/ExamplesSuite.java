package ch9k.plugins.examples;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Jasper Van der Jeugt
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ch9k.plugins.examples.FlickrImageProvider.class,
    ch9k.plugins.examples.GoogleImageProvider.class
})
public class ExamplesSuite {}
