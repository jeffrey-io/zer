package io.jeffrey.zer;

import io.jeffrey.zer.meta.SurfaceItemEditor;
import io.jeffrey.zer.plugin.Plugin;

import java.io.File;
import java.util.HashMap;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The stage which is the actual window shown to the user
 *
 * @author jeffrey
 */
public class ZERStage {

    public double lastLeftWidth = 0.0;

    /**
     * @param data
     *            what defines the document we are editing
     * @param stage
     *            the actual window
     */
    public ZERStage(final SurfaceData data, final Stage stage) {
        final BorderPane root = new BorderPane();

        // TODO: link status to something... else
        final Text status = new Text("TODO: put something useful here");

        final Canvas canvas = new Canvas(300, 250);
        final Camera camera = data.getCamera();
        final Surface surface = new Surface(canvas, data, status);

        final VBox left = new VBox();
        final VBox right = new VBox();

        root.setLeft(left);
        root.setRight(right);

        final SyncableSet syncs = new SyncableSet();
        final SyncableSet menuSync = new SyncableSet();
        final HashMap<String, Plugin> plugins = new HashMap<>();

        final File pluginRoot = data.getPluginRoot();
        if (pluginRoot.exists() && pluginRoot.isDirectory()) {
            for (final File file : pluginRoot.listFiles()) {
                if (file.getName().endsWith(".js")) {
                    try {
                        plugins.put(file.getName(), new Plugin(file.getPath(), data.getModel()));
                    } catch (final Exception err) {
                        // notify user
                    }
                }
            }
        } else {
            // notify user
        }

        // when plugins changed, we integrate the changes
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent dontcare) {
                boolean updated = false;
                for (final Plugin plugin : plugins.values()) {
                    try {
                        if (plugin.ping()) {
                            updated = true;
                        }
                    } catch (final Exception e) {
                        // TODO: notify, or remove the plugin
                    }
                }
                if (updated) {
                    // notify that plugins have been re-loaded
                    menuSync.sync();
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        final SurfaceItemEditor editor = new SurfaceItemEditor(left, data, surface, syncs);
        final ActionBar actions = new ActionBar(right, data, surface, plugins, syncs);
        syncs.add(editor);
        syncs.add(actions);
        syncs.add(menuSync);

        root.setTop(SurfaceLinkageToStage.createLinkedMenuBar(surface, data, stage, syncs, menuSync, plugins));

        final Pane center = new Pane();
        root.setCenter(center);
        root.setBottom(status);

        center.getChildren().add(canvas);

        SurfaceLinkageToStage.linkCanvasToSurface(canvas, camera, surface, syncs);

        editor.sync();

        surface.render();

        final Scene scene = new Scene(root, 900, 950);
        stage.setTitle(data.getTitle());
        stage.setScene(scene);
        center.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(final ObservableValue<? extends Number> observableValue, final Number oldSceneWidth, final Number newSceneWidth) {
                final double leftWidth = left.getWidth();
                final double change = leftWidth - lastLeftWidth;
                camera.tX -= change;
                lastLeftWidth = leftWidth;
                canvas.setWidth(newSceneWidth.doubleValue());
                syncs.sync();
                surface.render();
            }
        });
        center.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(final ObservableValue<? extends Number> observableValue, final Number oldSceneHeight, final Number newSceneHeight) {
                canvas.setHeight(newSceneHeight.doubleValue());
                syncs.sync();
                surface.render();
            }
        });
        stage.show();
    }
}
