package ch9k.core.gui;

import ch9k.core.ChatApplication;
import ch9k.core.I18n;
import com.vwp.sound.mod.modplay.ThreadedPlayer;
import com.vwp.sound.mod.modplay.loader.InvalidFormatException;
import com.vwp.sound.mod.modplay.player.PlayerException;
import com.vwp.sound.mod.sound.output.JavaSoundOutput;
import com.vwp.sound.mod.sound.output.SoundDataFormat;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.IOException;
import java.net.URL;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.apache.log4j.Logger;

/**
 *
 * @author Pieter De Baets
 */
public class AboutDialog extends JDialog {
    private ThreadedPlayer player;

    public AboutDialog(Frame parent) {
        super(parent, I18n.get("ch9k.core", "about"), true);

        initComponents();
        initSound();

        setSize(new Dimension(350, 350));
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void dispose() {
        super.dispose();

        if(player != null) {
            player.stop();
            player.close();
        }
    }

    private void initComponents() {
        URL imgURL = ChatApplication.class.getResource("/ch9k/core/nameLogo.png");
        ImageIcon logo = new ImageIcon(imgURL, "ChatHammer 9000");
        JLabel image = new JLabel(logo);

        JLabel text = new JLabel ("<html><center>" +
                "<p>ChatHammer 9000 was created by Bruno Corijn, " +
                "Pieter De Baets, Jens Panneel, Jasper Van der Jeugt and " +
                "Toon Willems.</p>" +
                "<p> </p>" +
                "<p>If you are impressed with our work, and want to hire our team, " +
                "feel free to contact us at so2project@zeus.ugent.be.</p>" +
                "<p> </p>" +
                "<p>In the unlikely event that you happen to find a bug" +
                " you may also contact us, and if we are really bored, " +
                "we might fix it in future release (also rather unlikely).</p>");
        text.setHorizontalAlignment(SwingConstants.CENTER);

        GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGap(15, 15, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(image)
                    .addGap(0, 0, Short.MAX_VALUE)
                )
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(text)
                    .addGap(0, 0, Short.MAX_VALUE)
                )
            )
            .addGap(15, 15, Short.MAX_VALUE)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGap(15, 15, Short.MAX_VALUE)
            .addComponent(image)
            .addGap(20)
            .addComponent(text)
            .addGap(15, 15, Short.MAX_VALUE)
        );
    }

    private void initSound() {
        // play the themesong
        try {
            player = new ThreadedPlayer();
            player.init(new JavaSoundOutput(new SoundDataFormat(16, 44100, 2), 500), true);
            player.load(ChatApplication.class.getResourceAsStream("/ch9k/core/themesong.mod"), "themesong.mod");
            player.start();
        } catch(PlayerException ex) {
            Logger.getLogger(AboutDialog.class).error(ex);
        } catch(InvalidFormatException ex) {
            Logger.getLogger(AboutDialog.class).error(ex);
        } catch(IOException ex) {
            Logger.getLogger(AboutDialog.class).error(ex);
        }
    }
}
