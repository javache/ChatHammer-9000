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
import javax.swing.JDialog;
import javax.swing.JFrame;
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
