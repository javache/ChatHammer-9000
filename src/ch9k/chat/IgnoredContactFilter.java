package ch9k.chat;

/**
 *
 * @author toon
 */
public class IgnoredContactFilter implements ContactFilter {
    @Override
    public boolean shouldDisplay(Contact contact) {
        return !contact.isIgnored();
    }
}
