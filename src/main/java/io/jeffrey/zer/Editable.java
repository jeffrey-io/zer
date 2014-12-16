package io.jeffrey.zer;

import io.jeffrey.zer.edits.Edit;
import io.jeffrey.zer.meta.SurfaceItemEditorBuilder;

import java.util.List;
import java.util.Map;

/**
 * Defines an editable item
 *
 * @author jeffrey
 */
public interface Editable {

    /**
     * create an editor for things in the surface
     *
     * @param data
     *            where data comes from
     * @param builder
     *            how the editor is build
     * @param parent
     *            what to update
     */
    public void createEditor(SurfaceData data, SurfaceItemEditorBuilder builder, Syncable parent);

    /**
     * @return a list of actions that apply to the thing in question
     */
    public List<String> getActions();

    /**
     * @param withHistory
     *            should the links be bound to any concept of a history
     * @return bi-direction links into the data
     */
    public Map<String, Edit> getLinks(boolean withHistory);

    /**
     * @return the unique identifier
     */
    public String id();

    /**
     * perform an action against this editable thing
     *
     * @param action
     *            the action to perform
     * @return an object that can be serialized to JSON
     */
    public Object invoke(String action);

    /**
     * @param key
     *            the name of the metadata
     * @param defaultValue
     *            the default value should it not be present
     * @return the edit link to the metadata
     */
    public Edit metadataOf(String key, String defaultValue);
}
