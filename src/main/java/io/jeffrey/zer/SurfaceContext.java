package io.jeffrey.zer;

/**
 *
 * a context for operations happening against the surface
 *
 * @author jeffrey
 *
 */
public class SurfaceContext {

    public final Camera camera;
    public double       cursor_x;
    public double       cursor_y;
    public double       height;
    public double       width;

    /**
     * the camera
     * 
     * @param camera
     */
    public SurfaceContext(final Camera camera) {
        this.camera = camera;
        cursor_x = 0;
        cursor_y = 0;
        width = 1;
        height = 1;
    }
}
