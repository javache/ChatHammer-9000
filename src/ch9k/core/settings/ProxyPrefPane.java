package ch9k.core.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author toon
 */
public class ProxyPrefPane extends JPanel {

    private Settings settings;
    private JTextField proxy;

    public ProxyPrefPane() {
        settings = new Settings();
        proxy = new JTextField();
        proxy.setColumns(30);
        proxy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                settings.set("proxy", proxy.getText());
            }
        });
        add(new JLabel("proxy: "));
        add(proxy);

        settings.addSettingsListener(new SettingsChangeListener() {

            @Override
            public void settingsChanged(SettingsChangeEvent event) {
                if(event.getKey().equals("proxy")) {
                    System.setProperty("http.proxyHost", event.getValue());
                }
            }
        });
    }

}
