package io.jeffrey.zer.edits;

/**
 * The base type of something that is linkable
 *
 * @author jeffrey
 */
public abstract class Edit {

    /**
     * @return a textual representation of the the item in question
     */
    public abstract String getAsText();

    /**
     * @return the pretty name of what is being edited
     */
    public abstract String name();

    /**
     * set the value with the given text by parsing it
     *
     * @param txt
     *            the given text to parse
     * @return true if the value was accepted
     */
    public boolean set(final String txt) {
        final boolean result = setByText(txt);
        return result;
    }

    /**
     * set the value with the given text by parsing it
     *
     * @param txt
     *            the given text to parse
     * @return true if the value was accepted
     */
    protected abstract boolean setByText(String txt);
}
