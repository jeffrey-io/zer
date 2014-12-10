package io.jeffrey.zer;

/**
 * Defines a selection band that is updated
 *
 * @author jeffrey
 *
 */
public class SelectionBand implements MouseInteraction {
    private final SurfaceData     data;
    private final SelectionWindow window;

    /**
     * @param camera
     *            the camera that we intend to manipulate
     * @param event
     *            the raw mouse event to start from
     */
    public SelectionBand(final AdjustedMouseEvent event, final SelectionWindow window, final SurfaceData data) {
        this.data = data;
        this.window = window;
        window.start(event.position.x_0, event.position.y_0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel() {
        window.end();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        window.end();
        // send a commit
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moved(final AdjustedMouseEvent event) {
        window.update(event.position.x_0, event.position.y_0);
        if (!window.empty()) {
            data.updateSelectionWindow(window);
        }
    }
}
