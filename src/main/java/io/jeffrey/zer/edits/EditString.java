package io.jeffrey.zer.edits;

/**
 * A link between two pieces of text, a text bridge so to speak
 *
 * @author jeffrey
 *
 */
public class EditString extends EditPrimitive<String> {
    /**
     * @param name
     *            the unique name of the field
     * @param v
     *            the value
     */
    public EditString(final String name, final String v) {
        super(name, v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsText() {
        return value();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setByText(final String txt) {
        value(txt);
        return true;
    }
}
