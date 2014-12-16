package io.jeffrey.zer;

import javafx.scene.input.MouseEvent;

/**
 * Pan the document
 *
 * @author jeffrey
 *
 */
public class Pan implements MouseInteraction {

    private final Camera camera;
    private final double ix;
    private final double iy;
    private final double x;
    private final double y;

    /**
     * @param camera
     *            the camera that we intend to manipulate
     * @param event
     *            the raw mouse event to start from
     */
    public Pan(final Camera camera, final MouseEvent event) {
        this.camera = camera;
        ix = camera.tX;
        iy = camera.tY;
        x = event.getX();
        y = event.getY();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel() {
        camera.tX = ix;
        camera.tY = iy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moved(final AdjustedMouseEvent event) {
        camera.tX = ix + event.clientX - x;
        camera.tY = iy + event.clientY - y;
    }
}
