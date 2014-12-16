package io.jeffrey.zer;

import io.jeffrey.zer.meta.LayerProperties;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Defines high level interactions over a canvas and maps it directly to a GraphicsContext
 *
 * @author jeffrey
 *
 */
public class Surface {

    private final Camera          camera;
    private final Canvas          canvas;

    public double                 cursor_x;
    public double                 cursor_y;
    private final SurfaceData     data;
    private final SelectionWindow window;

    /**
     * @param canvas
     *            the actual canvas
     * @param data
     *            the specifics of how the surface gets rendered and touched
     * @param status
     *            the textual status that we are free to update
     */
    public Surface(final Canvas canvas, final SurfaceData data) {
        this.canvas = canvas;
        this.data = data;
        camera = data.getCamera();
        window = new SelectionWindow();
        cursor_x = 0;
        cursor_y = 0;
    }

    private void drawGridLines(final double gridSize, final GraphicsContext gc) {
        final double yStart = Math.floor(camera.projY(0) / gridSize) * gridSize;
        final double yEnd = Math.ceil(camera.projY(canvas.getHeight()) / gridSize) * gridSize;
        final double xStart = Math.floor(camera.projX(0) / gridSize) * gridSize;
        final double xEnd = Math.ceil(camera.projX(canvas.getWidth()) / gridSize) * gridSize;
        for (double g = yStart; g <= yEnd; g += gridSize) {
            gc.strokeLine(0, camera.y(g), canvas.getWidth(), camera.y(g));
            gc.fillText("" + (int) g, 0, camera.y(g));
        }
        for (double g = xStart; g <= xEnd; g += gridSize) {
            gc.strokeLine(camera.x(g), 4, camera.x(g), canvas.getHeight());
            gc.fillText("" + (int) g, camera.x(g), canvas.getHeight() - 4);
        }
    }

    private boolean gridSizeOk(final double gridSize) {
        final double d = Math.ceil(camera.projY(canvas.getHeight()) / gridSize) - Math.floor(camera.projY(0) / gridSize);
        return d < (canvas.getHeight() + canvas.getWidth()) / 20.0;
    }

    /**
     * draw the surface
     */
    public double render() {
        final long start = System.nanoTime();
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double gridSize = Math.pow(2, 16);

        boolean ok = false;
        while (!ok && gridSize > 1) {
            gridSize /= 2;
            final double d = Math.ceil(camera.projY(canvas.getHeight()) / gridSize) - Math.floor(camera.projY(0) / gridSize);
            ok = d > 16;
        }

        gc.save();

        // draw the cursor beam
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(0.75);
        gc.strokeLine(camera.x(cursor_x), 0, camera.x(cursor_x), canvas.getHeight());
        gc.strokeLine(0, camera.y(cursor_y), canvas.getWidth(), camera.y(cursor_y));

        final LayerProperties layer = data.getActiveLayer();

        gc.setFill(Color.BLACK);
        gc.setFont(new Font(11));
        if (layer != null) {
            if (gridSizeOk(layer.gridMinor.value())) {
                gc.setStroke(Color.LIGHTGRAY);
                gc.setLineWidth(1.0);
                drawGridLines(layer.gridMinor.value(), gc);
            }
            if (gridSizeOk(layer.gridMajor.value())) {
                gc.setStroke(Color.DARKGRAY);
                gc.setLineWidth(1.5);
                drawGridLines(layer.gridMajor.value(), gc);
            }
        } else {
            gc.setStroke(Color.DARKGRAY);
            gc.setLineWidth(0.5);
            drawGridLines(gridSize, gc);
        }
        gc.restore();

        gc.save();
        try {
            data.draw(gc, camera);
        } finally {
            gc.restore();
        }
        window.drawWindow(camera, gc);
        final long end = System.nanoTime();
        return (Math.abs(end - start) + 1) / 1_000_000_000.0;
    }

    public void resetCamera() {
        camera.reset();
        camera.tX = canvas.getWidth() / 2;
        camera.tY = canvas.getHeight() / 2;
    }

    /**
     * begin a mouse interaction
     *
     * @param event
     *            the mouse's current state
     * @return a mouse interaction to update with move() and ultimately a commit()
     */
    public MouseInteraction startInteraction(final MouseEvent event) {
        final AdjustedMouseEvent aevent = new AdjustedMouseEvent(camera, event.getX(), event.getY(), event.isAltDown());

        if (event.isControlDown() && event.isPrimaryButtonDown() || event.isMiddleButtonDown()) {
            return new Pan(camera, event);
        }

        if (data.isInSelectionSet(aevent)) {
            final MouseInteraction setmove = data.getSelectionMovers(aevent);
            if (setmove != null) {
                return setmove;
            }
        }

        final MouseInteraction interaction = data.startSurfaceInteraction(aevent);

        if (interaction != null) {
            return interaction;
        }

        cursor_x = aevent.position.x_0;
        cursor_y = aevent.position.y_0;

        if (event.isPrimaryButtonDown()) {
            return new SelectionBand(aevent, window, data);
        }

        return null;
    }
}
