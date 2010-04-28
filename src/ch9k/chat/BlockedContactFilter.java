package ch9k.chat;

/**
 *
 * @author toon
 */
public class BlockedContactFilter implements ContactFilter {
    @Override
    public boolean shouldDisplay(Contact contact) {
        return !contact.isBlocked();
    }
}
