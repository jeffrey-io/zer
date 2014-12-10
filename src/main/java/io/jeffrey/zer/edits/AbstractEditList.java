package io.jeffrey.zer.edits;

import java.util.ArrayList;

/**
 * Defines how many edits are updated
 *
 * @author jeffrey
 *
 */
public abstract class AbstractEditList extends Edit {

    /**
     * all the edits available
     */
    public final ArrayList<Edit> edits;

    /**
     * the aggregate name of all the edits
     */
    public final String          name;

    /**
     * @param name
     *            the aggregate name of all the edits
     */
    public AbstractEditList(final String name) {
        this.name = name;
        edits = new ArrayList<Edit>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return name;
    }

}
