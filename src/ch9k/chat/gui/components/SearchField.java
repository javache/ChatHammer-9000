package ch9k.chat.gui.components;

import ch9k.chat.FilteredContactList;
import ch9k.chat.SearchContactFilter;
import ch9k.core.I18n;
import java.awt.SystemColor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Textfield that allows the user to update his status
 * @author Pieter De Baets
 */
public class SearchField extends DefaultValueField implements KeyListener {
    private SearchContactFilter filter;
    private FilteredContactList contactList;

    public SearchField(FilteredContactList contactList) {
        super(I18n.get("ch9k.chat", "search"), SystemColor.textText);
        this.contactList = contactList;

        filter = new SearchContactFilter();
        contactList.addFilter(filter);

        addKeyListener(this);
    }
    
    public void update() {
        String searchTerms = "";
        if(isTextChanged()) {
            searchTerms = getText();
        }

        filter.searchFor(searchTerms);
        contactList.updateMapping();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        update();
    }
}
