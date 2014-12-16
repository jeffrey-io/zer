package io.jeffrey.zer.meta;

import io.jeffrey.zer.Syncable;
import io.jeffrey.zer.edits.Edit;

import java.util.Map;

/**
 * Isolate how items describe their builder
 *
 * @author jeffrey
 *
 */
public interface SurfaceItemEditorBuilder {
    /**
     * An easy grid
     *
     * @author jeffrey
     *
     */
    public interface SurfaceFourColumnGrid {
        /**
         * add four edit columns
         *
         * @param value0
         *            the value of the first text field
         * @param value1
         *            the value of the second text field
         * @param value2
         *            the value of the third text field
         * @param value3
         *            the value of the fourth text field
         */
        public void add(final Edit value0, final Edit value1, final Edit value2, final Edit value3);

        /**
         * add an element with a label and a value that spaces 3 columns
         *
         * @param label
         *            the label of the text box
         * @param value
         *            the value of the text field
         */
        public void add(final String label, final Edit value);

        /**
         * add an element with label, value, label, value for things like x,y coordinates
         *
         * @param label0
         *            the label of the first text box
         * @param value0
         *            the value of the first text field
         * @param label1
         *            the label of the second text box
         * @param value1
         *            the value of the second text field
         */
        public void add(final String label0, final Edit value0, final String label1, final Edit value1);

        /**
         * add an element with two labels
         *
         * @param label
         *            the label of the text box
         * @param value
         *            the faux value of the text box
         */
        public void add(final String label, final String value);

        /**
         * add columns
         *
         * @param label0
         *            the first label
         * @param label1
         *            the second label
         * @param label2
         *            the third label
         * @param label3
         *            the fourth label
         */
        public void add(final String label0, final String label1, final String label2, final String label3);
    }

    /**
     * add a button
     *
     * @param label
     *            the label of the button
     * @param runnable
     *            the action to run when the button is clicked
     */
    public abstract void addAction(String label, Runnable runnable);

    /**
     * add a checkbox
     *
     * @param label
     *            the label of the checkbox
     * @param link
     *            the data link
     * @return the current value
     */
    public abstract boolean addBoolean(String label, Edit link);

    /**
     * add a color picker
     *
     * @param label
     *            the label for the color picker
     * @param link
     *            the data source
     * @return the current color
     */
    public abstract String addColor(String label, Edit link);

    /**
     *
     * @param canEdit
     *            can we add/delete elements of this type
     * @param label
     *            the name of the combobox
     * @param link
     *            the data link
     * @param values
     *            all current values
     * @param clazz
     *            the class for the editor (only used with canEdit is true)
     * @param parent
     *            the parent syncable to update
     * @return the current value
     */
    public abstract <T extends AbstractMapEditorItemRequirements, E extends AbstractMapEditor<T>> T addCombo(boolean canEdit, String label, Edit link, Map<String, T> values, Class<E> clazz, Syncable parent);

    /**
     * add a file picker
     *
     * @param title
     *            the title of the file chooser
     * @param label
     *            the label for the file picker
     * @param link
     *            the data source
     * @return the current file
     */
    public abstract String addFile(String title, String label, Edit link, DocumentFileSystem normalize);

    /**
     * no longer use the border
     */
    public abstract void endBorder();

    /**
     * start a border around the remaining items
     *
     * @param label
     *            the label of the border
     */
    public abstract void startBorder(String label);

    /**
     * @return a builder for a grid with four columns
     */
    public abstract SurfaceFourColumnGrid startFourColumnGrid();

}