package ch9k.core;

import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Window Manager wants to know about all windows.
 * When creating a new window do windowmanager.registerWindow(window) before using window.setVisible(true)
 * also always set a name on the new window.
 */
public class WindowManager extends Model implements WindowListener {
    private List<Window> openedWindows;

    public WindowManager() {
        openedWindows = new ArrayList<Window>();
    }

    public void registerWindow(Window w) {
        w.addWindowListener(this);
    }

    public List<Window> getOpenedWindows() {
        return openedWindows;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        Window w = e.getWindow();
        synchronized(this) {
            if(w.isShowing() && !openedWindows.contains(w)) {
                openedWindows.add(w);
                fireStateChanged();
            }
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        Window w = e.getWindow();
        synchronized(this) {
            if(!w.isShowing() && openedWindows.remove(w)) {
               fireStateChanged();
            }
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {
        windowOpened(e);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        windowClosed(e);
    }

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {}
}
