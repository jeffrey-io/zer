package io.jeffrey.zer.edits;

/**
 * A link between text and an integer
 *
 * @author jeffrey
 *
 */
public class EditInteger extends EditPrimitive<Integer> {
    /**
     * @param name
     *            the unique name of the field
     * @param v
     *            the value
     */
    public EditInteger(final String name, final Integer v) {
        super(name, v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsText() {
        return Integer.toString(value());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setByText(final String txt) {
        try {
            this.value(Integer.parseInt(txt));
            return true;
        } catch (final NumberFormatException nfe) {
            return false;
        }
    }
}
