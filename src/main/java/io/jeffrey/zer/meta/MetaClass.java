package io.jeffrey.zer.meta;

import io.jeffrey.zer.edits.ObjectDataMap;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class MetaClass extends AbstractMapEditorItemRequirements {
    /**
     * the fields that define the meta class
     */
    public final Map<String, String> fields;

    /**
     * @param id
     *            the unique id of the metadata class
     * @param name
     *            the name of the metadata
     */
    public MetaClass(final String id, final String name) {
        super(id, name);
        fields = new TreeMap<>();
        fields.put("name", name);
    }

    /**
     * @param fieldName
     *            the field
     * @param defaultValue
     *            the default value for items to pick up
     */
    public void add(final String fieldName, final String defaultValue) {
        fields.put(fieldName, defaultValue);
    }

    /**
     * inject the given packed metadata into the meta class data
     *
     * @param packed
     */
    public void inject(final ObjectDataMap data) {
        for (final String key : data.keys()) {
            fields.put(key, data.getString(key, null).value());
        }
    }

    /**
     * @return a packed representation of the data
     */
    public Map<String, String> pack() {
        return Collections.unmodifiableMap(fields);
    }

}
