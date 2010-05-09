package be.jaspervdj.wordcloud;

import java.awt.Color;
import java.util.Random;

/**
 * Word for a wordcloud.
 */
public class Word {
    /**
     * Random number generator.
     */
    private final static Random random = new Random();

    /**
     * Actual word.
     */
    private String text;

    /**
     * Virtual X coordinate.
     */
    private double x;

    /**
     * Virtual Y coordinate.
     */
    private double y;

    /**
     * Font size.
     */
    private float size;

    /**
     * Word color.
     */
    private Color color;

    /**
     * Constructor.
     * @param text Actual word text.
     */
    public Word(String text) {
        this.text = text;
        x = random.nextDouble();
        y = random.nextDouble();
        size = random.nextFloat();

        /* Always pick a relatively bright color. */
        color = Color.getHSBColor(random.nextFloat(),
                0.6f + 0.4f * random.nextFloat(),
                0.6f + 0.4f * random.nextFloat());
    }

    /**
     * Get the actual text.
     * @return The actual text.
     */
    public String getText() {
        return text;
    }

    /**
     * Get the virtual X coordinate.
     * @return The virtual X coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Get the virtual Y coordinate.
     * @return The virtual Y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Get the size of the word.
     * @return The size of the word.
     */
    public float getSize() {
        return size;
    }

    /**
     * Get the color of the word.
     * @return The word color.
     */
    public Color getColor() {
        return color;
    }
}
