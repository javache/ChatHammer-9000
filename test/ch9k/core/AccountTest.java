package ch9k.core;

import ch9k.configuration.PersistentDataObject;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    public void testPersist() {
        Account account = new Account("Toon", "mac4Life");
        PersistentDataObject pdo = account.persist();
        Account loadedAccount = new Account(pdo);

        assertEquals(account, loadedAccount);
    }

    @Test
    public void testGetInetAddresses() throws UnknownHostException{
        Account account = new Account("javache", "not-my-password");
        InetAddress[] addresses = account.getInetAddresses();

        // can't really verify this
        assertEquals(2, addresses.length);
        assertEquals(InetAddress.getLocalHost(), addresses[0]);
    }
}
