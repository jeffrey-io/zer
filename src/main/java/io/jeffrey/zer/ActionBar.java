package io.jeffrey.zer;

import io.jeffrey.zer.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
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
    private final SurfaceData             data;
    private final HashMap<String, Plugin> plugins;
    private final Surface                 surface;
    private final Syncable                syncable;
    private final VBox                    vbox;

    /**
     * @param vbox
     *            the host to the action bar
     * @param data
     *            the data that the surface shows
     * @param surface
     *            the surface that can be rendered
     * @param plugins
     *            the various plugins that are loaded
     * @param syncable
     *            what to update once an action has been performed
     * 
     */
    public ActionBar(final VBox vbox, final SurfaceData data, final Surface surface, final HashMap<String, Plugin> plugins, final Syncable syncable) {
        this.vbox = vbox;
        this.data = data;
        this.surface = surface;
        this.plugins = plugins;
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
            final ArrayList<String> commonActionables = new ArrayList<String>();
            for (final Editable editable : edits) {
                commonActionables.addAll(editable.getActions());
                break;
            }
            for (final Editable editable : edits) {
                final HashSet<String> actions = new HashSet<>(editable.getActions());
                final Iterator<String> it = commonActionables.iterator();
                while (it.hasNext()) {
                    if (!actions.contains(it.next())) {
                        it.remove();
                    }
                }
            }

            // compute the common elements by intersecting them all
            final ArrayList<String> commonPlugins = new ArrayList<String>();
            for (final Editable editable : edits) {
                for (final Entry<String, Plugin> namedPlugin : plugins.entrySet()) {
                    if (namedPlugin.getValue().isActionable(editable.id())) {
                        commonPlugins.add(namedPlugin.getKey());
                    }
                }
                break;
            }
            for (final Editable editable : edits) {
                final HashSet<String> actions = new HashSet<>();
                for (final Entry<String, Plugin> namedPlugin : plugins.entrySet()) {
                    if (namedPlugin.getValue().isActionable(editable.id())) {
                        actions.add(namedPlugin.getKey());
                    }
                }
                final Iterator<String> it = commonPlugins.iterator();
                while (it.hasNext()) {
                    if (!actions.contains(it.next())) {
                        it.remove();
                    }
                }
            }

            // we have some common actions, let's show them
            if (commonActionables.size() > 0 || commonPlugins.size() > 0) {
                children.add(new Text("All Selected"));
                for (final String action : commonActionables) {
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
                for (final String p : commonPlugins) {
                    final Button button = new Button(p);
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent arg0) {
                            for (final Editable editable : edits) {
                                final Plugin plug = plugins.get(p);
                                plug.evaluate(editable.id());
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
            for (final Entry<String, Plugin> namedPlugin : plugins.entrySet()) {
                if (namedPlugin.getValue().isActionable(editable.id())) {
                    final Button button = new Button(namedPlugin.getKey());
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent arg0) {
                            namedPlugin.getValue().evaluate(editable.id());
                            surface.render();
                            syncable.sync();
                        }
                    });
                    children.add(button);
                }
            }
        }
    }
}
