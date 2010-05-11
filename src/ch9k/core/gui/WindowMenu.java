
package ch9k.core.gui;

import ch9k.core.ChatApplication;
import ch9k.core.I18n;
import ch9k.core.WindowManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Menu to switch between different windows
 */
public class WindowMenu extends JMenu implements ChangeListener{
    private Map<Window,ShowWindowItem> menuMap;
    private JFrame ownerWindow;
    private WindowManager windowManager;

    public WindowMenu(JFrame ownerWindow) {
        super(I18n.get("ch9k.core", "window"));

        this.ownerWindow = ownerWindow;
        menuMap = new HashMap<Window, ShowWindowItem>();

        windowManager =  ChatApplication.getInstance().getWindowManager();
        windowManager.addChangeListener(this);

        List<Window> windows = windowManager.getOpenedWindows();
        for(Window w : windows) {
            addWindow(w);
        }
    }

    private void addWindow(Window w) {
        ShowWindowItem menuItem = new ShowWindowItem(w.getName(), w);
        if(!menuMap.containsKey(w)) {
            menuMap.put(w, menuItem);
            add(menuItem);
        }
    }
    
    private void removeWindow(Window w) {
        ShowWindowItem menuItem = menuMap.remove(w);
        if( menuItem != null) {
            remove(menuItem);
            if(w.equals(ownerWindow)) {
                windowManager.removeChangeListener(this);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        List<Window> windows = windowManager.getOpenedWindows();
        for(Window w : windows) {
            if(!menuMap.containsKey(w)){
                addWindow(w);
            }
        }
        ArrayList<Window> toremove = new ArrayList<Window>();
        for(Window w: menuMap.keySet()){
            if(!windows.contains(w)){
                toremove.add(w);
            }
        }
        for(Window w : toremove) {
            removeWindow(w);
        }
        toremove.clear();
    }

    private class ShowWindowItem extends JCheckBoxMenuItem implements ActionListener {
        private Window window;

        public ShowWindowItem(String title, Window window) {
            super(title);
            this.window = window;

            setSelected(ownerWindow.equals(window));
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            window.toFront();
            setSelected(window.equals(ownerWindow));
        }
    }
}
