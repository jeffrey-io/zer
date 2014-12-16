package io.jeffrey.zer;

import io.jeffrey.zer.Notifications.Notification;
import io.jeffrey.zer.meta.SurfaceItemEditor;
import io.jeffrey.zer.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
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
        final Notifications notify = data.getNotifications();
        final BorderPane root = new BorderPane();

        final Text status = new Text("");

        notify.listen(new Runnable() {
            @Override
            public void run() {
                Notification latest = notify.latest();
                if (latest != null) {
                    status.setText(latest.shortMessage);
                }
            }
        });
        
        status.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent dontcare) {
                // pop up window of events
            }
        });

        final Canvas canvas = new Canvas(300, 250);
        final Camera camera = data.getCamera();
        final Surface surface = new Surface(canvas, data);

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
                        notify.println("loading plugin:", file.toString());
                        plugins.put(file.getName(), new Plugin(file.getPath(), data.getModel(), notify));
                    } catch (final Exception failure) {
                        notify.println(failure, "unable to load:", file.toString());
                    }
                }
            }
        } else {
            notify.println("plug in directory '", pluginRoot.toString(), "' does not exist");
        }

        // when plugins changed, we integrate the changes
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent dontcare) {
                boolean updated = false;
                final HashSet<String> toAxe = new HashSet<String>();
                for (final Entry<String, Plugin> entry : plugins.entrySet()) {
                    final Plugin plugin = entry.getValue();
                    try {
                        if (plugin.ping()) {
                            updated = true;
                        }
                    } catch (final Exception failure) {
                        notify.println(failure, "unable to ping plugin: ", entry.getKey());
                        toAxe.add(entry.getKey());
                    }
                    if (!plugin.exists()) {
                        toAxe.add(entry.getKey());
                    }
                }
                for (final String ax : toAxe) {
                    plugins.remove(ax);
                }
                if (pluginRoot.exists() && pluginRoot.isDirectory()) {
                    for (final File file : pluginRoot.listFiles()) {
                        if (file.getName().endsWith(".js")) {
                            if (!plugins.containsKey(file.getName())) {
                                try {
                                    notify.println("loading plugin:", file.toString());
                                    plugins.put(file.getName(), new Plugin(file.getPath(), data.getModel(), notify));
                                    updated = true;
                                } catch (final Exception failure) {
                                    notify.println(failure, "unable to load:", file.toString());
                                }
                            }
                        }
                    }
                }
                if (updated) {
                    menuSync.sync();
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        final SurfaceItemEditor editor = new SurfaceItemEditor(left, data, surface, syncs, notify);
        final ActionBar actions = new ActionBar(right, data, surface, plugins, syncs);
        syncs.add(editor);
        syncs.add(actions);
        syncs.add(menuSync);

        root.setTop(SurfaceLinkageToStage.createLinkedMenuBar(surface, data, stage, syncs, menuSync, plugins, notify));

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
