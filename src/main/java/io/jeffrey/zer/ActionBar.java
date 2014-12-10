package io.jeffrey.zer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Defines what actions are available for the given selection
 *
 * @author jeffrey
 */
public class ActionBar implements Syncable {
    private final SurfaceData data;
    private final Surface     surface;
    private final Syncable    syncable;
    private final VBox        vbox;

    /**
     * @param vbox
     *            the host to the action bar
     * @param data
     *            the data that the surface shows
     * @param surface
     *            the surface that can be rendered
     */
    public ActionBar(final VBox vbox, final SurfaceData data, final Surface surface, final Syncable syncable) {
        this.vbox = vbox;
        this.data = data;
        this.surface = surface;
        this.syncable = syncable;
    }

    /**
     * A change has occured in the selection, update the action bar
     */
    @Override
    public void sync() {
        final Set<Editable> edits = data.getEditables();
        vbox.getChildren().clear();
        final ObservableList<Node> children = vbox.getChildren();

        if (edits.size() == 0) {

            for (final String addable : data.getAddables()) {
                final Button button = new Button("Add " + addable);
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent arg0) {
                        data.add(addable, surface.cursor_x, surface.cursor_y);
                        surface.render();
                        syncable.sync();
                    }
                });
                children.add(button);
            }

            return;
        }
        // we have more than one thing
        if (edits.size() > 1) {

            // compute the common elements by intersecting them all
            final ArrayList<String> common = new ArrayList<String>();
            for (final Editable editable : edits) {
                common.addAll(editable.getActions());
                break;
            }
            for (final Editable editable : edits) {
                final HashSet<String> actions = new HashSet<>(editable.getActions());
                final Iterator<String> it = common.iterator();
                while (it.hasNext()) {
                    if (!actions.contains(it.next())) {
                        it.remove();
                    }
                }
            }

            // we have some common actions, let's show them
            if (common.size() > 0) {
                children.add(new Text("All Selected"));
                for (final String action : common) {
                    final Button button = new Button(action);
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent arg0) {
                            for (final Editable editable : edits) {
                                editable.perform(action);
                            }
                            surface.render();
                            syncable.sync();
                        }
                    });
                    children.add(button);
                }
            }
        }

        /**
         * for each specific thing, let's show the actions available
         */
        for (final Editable editable : edits) {
            children.add(new Text("ID:" + editable.id()));
            for (final String action : editable.getActions()) {
                final Button button = new Button(action);
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent arg0) {
                        editable.perform(action);
                        surface.render();
                        syncable.sync();
                    }
                });
                children.add(button);
            }
        }
    }
}
