package io.jeffrey.zer.edits;

/**
 * A boolean link
 *
 * @author jeffrey
 *
 */
public class EditBoolean extends EditPrimitive<Boolean> {

    /**
     * @param name
     *            the unique name of the field
     * @param v
     *            the value
     */
    public EditBoolean(final String name, final Boolean v) {
        super(name, v);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsText() {
        if (value()) {
            return "yes";
        }
        return "no";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setByText(final String txt) {
        boolean parsed = false;
        final String x = txt.trim().replaceAll("\\s", "");
        if (x.equalsIgnoreCase("true")) {
            parsed = true;
        }
        if (x.equalsIgnoreCase("1")) {
            parsed = true;
        }
        if (x.equalsIgnoreCase("yes")) {
            parsed = true;
        }
        value(parsed);
        return true;
    }

}
