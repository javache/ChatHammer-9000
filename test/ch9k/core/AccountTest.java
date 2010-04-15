package ch9k.core;

import ch9k.configuration.PersistentDataObject;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
    @Test
    public void testPasswordMethods() {
        Account acc = new Account("Toon", "lalala");
        assertNotNull(acc.getPasswordHash());

        Account acc2 = new Account("Toon2", "lalala");
        assertEquals(acc.getPasswordHash(), acc2.getPasswordHash());

        acc.setPassword("not lalala");
        assertNotSame(acc.getPasswordHash(), acc2.getPasswordHash());
    }

    @Test
    public void persistTest(){
        Account acc = new Account("Toon", "mac4Life");
        PersistentDataObject pdo = acc.persist();
        Account acc2 = new Account(pdo);

        assertEquals(acc,acc2);
    }
}
