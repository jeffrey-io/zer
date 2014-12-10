package io.jeffrey.zer.meta;

/**
 * Defines the requirements for map values to be edited by the AbstractMapEditor
 *
 * @author jeffrey
 *
 */
public abstract class AbstractMapEditorItemRequirements {

    private final String id;
    private String       name;

    public AbstractMapEditorItemRequirements(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return the globally unique id for the item
     */
    public String id() {
        return id;
    }

    /**
     * return the name of the item
     */
    public String name() {
        return name;
    }

    /**
     * set the name of the item
     *
     * @param name
     *            the new name
     */
    public void name(final String name) {
        this.name = name;
    }

    /**
     * Return the name of the item {@inheritDoc}
     */
    @Override
    public String toString() {
        return name();
    }
}
