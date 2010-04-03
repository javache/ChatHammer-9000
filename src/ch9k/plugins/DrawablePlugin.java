package ch9k.plugins;

/**
 * Represents a plugin that is drawable on a swing panel.
 * @author Jasper Van der Jeugt
 */
public interface DrawablePlugin extends Plugin
{
    /**
     * Enable this plugin for a specific conversation. This method is also
     * used for passing the JPanel on which the plugin should be drawn.
     * @param conversation Conversation to enable this plugin for.
     * @param panel Panel on which the plugin should be drawn.
     */
    void enable(Conversation conversation, JPanel panel);
}
