package io.jeffrey.zer;

import io.jeffrey.zer.SurfaceData.SurfaceAction;

import java.io.File;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * These are helper functions to simplify how the view is constructed in {@code WorldStage}
 *
 * @author jeffrey
 *
 */
public class SurfaceLinkageToStage {

    private static MenuItem actionOf(final String label, final SurfaceAction action, final Surface surface, final SurfaceData data, final Stage stage, final Syncable syncable, final SyncableSet menuSync) {
        final MenuItem item = new MenuItem(label);
        item.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent evt) {
                try {
                    data.execute(action, surface.cursor_x, surface.cursor_y);
                } catch (final Exception e) {
                    // TODO: show window
                    e.printStackTrace();
                }
                syncable.sync();
                surface.render();
            }
        });
        return item;
    }

    /**
     * create the menu bar
     *
     * @param data
     * @return
     */
    public static VBox createLinkedMenuBar(final Surface surface, final SurfaceData data, final Stage stage, final Syncable syncable, final SyncableSet menuSync) {
        final VBox top = new VBox();
        final MenuBar menuBar = new MenuBar();
        final Menu top_file = new Menu("File");
        final MenuItem _new = new MenuItem("New...");
        final MenuItem _open = new MenuItem("Open");
        final MenuItem _save = new MenuItem("Save");
        final MenuItem _saveAs = new MenuItem("Save As...");
        final MenuItem _close = new MenuItem("Close");

        final Menu top_edit = new Menu("Edit");

        final MenuItem _del = actionOf("Delete", SurfaceAction.DeleteSelection, surface, data, stage, syncable, menuSync);

        final MenuItem _all = actionOf("Select All", SurfaceAction.SelectAll, surface, data, stage, syncable, menuSync);
        final MenuItem _inv = actionOf("Inverse Selection", SurfaceAction.InverseSelection, surface, data, stage, syncable, menuSync);

        final MenuItem _copy = actionOf("Copy", SurfaceAction.Copy, surface, data, stage, syncable, menuSync);
        final MenuItem _cut = actionOf("Cut", SurfaceAction.Cut, surface, data, stage, syncable, menuSync);
        final MenuItem _paste = actionOf("Paste", SurfaceAction.Paste, surface, data, stage, syncable, menuSync);
        final MenuItem _undo = actionOf("Undo", SurfaceAction.Undo, surface, data, stage, syncable, menuSync);
        final MenuItem _redo = actionOf("Redo", SurfaceAction.Redo, surface, data, stage, syncable, menuSync);

        _del.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));

        _all.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
        _inv.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));

        _copy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        _cut.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        _paste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));

        _undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        _redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));

        _save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        _new.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        _open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

        menuSync.add(new Syncable() {

            @Override
            public void sync() {
                _copy.setDisable(!data.available(SurfaceAction.Copy));
                _cut.setDisable(!data.available(SurfaceAction.Cut));
                _paste.setDisable(!data.available(SurfaceAction.Paste));

                _undo.setDisable(!data.available(SurfaceAction.Undo));
                _redo.setDisable(!data.available(SurfaceAction.Redo));
            }
        });

        top_edit.getItems().addAll(_del, _all, _inv, _copy, _cut, _paste, _undo, _redo);

        final Menu top_view = new Menu("View");
        final MenuItem _reset_camera = new MenuItem("Reset Camera");

        _reset_camera.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent arg0) {
                surface.resetCamera();
                syncable.sync();
                surface.render();
            }
        });

        _new.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent arg0) {
                try {
                    data.execute(SurfaceAction.NewFile, surface.cursor_x, surface.cursor_y);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });

        _open.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent arg0) {
                final FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Surface");
                final File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    data.actionOpen(file);
                }
                surface.render();
                syncable.sync();
            }
        });

        final EventHandler<ActionEvent> saveAsEvent = new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent arg0) {
                final FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Surface As");
                final File file = fileChooser.showSaveDialog(stage);
                // get name
                if (data.setFile(file)) {
                    stage.setTitle(file.getName());
                    try {
                        data.execute(SurfaceAction.Save, surface.cursor_x, surface.cursor_y);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                    return;
                } else {
                    // TODO: show error message
                }
            }
        };

        _save.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent arg0) {
                if (!data.available(SurfaceAction.Save)) {
                    saveAsEvent.handle(arg0);
                } else {
                    try {
                        data.execute(SurfaceAction.Save, surface.cursor_x, surface.cursor_y);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        _saveAs.setOnAction(saveAsEvent);

        _close.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent arg0) {
                stage.hide();
            }
        });

        top_file.getItems().addAll(_new, _open, _save, _saveAs, _close);
        top_view.getItems().addAll(_reset_camera);

        final MenuItem tb = new MenuItem("tb");

        final ToolBar tba = new ToolBar(new Text("foo"));

        menuBar.getMenus().addAll(top_file, top_edit, top_view);

        top.getChildren().add(menuBar);

        tb.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent arg0) {
                if (top.getChildren().contains(tba)) {
                    top.getChildren().remove(tba);

                } else {
                    top.getChildren().add(tba);
                }

            }
        });
        return top;
    }

    /**
     * Connect the mouse events from the canvas to the surface
     *
     * @param canvas
     *            where events are produced
     * @param camera
     *            where the user is looking at
     * @param surface
     *            where events are sent
     */
    public static void linkCanvasToSurface(final Canvas canvas, final Camera camera, final Surface surface, final Syncable syncable) {
        final HashMap<MouseButton, MouseInteraction> interactions = new HashMap<>();
        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent evt) {
                final MouseInteraction interaction = surface.startInteraction(evt);
                interactions.put(evt.getButton(), interaction);
                surface.render();
            }
        });
        final EventHandler<MouseEvent> movement = new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent evt) {
                boolean update = false;
                for (final MouseInteraction interaction : interactions.values()) {
                    if (interaction != null) {
                        interaction.moved(new AdjustedMouseEvent(camera, evt));
                        update = true;
                    }
                }
                if (update) {
                    syncable.sync();
                    surface.render();
                }
            }
        };
        canvas.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(final ScrollEvent scroll) {
                if (Math.abs(scroll.getDeltaY()) < 10) {
                    return;
                }
                final double scaleAfter = Math.min(Math.pow(2, 8), Math.max(1 / Math.pow(2, 8), camera.scale * (scroll.getDeltaY() > 0 ? 1.1 : 0.9)));
                camera.tX = scroll.getX() - (scroll.getX() - camera.tX) / camera.scale * scaleAfter;
                camera.tY = scroll.getY() - (scroll.getY() - camera.tY) / camera.scale * scaleAfter;
                camera.scale = scaleAfter;
                syncable.sync();
                surface.render();
            }
        });
        canvas.setOnMouseDragged(movement);
        canvas.setOnMouseMoved(movement);
        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent evt) {
                final MouseInteraction interaction = interactions.get(evt.getButton());
                if (interaction != null) {
                    interaction.moved(new AdjustedMouseEvent(camera, evt));
                    interaction.commit();
                    syncable.sync();
                    surface.render();
                    interactions.put(evt.getButton(), null);
                }
            }
        });
    }
}
