package io.jeffrey.zer.meta;

import io.jeffrey.vector.VectorRegister2;
import io.jeffrey.zer.Camera;
import io.jeffrey.zer.edits.EditDouble;

/**
 * a guide line which objects can snap too
 *
 * @author jeffrey
 */
public class GuideLine {

    public static GuideLine fromString(final String v) {
        final String[] parts = v.split(",");
        if (parts.length != 4) {
            return null;
        }
        final GuideLine gl = new GuideLine();
        gl.a.set(parts[0]);
        gl.b.set(parts[1]);
        gl.c.set(parts[2]);
        gl.distance.set(parts[3]);
        return gl;
    }

    public final EditDouble a;
    public final EditDouble b;
    public final EditDouble c;

    public final EditDouble distance;

    // a x + b y = c
    public GuideLine() {
        a = new EditDouble("a", 1.0);
        b = new EditDouble("b", 0.0);
        c = new EditDouble("c", 1.0);
        distance = new EditDouble("distance", 10.0);
    }

    @Override
    public String toString() {
        return a.value() + "," + b.value() + "," + c.value() + "," + distance.value();
    }

    public void writeSegment(final Camera camera, final VectorRegister2 reg) {
        if (Math.abs(a.value()) > 0) {
            double y = camera.projY(-10000);
            reg.set_0((c.value() - b.value() * y) / a.value(), y);
            y = camera.projY(10000);
            reg.set_1((c.value() - b.value() * y) / a.value(), y);
        } else if (Math.abs(b.value()) > 0) {
            double x = camera.projX(-10000);
            reg.set_0(x, (c.value() - a.value() * x) / b.value());
            x = camera.projX(10000);
            reg.set_1(x, (c.value() - a.value() * x) / b.value());
        }
    }

}
