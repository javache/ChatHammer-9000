package ch9k.chat;

/**
 * Filters contact by a given searchterm
 * @author Pieter De Baets
 */
public class SearchContactFilter implements ContactFilter {
    private String[] terms = {};

    public void searchFor(String searchTerms) {
        terms = searchTerms.split(" ");
    }

    @Override
    public boolean shouldDisplay(Contact contact) {
        boolean shouldDisplay = true;
        String username = contact.getUsername().toLowerCase();

        for(String term : terms) {
            if(!username.contains(term.toLowerCase())) {
                shouldDisplay = false;
            }
        }
        return shouldDisplay;
    }
}
