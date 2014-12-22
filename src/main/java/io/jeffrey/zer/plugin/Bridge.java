package io.jeffrey.zer.plugin;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * Connects the underlying tool's model to the javascript, and provides some common functionalities
 *
 * @author jeffrey
 *
 */
public class Bridge extends ImporterTopLevel {
    private static class NullCallable implements Callable {
        @Override
        public Object call(final Context context, final Scriptable scope, final Scriptable holdable, final Object[] objects) {
            return objects[1];
        }
    }

    private static final NullCallable NULL_CALLABLE    = new NullCallable();
    private static final long         serialVersionUID = 7996341368896794253L;

    private final Model               model;

    public Bridge(final Model model) {
        this.model = model;

        final Context context = Context.enter();
        try {
            initStandardObjects(context, false);
            defineFunctionProperties(new String[] { "bridge", "invoke", "debug" }, Bridge.class, ScriptableObject.DONTENUM);
            context.evaluateString(this, "var $ = bridge;", "bridge-connector", 0, null);
        } finally {
            Context.exit();
        }
    }

    /**
     * defines the core function to bridge how data is read and written
     *
     * @param query
     *            the query
     * @param valueIfPut
     *            the value we wish to evalute to put too
     * @return an object if we are reading data
     */
    public Object bridge(final String query, final Object valueIfPut) {
        if (valueIfPut instanceof Undefined) {
            final String json = model.getJson(query);
            return NativeJSON.parse(Context.getCurrentContext(), this, json, NULL_CALLABLE);
        } else {
            model.put(query, valueIfPut.toString());
            return "null";
        }
    }

    /**
     * log the given thing _somewhere_
     *
     * @param thing
     *            the thing we are going to log out to the console
     */
    public void debug(final Object thing) {
        final Object json = NativeJSON.stringify(Context.getCurrentContext(), this, thing, NULL_CALLABLE, NULL_CALLABLE);
        final String pretty = (String) json;
        // TODO: add a visual way of understanding what is happening with the, you know, thing
        System.err.println(pretty);
    }

    /**
     * invoke a method on the given query
     *
     * @param query
     *            the query
     * @param method
     *            the method to invoke
     * @return a javascript object
     */
    public Object invoke(final String query, final String method) {
        final String json = model.invokeAndReturnJson(query, method);
        return NativeJSON.parse(Context.getCurrentContext(), this, json, NULL_CALLABLE);
    }
}
