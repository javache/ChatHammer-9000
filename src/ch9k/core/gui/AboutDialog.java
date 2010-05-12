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

        setSize(new Dimension(300, 300));
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

        JLabel text = new JLabel ("<html><p>ChatHammer 9000 is a project created by Corijn Bruno, De Baets Pieter, Panneel Jens, Van der Jeugt Jasper and Willems Toon.</p>" +
                "<p>If you are very impressed with our work, and want to hire our team, feel free to drop by the Zeus WPI Headquarters or contact us at so2project@zeus.ugent.be. <br />" +
                "In case you happen to find any bugs (rather unlikely) you may also contact us, and we might try to fix them in future releases! (also rather unlikely)");

        GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup()
                .addComponent(image)
                .addComponent(text)
                )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(10)
                .addComponent(image)
                .addGap(20)
                .addComponent(text)
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
