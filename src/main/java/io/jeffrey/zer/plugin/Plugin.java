package io.jeffrey.zer.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.mozilla.javascript.Context;

/**
 * Defines a file as a plug in
 *
 * @author jeffrey
 *
 */
public class Plugin {

    private final Bridge bridge;
    public final String  filename;

    /**
     *
     * @param filename
     *            the filename containing the file
     * @param model
     *            the model of what the plugin can read and write
     * @throws Exception
     *             we were unable to load the plugin
     */
    public Plugin(final String filename, final Model model) throws Exception {
        this.filename = filename;
        bridge = new Bridge(model);
        reload();

    }

    /**
     * evaluate the plugin on the given element by the given id
     *
     * @param id
     *            the identifer of the item we wish to manipulate
     */
    public void evaluate(final String id) {
        final Context context = Context.enter();
        try {
            context.evaluateString(bridge, "evaluate(\"" + id + "\");", "avail." + id, 0, null);
        } finally {
            Context.exit();
        }
    }

    /**
     * is the given plugin actionable on the given element with the given id
     *
     * @param id
     *            the identifer of the item we wish to check
     * @return true if we can evaluate the plugin
     */
    public boolean isActionable(final String id) {
        final Context context = Context.enter();
        try {
            final Object result = context.evaluateString(bridge, "available(\"" + id + "\");", "avail." + id, 0, null);
            if (result == null) {
                return false;
            }
            if (result instanceof Boolean) {
                return (Boolean) result;
            }
            return false;
        } finally {
            Context.exit();
        }
    }

    /**
     * reload the plugin
     *
     * @throws Exception
     */
    public void reload() throws Exception {
        final Context context = Context.enter();
        try {
            context.evaluateReader(bridge, new BufferedReader(new FileReader(new File(filename))), filename, 0, null);
        } finally {
            Context.exit();
        }
    }
}
