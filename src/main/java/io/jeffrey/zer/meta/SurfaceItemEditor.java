package io.jeffrey.zer.meta;

import io.jeffrey.zer.Editable;
import io.jeffrey.zer.Notifications;
import io.jeffrey.zer.Surface;
import io.jeffrey.zer.SurfaceData;
import io.jeffrey.zer.Syncable;

import java.util.Set;

import javafx.scene.layout.VBox;

/**
 * defines how a selected item is edited
 */
public class SurfaceItemEditor implements Syncable {

    private final EditBinding binding;
    private final SurfaceData data;
    private boolean           skip = false;
    private final Surface     surface;
    private final Syncable    syncable;
    private final VBox        vbox;

    /**
     * @param vbox
     *            where we render the items
     * @param data
     *            the data to connect with
     * @param surface
     *            the surace to render
     * @param syncable
     *            how we update other views
     */
    public SurfaceItemEditor(final VBox vbox, final SurfaceData data, final Surface surface, final Syncable syncable, final Notifications notify) {
        this.vbox = vbox;
        this.data = data;
        this.surface = surface;
        this.syncable = syncable;
        binding = new EditBinding(vbox, new Syncable() {

            @Override
            public void sync() {
                refresh();
            }
        }, notify);

    }

    /**
     * update the view from a value change
     */
    private void refresh() {
        skip = true;
        try {
            syncable.sync();
        } finally {
            skip = false;
        }
        surface.render();
    }

    /**
     * Update the panel
     */
    @Override
    public void sync() {
        if (skip) {
            return;
        }
        final Set<Editable> edits = data.getEditables();
        final SurfaceItemEditorBuilderImpl builder = new SurfaceItemEditorBuilderImpl(vbox, binding);
        boolean empty = true;
        for (final Editable editable : edits) {
            empty = false;
            editable.createEditor(data, builder, this);
        }
        if (empty) {
            binding.resetFocus();
        }
        binding.focus();
    }
}
