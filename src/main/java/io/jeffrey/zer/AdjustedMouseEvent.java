package io.jeffrey.zer;

import io.jeffrey.vector.VectorRegister8;
import javafx.scene.input.MouseEvent;

/**
 * An adjust mouse event is a mouse event that has been coupled to the document and transformed to the view. The view can be translated and scaled, so this will ensure the (x,y) point of the pouse interaction is correct
 *
 * @author jeffrey
 *
 */
public class AdjustedMouseEvent {

    public final VectorRegister8 position;
    /**
     * is the alt key held down
     */
    public final boolean         altdown;

    private final Camera         camera;

    /**
     * the actual mouse event with all the raw data
     */
    public final MouseEvent      event;

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
    public AdjustedMouseEvent(final Camera camera, final MouseEvent event) {
        this.position = new VectorRegister8();
        position.set_0((event.getX() - camera.tX) / camera.scale, (event.getY() - camera.tY) / camera.scale);
        this.camera = camera;
        this.event = event;
        altdown = event.isAltDown();
    }

    /**
     * @param world
     *            the point in world space
     * @return the distance between the world point and the point on the screen
     */
    public double doodadDistance(double wx, double wy) {
        final double dx = Math.abs(camera.x(wx) - event.getX());
        final double dy = Math.abs(camera.y(wy) - event.getY());
        return Math.max(dx, dy);
    }
}
