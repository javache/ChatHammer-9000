package ch9k.core;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Window Manager wants to know about all windows.
 * When creating a new window do windowmanager.registerWindow(window) before using window.setVisible(true)
 * also always set a name on the new window.
 */
public class WindowManager extends WindowAdapter {
    private List<Window> openedWindows;
    private List<WindowStateListener> listeners;

    public WindowManager() {
        openedWindows = new ArrayList<Window>();
        listeners = new ArrayList<WindowStateListener>();
    }

    public void registerWindow(Window w) {
        w.addWindowListener(this);
    }

    public List<Window> getOpenedWindows() {
        return openedWindows;
    }

    public void addListener(WindowStateListener wl) {
        listeners.add(wl);
    }

    public void removeListener(WindowStateListener wl) {
        listeners.remove(wl);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        Window w = e.getWindow();
        openedWindows.add(w);
        for(int i = 0; i < listeners.size(); i++) {
            listeners.get(i).windowStateChanged(e);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        Window w = e.getWindow();
        openedWindows.remove(w);
        for(int i = 0; i < listeners.size(); i++) {
            listeners.get(i).windowStateChanged(e);
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {
        Window w = e.getWindow();
        if(w.isVisible()) {
            openedWindows.add(w);
            for(int i = 0; i < listeners.size(); i++) {
                listeners.get(i).windowStateChanged(new WindowEvent(w, WindowEvent.WINDOW_OPENED));
            }
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        Window w = e.getWindow();
        if(!w.isVisible()) {
            openedWindows.remove(w);
            for(int i = 0; i < listeners.size(); i++) {
                listeners.get(i).windowStateChanged(new WindowEvent(w, WindowEvent.WINDOW_CLOSED));
            }
        }
    }

}
