package ch9k.chat.gui;

import java.awt.Dimension;
import ch9k.core.I18n;
import ch9k.core.ChatApplication;
import ch9k.plugins.PluginManager;
import ch9k.chat.Conversation;
import javax.swing.JMenu;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JCheckBoxMenuItem;
import java.util.Map;
import java.util.HashMap;
import java.net.URL;

/**
 * Menu to enable and disable plugins for a specific conversation.
 * @author Jasper Van der Jeugt
 */
public class PluginMenu extends JMenu implements ChangeListener {
    /**
     * The releveant conversation for this menu.
     */
    private Conversation conversation;

    /**
     * Keep the buttons in a map.
     */
    private Map<String, JCheckBoxMenuItem> itemMap;

    /**
     * The plugin manager.
     */
    public PluginManager manager;

    /**
     * Constructor.
     * @param conversation Conversation this menu adheres to.
     */
    public PluginMenu(Conversation conversation) {
        super(I18n.get("ch9k.chat", "plugins"));
        this.conversation = conversation;
        itemMap = new HashMap<String, JCheckBoxMenuItem>();

        // manager = ChatApplication.getInstance().getPluginManager();
        manager = new PluginManager();

        /* Add buttons for the available plugins. */
        buildMenu();

        /* Listen for changes in the manager's state. */
        manager.addChangeListener(this);
    }

    /**
     * Build the actual menu. Calling this method again is equivalent to a
     * "refresh".
     */
    public void buildMenu() {
        /* Remove everything. */
        removeAll();
        itemMap.clear();

        /* Add a button for every available plugin. */
        for(String plugin: manager.getAvailablePlugins()) {
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(prettify(plugin));
            add(item);
            itemMap.put(plugin, item);
        }
    }

    /**
     * Prettify a plugin name.
     * @param plugin Original plugin name.
     * @return Prettier plugin name.
     */
    private String prettify(String plugin) {
        return plugin.substring(plugin.lastIndexOf('.') + 1);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        /* If the manager sends an event, this means the installed plugins list
         * has changed. We thus want to rebuild it. */
        if(manager == event.getSource()) {
            buildMenu();
        }
    }

    /**
     * Quick and dirty testing method.
     * TODO: remove.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("PluginMenu test.");
        
        JMenuBar bar = new JMenuBar();
        PluginMenu menu = new PluginMenu(null);
        bar.add(menu);

        frame.setJMenuBar(bar);

        frame.setSize(new Dimension(400, 300));
        frame.pack();
        frame.setVisible(true);

        try {
            Thread.sleep(5000);
            menu.manager.getPluginInstaller()
                .installPlugin(new URL("http://zeus.ugent.be/~jasper/DummyPlugin.jar"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
