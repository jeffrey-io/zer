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
    public final File    file;
    private long         lastTime;
    private final Model  model;

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
        file = new File(filename);
        this.model = model;
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
        model.begin();
        try {
            context.evaluateString(bridge, "evaluate(\"" + id + "\");", "avail." + id, 0, null);
        } finally {
            model.end();
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
        model.begin();
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
            model.end();
            Context.exit();
        }
    }

    /**
     * ping the plugin to see if it needs to be updated
     *
     * @throws Exception
     *             we failed to reload the file
     * @return true if the plugin was updated
     */
    public boolean ping() throws Exception {
        if (file.exists() && lastTime != file.lastModified()) {
            reload();
            return true;
        }
        return false;
    }

    /**
     * reload the plugin
     */
    private void reload() throws Exception {
        final Context context = Context.enter();
        try {
            lastTime = file.lastModified();
            context.evaluateReader(bridge, new BufferedReader(new FileReader(file)), file.getName(), 0, null);
        } finally {
            Context.exit();
        }
    }
}
