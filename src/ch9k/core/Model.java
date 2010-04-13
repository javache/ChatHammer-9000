/* Model.java
 * ============================================================
 * Copyright (C) 2001-2009 Universiteit Gent
 * 
 * Bijlage bij de cursus 'Programmeren 2'.
 * 
 * Auteur: Kris Coolsaet
 */
package ch9k.core;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Gemeenschappelijke bovenklasse van alle modellen die gebruik maken van
 * luisteraars van het type {@link ChangeListener}.
 */
public class Model {

    /**
     * Lijst van geregistreerde luisteraars.
     */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * Registreer een luisteraar.
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Maak registratie ongedaan.
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }
    /**
     * Uniek gebeurtenisobject met dit model als bron.
     */
    private final ChangeEvent changeEvent = new ChangeEvent(this);

    /**
     * Behandel een ChangeEvent-gebeurtenis die van het model
     * afkomstig is door een nieuwe gebeurtenis aan alle luisteraars
     * door te geven. De nieuwe gebeurtenis heeft dit model als bron.
     */
    protected void fireStateChanged() {
        if (Thread.currentThread().getName().equals("AWT-EventQueue-0")) {
            stateChange();
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    stateChange();
                }
            });
        }
    }


    private void stateChange() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}
