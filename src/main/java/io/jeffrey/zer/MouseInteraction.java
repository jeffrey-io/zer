package io.jeffrey.zer;

/**
 * Defines the basics of the state machine behind mouse interaction
 *
 * @author jeffrey
 *
 */
public interface MouseInteraction {

    /**
     * Cancel the current interaction
     */
    public void cancel();

    /**
     * Commit the current interaction
     */
    public void commit();

    /**
     * The mouse has moved during the current interaction
     *
     * @param event
     *            where the mouse has moved too along with other signals
     */
    public void moved(AdjustedMouseEvent event);
}
