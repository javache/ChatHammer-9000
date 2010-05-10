package be.jaspervdj.wordcloud;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.Queue;
import java.util.LinkedList;
import java.awt.Color;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.EventListener;
import ch9k.chat.event.NewConversationSubjectEvent;
import ch9k.chat.ConversationSubject;

import javax.swing.JFrame;
import java.net.InetAddress;
import ch9k.chat.Contact;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventPool;
import ch9k.chat.event.RequestedPluginContainerEvent;
import ch9k.plugins.flickr.FlickrImageProvider;
import java.awt.Dimension;

/**
 * Panel to show a wordcloud.
 */
public class WordCloudPanel extends JPanel implements EventListener {
    /**
     * Maximum number of words.
     */
    private final static int MAX_WORDS = 50;

    /**
     * The words.
     */
    private Queue<Word> words;

    /**
     * Constructor.
     */
    public WordCloudPanel() {
        super();
        words = new LinkedList<Word>();

        setBackground(Color.BLACK);
    }

    /**
     * Add a word to the cloud.
     * @param word Word to add.
     */
    private void addWord(Word word) {
        if(words.size() >= MAX_WORDS) {
            words.poll();
        }

        words.offer(word);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(Word word: words) {
            drawWord(g, word);
        }
    }

    /**
     * Draw one word.
     * @param graphics Graphical object to use.
     * @param word Word to draw.
     */
    public void drawWord(Graphics graphics, Word word) {
        double width = (double) getWidth(); 
        double height = (double) getHeight(); 

        /* Get a font to draw the word with. */
        Font originalFont = graphics.getFont();
        Font font = originalFont.deriveFont((float) height * 0.03f +
                word.getSize() * (float) height * 0.1f);
        graphics.setFont(font);

        /* Find out the width of the text to center it. */
        int textWidth = graphics.getFontMetrics().stringWidth(word.getText());

        int x = (int) (word.getX() * width - textWidth / 2);
        int y = (int) (word.getY() * height);

        /* Set the correct color. */
        graphics.setColor(word.getColor());

        graphics.drawString(word.getText(), x, y);
    }

    @Override
    public void handleEvent(Event e) {
        if(e instanceof NewConversationSubjectEvent) {
            NewConversationSubjectEvent event = (NewConversationSubjectEvent) e;
            ConversationSubject subject = event.getConversationSubject();
            String[] subjects = subject.getSubjects();
            for(String word: subjects) {
                addWord(new Word(word));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setContentPane(new WordCloudPanel());
        frame.setPreferredSize(new Dimension(300, 400));

        frame.pack();
        frame.setTitle("WordCloud test.");
        frame.setVisible(true);
    }
}
