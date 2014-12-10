package io.jeffrey.zer.meta;

import io.jeffrey.zer.edits.Edit;
import io.jeffrey.zer.edits.EditBoolean;
import io.jeffrey.zer.edits.EditDouble;
import io.jeffrey.zer.edits.EditInteger;
import io.jeffrey.zer.edits.ObjectDataMap;

import java.util.HashMap;
import java.util.Map;

/**
 * defines the properties of the layer
 *
 * @author jeffrey
 */
public class LayerProperties extends AbstractMapEditorItemRequirements {
    private static double _snap(double v, final double r) {
        v /= r;
        v = Math.round(v);
        v *= r;
        return v;
    }

    /**
     * the layer's major grid
     */
    public final EditDouble  gridMajor;

    /**
     * the layer's minor grid
     */
    public final EditDouble  gridMinor;

    /**
     * snap to the major grid
     */
    public final EditBoolean snapMajor;

    /**
     * snap to the minor grid
     */
    public final EditBoolean snapMinor;

    /**
     * the zorder of the layer
     */
    public final EditInteger zorder;

    /**
     * @param id
     *            the unique id of the layer
     * @param name
     *            the mutable name of the layer
     */
    public LayerProperties(final String id, final String name) {
        super(id, name);
        zorder = new EditInteger("zorder", 0);
        gridMajor = new EditDouble("major", 10.0);
        gridMinor = new EditDouble("minor", 1.0);
        snapMajor = new EditBoolean("snap-major", false);
        snapMinor = new EditBoolean("snap-minor", true);
    }

    /**
     * @return a packed representation for the layer
     */
    public Map<String, Object> pack() {
        final HashMap<String, Object> packed = new HashMap<String, Object>();
        for (final Edit ed : new Edit[] { zorder, gridMajor, gridMinor, snapMajor, snapMinor }) {
            packed.put(ed.name(), ed.getAsText());
        }
        packed.put("name", name());
        return packed;
    }

    /**
     * snap to a grid
     *
     * @param v
     *            the value to snap
     * @return the resulting snap'd value
     */
    public double snap(final double v) {
        double v0 = v;
        double v1 = v;
        if (snapMajor.value() && snapMinor.value()) {
            v0 = _snap(v, gridMajor.value());
            v1 = _snap(v, gridMinor.value());
        } else if (snapMajor.value()) {
            v0 = _snap(v, gridMajor.value());
            v1 = v0;
        } else if (snapMinor.value()) {
            v0 = _snap(v, gridMinor.value());
            v1 = v0;
        }
        if (Math.abs(v - v0) < Math.abs(v - v1)) {
            return v0;
        }
        return v1;
    }

    /**
     * unpack the map and build the layer
     *
     * @param map
     *            where the data is stored
     */
    public void unpack(final ObjectDataMap map) {
        zorder.setByText(map.getInteger("zorder", zorder.value()).getAsText());
        gridMajor.setByText(map.getDouble("major", gridMajor.value()).getAsText());
        gridMinor.setByText(map.getDouble("minor", gridMinor.value()).getAsText());
        snapMajor.setByText(map.getBoolean("snap-major", snapMajor.value()).getAsText());
        snapMinor.setByText(map.getBoolean("snap-minor", snapMinor.value()).getAsText());
    }

}
