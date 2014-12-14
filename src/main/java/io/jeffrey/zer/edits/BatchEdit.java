package io.jeffrey.zer.edits;

/**
 * Very simple implementation of an edit that extends over a vast array of edits
 *
 * @author jeffrey
 *
 */
public class BatchEdit extends Edit {

    private final Edit[] edits;

    /**
     * @param edits
     *            the edits that will be combined into one
     */
    public BatchEdit(final Edit... edits) {
        this.edits = edits;
        final String name = edits[0].name();
        for (final Edit ed : edits) {
            if (!ed.name().equals(name)) {
                throw new IllegalArgumentException("Invalid Name");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsText() {
        String value = edits[0].getAsText();
        for (final Edit ed : edits) {
            if (!value.equals(ed.getAsText())) {
                value = "";
            }
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return edits[0].name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean setByText(final String txt) {
        final String[] backup = new String[edits.length];
        for (int k = 0; k < edits.length; k++) {
            backup[k] = edits[k].getAsText();
            if (!edits[k].setByText(txt)) {
                for (int j = k; j >= 0; j--) {
                    edits[j].setByText(backup[j]);
                }
                return false;
            }
        }
        return true;
    }
}
