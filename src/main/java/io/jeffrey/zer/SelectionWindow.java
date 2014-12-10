package io.jeffrey.zer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A window defined in screen space
 *
 * @author jeffrey
 */
public class SelectionWindow {
    private boolean engaged;
    private double  h;
    private double  w;
    private double  x;
    private double  x0;
    private double  x1;
    private double  y;
    private double  y0;
    private double  y1;

    /**
     * create an empty window
     */
    public SelectionWindow() {
        engaged = true;
        x0 = 0;
        y0 = 0;
        x1 = 0;
        y1 = 0;
        x = 0;
        y = 0;
        w = 0;
        h = 0;
    }

    /**
     * draw the selection window
     *
     * @param camera
     *            the viewport
     * @param gc
     *            the context for drawing
     */
    public void drawWindow(final Camera camera, final GraphicsContext gc) {
        if (!engaged) {
            return;
        }
        gc.save();
        try {
            gc.setStroke(Color.GREEN);
            gc.translate(camera.tX, camera.tY);
            gc.scale(camera.scale, camera.scale);
            gc.setLineWidth(2.5 / camera.scale);
            gc.strokeRect(x, y, w, h);
        } finally {
            gc.restore();
        }

    }

    public boolean empty() {
        return w == 0 || h == 0;
    }

    /**
     * We are no longer selecting
     */
    public void end() {
        engaged = false;
    }

    /**
     * @return a new double[] with (x,y) pairs flattened in an array
     */
    public double[] rect() {
        final double[] coord = new double[8];
        coord[0] = x;
        coord[1] = y;

        coord[2] = x + w;
        coord[3] = y;

        coord[4] = x + w;
        coord[5] = y + h;

        coord[6] = x;
        coord[7] = y + h;
        return coord;
    }

    /**
     * Start the selection window
     *
     * @param sX
     *            the starting x coordinate in world space
     * @param sY
     *            the starting y coordinate in world space
     */
    public void start(final double sX, final double sY) {
        engaged = true;
        x0 = sX;
        y0 = sY;
        x1 = sX;
        y1 = sY;
    }

    /**
     * update the selection window
     *
     * @param uX
     *            the current x coordinate
     * @param uY
     *            the current y coordinate
     */
    public void update(final double uX, final double uY) {
        x1 = uX;
        y1 = uY;
        x = Math.min(x0, x1);
        y = Math.min(y0, y1);
        w = Math.max(x0, x1) - x;
        h = Math.max(y0, y1) - y;
    }
}
