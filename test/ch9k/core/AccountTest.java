package ch9k.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
    
    @Test
    public void testPasswordMethods() {
        Account acc = new Account("Toon","lalala");
        assertNotNull(acc.getPasswordHash());
        Account acc2 = new Account("Toon2","lalala");
        assertArrayEquals(acc.getPasswordHash(),acc2.getPasswordHash());
        acc.setPassword("not lalala");
        
        boolean shouldRaise = false;
        try {
            assertArrayEquals(acc.getPasswordHash(),acc2.getPasswordHash());
        } catch(AssertionError e) {
            shouldRaise = true;
        }
        
        assertTrue(shouldRaise);
        
    }
    
    
}