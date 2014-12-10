package io.jeffrey.zer.edits;

import java.util.Map;
import java.util.Set;

/**
 * Read from a probably mis-typed Map<String, Object> fields.
 *
 * This makes it easy to handle the situations where strings should be doubles and doubles are actually integers
 *
 * @author jeffrey
 */
public class ObjectDataMap {

    public final Map<String, Object> fields;

    public ObjectDataMap(final Map<String, Object> fields) {
        this.fields = fields;
    }

    /**
     * Get a bound boolean
     *
     * @param name
     *            the name of the boolean
     * @param def
     *            the default value should it not exist
     * @return a editable link to the boolean
     */
    public EditBoolean getBoolean(final String name, final boolean def) {
        return new EditBoolean(name, lookup(name, def)) {
            @Override
            public boolean setByText(final String txt) {
                fields.put(name, txt);
                return super.setByText(txt);
            }
        };
    }

    /**
     * Get a bound double
     *
     * @param name
     *            the name of the double
     * @param def
     *            the default value should it not exist
     * @return a editable link to the double
     */
    public EditDouble getDouble(final String name, final double def) {
        return new EditDouble(name, lookup(name, def)) {
            @Override
            public boolean setByText(final String txt) {
                fields.put(name, txt);
                return super.setByText(txt);
            }
        };
    }

    /**
     * Get a bound integer
     *
     * @param name
     *            the name of the integer
     * @param def
     *            the default value should it not exist
     * @return a editable link to the integer
     */
    public EditInteger getInteger(final String name, final int def) {
        return new EditInteger(name, lookup(name, def)) {
            @Override
            public boolean setByText(final String txt) {
                fields.put(name, txt);
                return super.setByText(txt);
            }
        };
    }

    /**
     * Get a bound string
     *
     * @param name
     *            the name of the string
     * @param def
     *            the default value should it not exist
     * @return a editable link to the string
     */
    public EditString getString(final String name, final String def) {
        return new EditString(name, lookup(name, def)) {
            @Override
            public boolean setByText(final String txt) {
                fields.put(name, txt);
                return super.setByText(txt);
            }
        };
    }

    /**
     * @return a set of all keys defined in the map
     */
    public Set<String> keys() {
        return fields.keySet();
    }

    private boolean lookup(final String name, final boolean def) {
        final Object v = fields.get(name);

        if (v == null) {
            return def;
        }
        if (v instanceof Boolean) {
            return (Boolean) v;
        }
        if (v instanceof String) {
            final String txt = ((String) v).trim().toLowerCase();
            return txt.equals("true") || txt.equals("yes");
        }
        return def;
    }

    private double lookup(final String name, final double def) {
        final Object v = fields.get(name);

        if (v == null) {
            return def;
        }
        if (v instanceof Double) {
            return (Double) v;
        }
        if (v instanceof Integer) {
            return (Integer) v;
        }
        if (v instanceof String) {
            return Double.parseDouble((String) v);
        }
        return def;
    }

    private int lookup(final String name, final int def) {
        final Object v = fields.get(name);

        if (v == null) {
            return def;
        }
        if (v instanceof Integer) {
            return (Integer) v;
        }
        if (v instanceof String) {
            return Integer.parseInt((String) v);
        }
        return def;
    }

    private String lookup(final String name, final String def) {
        final Object v = fields.get(name);
        if (v == null) {
            return def;
        }
        if (v instanceof String) {
            return (String) v;
        }
        return def;
    }
}
