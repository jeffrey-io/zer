package io.jeffrey.zer;

import io.jeffrey.zer.edits.ObjectDataMap;

import java.util.HashMap;
import java.util.Map;

/**
 * a camera that defines the view transformation
 *
 * @author jeffrey
 */
public class Camera {
    public boolean dirty = true;
    public double  scale = 1.0;
    public double  tX    = 0;
    public double  tY    = 0;

    /**
     * @return a map of all the data for the camera
     */
    public Map<String, Object> pack() {
        final HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("tx", tX);
        data.put("ty", tY);
        data.put("scale", scale);
        return data;
    }

    /**
     * transform the screen value into the world space
     *
     * @param x
     *            the coordinate in screen space
     * @return the coordinate in world space
     */
    public double projX(final double x) {
        return (x - tX) / scale;
    }

    /**
     * transform the screen value into the world space
     *
     * @param y
     *            the coordinate in screen space
     * @return the coordinate in world space
     */
    public double projY(final double y) {
        return (y - tY) / scale;
    }

    /**
     * restore the camera to the defaults
     */
    public void reset() {
        tX = 0.0;
        tY = 0.0;
        scale = 1.0;
    }

    /**
     * pull the data for the camera from the given map
     *
     * @param data
     */
    public void sync(final ObjectDataMap data) {
        dirty = true;
        tX = data.getDouble("tx", tX).value();
        tY = data.getDouble("ty", tY).value();
        scale = data.getDouble("scale", scale).value();
    }

    /**
     * transform the given world value into screen space
     *
     * @param x
     *            the x coordinate of the world point
     * @return the x coordinate in screen space
     */
    public double x(final double x) {
        return x * scale + tX;
    }

    /**
     * transform the given world value into screen space
     *
     * @param y
     *            the y coordinate of the world point
     * @return the y coordinate in screen space
     */
    public double y(final double y) {
        return y * scale + tY;
    }
}
