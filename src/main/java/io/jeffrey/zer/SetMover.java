package io.jeffrey.zer;

import java.util.Set;

/**
 * Handy tool to iteract with a set
 *
 * @author jeffrey
 */
public class SetMover implements MouseInteraction {
    private final Set<MouseInteraction> interactions;

    /**
     * @param interactions
     *            all interactions that will be executed
     */
    public SetMover(final Set<MouseInteraction> interactions) {
        this.interactions = interactions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel() {
        for (final MouseInteraction interaction : interactions) {
            interaction.cancel();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        for (final MouseInteraction interaction : interactions) {
            interaction.commit();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moved(final AdjustedMouseEvent event) {
        for (final MouseInteraction interaction : interactions) {
            interaction.moved(event);
        }
    }
}
