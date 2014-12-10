package io.jeffrey.zer;

import io.jeffrey.zer.meta.SurfaceItemEditor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

        final Text status = new Text("Status Bar? Yes?");

        final Canvas canvas = new Canvas(300, 250);
        final Camera camera = data.getCamera();
        final Surface surface = new Surface(canvas, data, status);

        final VBox left = new VBox();
        final VBox right = new VBox();

        root.setLeft(left);
        root.setRight(right);

        final SyncableSet syncs = new SyncableSet();
        final SyncableSet menuSync = new SyncableSet();

        final SurfaceItemEditor editor = new SurfaceItemEditor(left, data, surface, syncs);
        final ActionBar actions = new ActionBar(right, data, surface, syncs);
        syncs.add(editor);
        syncs.add(actions);
        syncs.add(menuSync);

        root.setTop(SurfaceLinkageToStage.createLinkedMenuBar(surface, data, stage, syncs, menuSync));

        final Pane center = new Pane();
        root.setCenter(center);
        root.setBottom(status);

        center.getChildren().add(canvas);

        SurfaceLinkageToStage.linkCanvasToSurface(canvas, camera, surface, syncs);

        editor.sync();

        surface.render();

        final Scene scene = new Scene(root, 900, 950);
        stage.setTitle("Compose Yourself");
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
