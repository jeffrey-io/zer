package io.jeffrey.zer.meta;

import io.jeffrey.zer.Syncable;

import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

/**
 * Generic tool for having a combo box that updates a panel (i.e. the body). This is useful to avoid having to do the whole "add", "update", "delete" cycle. You simply type the name, and either it exists or it doesn't. If it doesn't, then you just edit it anyway.
 *
 * @author jeffrey
 *
 * @param <T>
 *            the thing to update
 */
public abstract class AbstractMapEditor<T extends AbstractMapEditorItemRequirements> implements Syncable {
    protected T                      current;
    private boolean                  official;
    private final Syncable           parent;
    protected final ComboBox<String> selector;
    protected boolean                skipNotification = false;
    private final Map<String, T>     things;

    /**
     * @param things
     *            the things to edit
     * @param id
     *            the id selected
     * @param parent
     *            the owning sycnable that needs to be updated when the editor edits
     */
    protected AbstractMapEditor(final Map<String, T> things, final String id, final Syncable parent) {
        this.things = things;
        this.parent = parent;
        if (id != null) {
            current = things.get(id);
        }
        if (current == null) {
            for (final T search : things.values()) {
                if (isDefault(search)) {
                    current = search;
                }
            }
        }
        if (current == null) {
            current = getNewDefault();
            official = false;
        } else {
            official = true;
        }
        selector = new ComboBox<String>();
        selector.setEditable(true);
        selector.setValue(current.name());
        selector.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> val, final String before, final String after) {
                if (skipNotification) {
                    return;
                }
                notifyUpdate(after);
                updateBody();
            }
        });
        selector.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(final ObservableValue<? extends Boolean> dontCare, final Boolean reallyDontCare, final Boolean after) {
                if (after) {
                    takeFocusAwayFromBody();
                }
            }
        });
    }

    /**
     * indicate that we wish to keep the currently generated thing
     */
    protected void checkAndOfficiate() {
        if (!official) {
            skipNotification = true;
            try {
                things.put(current.id(), current);
                official = true;
                title(current.name(), true);
                selector.getItems().add(current.name());
            } finally {
                skipNotification = false;
            }
        }
        parent.sync();
    }

    /**
     * @param name
     *            the new name
     * @return an element of the new thing with the given name
     */
    protected abstract T create(String name);

    /**
     * @return a an instance with the default name
     */
    protected abstract T getNewDefault();

    /**
     * @param thing
     *            the thing to check
     * @return check the given thing to see if it is actually the default
     */
    protected abstract boolean isDefault(T thing);

    /**
     * @return a new globally unique name
     */
    protected String newId() {
        return UUID.randomUUID().toString();
    }

    /**
     * a new name has been produced
     *
     * @param newName
     *            the new name
     */
    private void notifyUpdate(final String newName) {
        if (newName == null) {
            return;
        }
        for (final T cls : things.values()) {
            if (newName.equals(cls.name())) {
                current = cls;
                official = true;
                title(newName, true);
                parent.sync();
                return;
            }
        }
        current = create(newName);
        official = false;
        title(newName, false);
        parent.sync();
    }

    /**
     * the child indicates that it is ready to recieve commands, so send them
     */
    protected void postConstruct() {
        title(current.name(), official);
    }

    @Override
    public void sync() {
        checkAndOfficiate();
        if (parent == null) {
            return;
        }
        parent.sync();
    }

    /**
     * the selector has changed, so let's update it
     */
    protected void syncSelector() {
        selector.getItems().clear();
        String to = null;
        final TreeSet<String> names = new TreeSet<>();
        for (final T cls : things.values()) {
            names.add(cls.name());
            if (current.id().equals(cls.id())) {
                to = cls.name();
            }
        }
        selector.getItems().addAll(names);
        if (to != null) {
            final String old = selector.getValue();
            if (old == null || !old.equals(to)) {
                selector.setValue(to);
            }
        }
    }

    /**
     * We have acquired focus from the body, so let's deal with that
     */
    protected abstract void takeFocusAwayFromBody();

    /**
     * set the title based on the title of the current node
     *
     * @param name
     *            the new title
     * @param official
     *            is the title official
     */
    protected abstract void title(String name, boolean official);

    /**
     * the current has changed, so let's update body
     */
    protected abstract void updateBody();
}
