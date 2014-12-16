package io.jeffrey.zer.meta;

import io.jeffrey.zer.Syncable;
import io.jeffrey.zer.edits.Edit;

import java.io.File;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

/**
 * Handy Dandy Class to bind JavaFx classes to our Edit schema
 *
 * @author jeffrey
 */
public class EditBinding {
    private boolean        building;
    private String         focusOn    = null;
    final Pane             root;
    private final Syncable syncable;
    private Node           toSetFocus = null;

    /**
     * @param syncable
     *            the thing that gets updated when a value changes
     */
    public EditBinding(final Pane root, final Syncable syncable) {
        this.root = root;
        this.syncable = syncable;
        building = false;
    }

    /**
     * bind a checkbox
     *
     * @param checkbox
     *            the checkbox to bind
     * @param value
     *            the value to connect to
     */
    public void bindBoolean(final CheckBox checkbox, final Edit value) {
        try {
            building = true;
            checkbox.setIndeterminate(false);
            checkbox.setSelected(value.getAsText().equals("yes"));
            checkbox.selectedProperty().addListener(new ChangeListener<Boolean>() {

                @Override
                public void changed(final ObservableValue<? extends Boolean> observed, final Boolean before, final Boolean after) {
                    value.set(after ? "yes" : "no");
                    syncable.sync();
                }

            });
            bindFocus(value.name(), checkbox);
        } finally {
            building = false;
        }
    }

    /**
     * bind a color picker
     *
     * @param colorPicker
     *            the color picker in question
     * @param value
     *            the value to link to
     */
    public void bindColor(final ColorPicker colorPicker, final Edit value) {
        try {
            building = true;
            Color color = Color.BLACK;
            try {
                color = Color.valueOf(value.getAsText());
            } catch (final Exception err) {
            }

            colorPicker.setValue(color);
            colorPicker.valueProperty().addListener(new ChangeListener<Color>() {
                @Override
                public void changed(final ObservableValue<? extends Color> val, final Color before, final Color after) {
                    value.set(after.toString());
                    syncable.sync();
                }
            });
            bindFocus(value.name(), colorPicker);
        } finally {
            building = false;
        }
    }

    /**
     * bind a combobox
     *
     * @param selector
     *            the combobox to bind
     * @param value
     *            the value to connect to
     * @param values
     *            all the current values
     * @param parent
     *            the owner that requires update once the value is changed
     * @param <T>
     *            the type of what we are selecting
     */
    public <T extends AbstractMapEditorItemRequirements> void bindComboBox(final ComboBox<T> selector, final Edit value, final Map<String, T> values, final Syncable parent) {
        try {
            building = true;
            final T cr = values.get(value.getAsText());
            if (cr != null) {
                selector.setValue(cr);
            }
            selector.valueProperty().addListener(new ChangeListener<T>() {
                @Override
                public void changed(final ObservableValue<? extends T> val, final T before, final T after) {
                    if (value.set(after.id())) {
                        syncable.sync();
                        parent.sync();
                    }
                }
            });
            selector.getItems().addAll(values.values());
            bindFocus(value.name(), selector);
        } finally {
            building = false;
        }
    }

    /**
     * bind a button to pick a file
     *
     * @param title
     *            the title of the dialog to open
     * @param button
     *            the button to bind
     * @param ref
     *            the visual text field to update
     * @param value
     *            the value to connect to
     * @param normalize
     *            the method to normalize the file to a string
     */
    public void bindFile(final String title, final Button button, final TextField ref, final Edit value, final DocumentFileSystem normalize) {
        try {
            building = true;
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent arg0) {
                    final FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle(title);
                    final File file = fileChooser.showOpenDialog(null);
                    final String newUri = normalize.normalize(file);
                    ref.setText(newUri);
                    value.set(newUri);
                    syncable.sync();
                }
            });
            bindFocus(value.name(), button);
        } finally {
            building = false;
        }
    }

    /**
     * Helper function to ensure the focus is set appropriately
     *
     * @param name
     *            the name of the node
     * @param node
     *            the node to watch for focus
     */
    private void bindFocus(final String name, final Node node) {
        node.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(final ObservableValue<? extends Boolean> dontCare, final Boolean reallyDontCare, final Boolean after) {
                if (after && !building) {
                    focusOn = name;
                }
                if (!after) {

                }
            }
        });
        if (name.equals(focusOn)) {
            toSetFocus = node;
        }
    }

    /**
     * bind a text field
     *
     * @param field
     *            the TextField to bind
     * @param value
     *            the value to connect to
     */
    public void bindTextField(final TextField field, final Edit value) {
        try {
            building = true;
            field.setText(value.getAsText());
            final EventHandler<ActionEvent> upload = new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent dc) {
                    if (!value.set(field.getText())) {
                    } else {
                        syncable.sync();
                    }
                }
            };
            field.setOnAction(upload);
            field.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(final KeyEvent arg0) {
                    upload.handle(null);
                }
            });
            bindFocus(value.name(), field);
        } finally {
            building = false;
        }
    }

    /**
     * attempt to grab the focus if we can
     */
    public void focus() {
        if (toSetFocus != null) {
            toSetFocus.requestFocus();
        } else {
            root.requestFocus();
            toSetFocus = null;
        }
    }

    /**
     * denote that the focus will not be acquired
     */
    public void resetFocus() {
        toSetFocus = null;
        focusOn = null;
    }
}
