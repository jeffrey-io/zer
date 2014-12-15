package io.jeffrey.zer.meta;

import io.jeffrey.zer.Syncable;

import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Edit the layer properties
 *
 * @author jeffrey
 */
public class LayerPropertiesEditor extends AbstractMapEditor<LayerProperties> {
    private static final String DEFAULT_LAYER = "Foreground";

    private final EditBinding   binding;
    private final VBox          core          = new VBox();
    private final VBox          parts         = new VBox();
    private final Stage         stage         = new Stage();

    /**
     * construct the editor
     * 
     * @param allLayers
     *            all the layers
     * @param currentLayerId
     *            the layer we are editing
     * @param parent
     *            the parent that needs to be notified when the value has been updated
     */
    public LayerPropertiesEditor(final Map<String, LayerProperties> allLayers, final String currentLayerId, final Syncable parent) {
        super(allLayers, currentLayerId, parent);

        binding = new EditBinding(core, new Syncable() {
            @Override
            public void sync() {
                checkAndOfficiate();
            }
        });
        updateBody();
        syncSelector();

        final HBox top = new HBox();
        final Text text = new Text("Layer Name:");
        top.setStyle("-fx-background-color: #ccccff;");
        top.getChildren().addAll(text, selector);
        HBox.setMargin(text, new Insets(10));
        HBox.setMargin(selector, new Insets(10));

        core.getChildren().add(top);
        core.getChildren().add(parts);

        final Scene scene = new Scene(core, 400, 600);
        stage.setScene(scene);
        stage.show();
        postConstruct();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LayerProperties create(final String name) {
        return new LayerProperties(newId(), name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LayerProperties getNewDefault() {
        return create(DEFAULT_LAYER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isDefault(final LayerProperties thing) {
        return thing.name().equals(DEFAULT_LAYER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void takeFocusAwayFromBody() {
        binding.resetFocus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void title(final String name, final boolean official) {
        stage.setTitle("Layer:" + name + (official ? "*" : ""));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateBody() {

        parts.getChildren().clear();

        final EditBinding binding = new EditBinding(parts, this);
        final SurfaceItemEditorBuilderImpl builder = new SurfaceItemEditorBuilderImpl(parts, binding);

        builder.startBorder("Ordering");
        builder.startFourColumnGrid().add("Z Order", current.zorder);
        builder.endBorder();

        builder.startBorder("Grid");
        builder.startFourColumnGrid().add("Major", current.gridMajor, "Minor", current.gridMinor);
        builder.endBorder();

        builder.startBorder("Snapping");
        builder.addBoolean("Snap Major", current.snapMajor);
        builder.addBoolean("Snap Minor", current.snapMinor);
        builder.endBorder();

        binding.focus();
    }
}
