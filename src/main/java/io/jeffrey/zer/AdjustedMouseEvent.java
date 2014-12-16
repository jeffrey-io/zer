package io.jeffrey.zer;

import io.jeffrey.vector.VectorRegister8;

/**
 * An adjust mouse event is a mouse event that has been coupled to the document and transformed to the view. The view can be translated and scaled, so this will ensure the (x,y) point of the pouse interaction is correct
 *
 * @author jeffrey
 *
 */
public class AdjustedMouseEvent {

    /**
     * is the alt key held down
     */
    public final boolean         altdown;
    private final Camera         camera;

    public final double          clientX;
    public final double          clientY;

    public final VectorRegister8 position;

    /**
     * public user data that can be attached to the object
     */
    public Object                userdata;

    /**
     * @param camera
     *            the document that holds the view transformation
     * @param event
     *            the raw event
     */
    public AdjustedMouseEvent(final Camera camera, final double x, final double y, final boolean altdown) {
        position = new VectorRegister8();
        clientX = x;
        clientY = y;
        position.set_0((clientX - camera.tX) / camera.scale, (clientY - camera.tY) / camera.scale);
        this.camera = camera;
        this.altdown = altdown;
    }

    /**
     * @param wx
     *            the x coordinate in world space
     * @param wy
     *            the y coordinate in world space
     * @return the distance between the world point and the point on the screen
     */
    public double doodadDistance(final double wx, final double wy) {
        final double dx = Math.abs(camera.x(wx) - clientX);
        final double dy = Math.abs(camera.y(wy) - clientY);
        return Math.max(dx, dy);
    }
}
