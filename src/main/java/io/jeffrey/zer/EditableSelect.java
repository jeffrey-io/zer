package io.jeffrey.zer;

import java.util.Iterator;
import java.util.TreeSet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

/**
 * Handy dandy helper to create and bind controls to manipulate the currently selected editable
 *
 * @author jeffrey
 *
 */
public class EditableSelect {
    private Editable              current;
    private final SurfaceData     data;
    private String                id      = null;
    private final TreeSet<String> ids;
    private String                nextId  = null;
    private final Syncable        parent;
    private String                priorId = null;

    /**
     * @param parent the parent that needs to be updated once a selection is made
     * @param data what we are selecting from
     */
    public EditableSelect(final Syncable parent, final SurfaceData data) {
        this.parent = parent;
        this.data = data;
        ids = new TreeSet<String>();
    }

    /**
     * @return a box that contains the controls to pick an editable
     */
    public HBox createLinkedControlBox() {
        final HBox hbox = new HBox();

        updateCache();

        if (data.getEditables().size() <= 1) {
            return hbox;
        }

        final Button All = new Button("*");
        All.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent dontcare) {
                set(null);
            }
        });
        hbox.getChildren().add(All);

        if (priorId != null) {
            final Button Previous = new Button(priorId);
            Previous.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent dontcare) {
                    set(priorId);
                }
            });
            hbox.getChildren().add(Previous);
        }

        {
            final ComboBox<String> Select = new ComboBox<String>();
            Select.itemsProperty().get().addAll(ids);
            if (id != null) {
                Select.valueProperty().set(id);
            } else {
                Select.valueProperty().set("*");
            }
            Select.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(final ObservableValue<? extends String> val, final String before, final String after) {
                    id = after;
                    parent.sync();
                }
            });
            hbox.getChildren().add(Select);
        }

        if (nextId != null) {
            final Button Next = new Button(nextId);
            Next.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent dontcare) {
                    set(nextId);
                }
            });
            hbox.getChildren().add(Next);
        }
        return hbox;
    }

    /**
     * @return the currently selected editable
     */
    public Editable current() {
        return current;
    }

    /**
     * helper: select the item based on the id 
     * @param newId the new editable id to select
     */
    private void set(final String newId) {
        id = newId;
        updateCache();
        parent.sync();
    }

    /**
     * update the cache of what is the current editable, and the ids for the prior and next
     */
    private void updateCache() {
        String last = null;
        boolean keep = false;
        ids.clear();
        for (final Editable edit : data.getEditables()) {
            ids.add(edit.id());
            if (edit.id().equals(id)) {
                current = edit;
            }
        }
        final Iterator<String> it = ids.iterator();
        while (it.hasNext()) {
            final String me = it.next();
            if (me.equals(id)) {
                priorId = last;
                keep = true;
                if (it.hasNext()) {
                    nextId = it.next();
                }
            }
            last = me;
        }
        if (!keep) {
            id = null;
        }
    }
}
