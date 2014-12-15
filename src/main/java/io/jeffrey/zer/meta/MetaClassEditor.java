package io.jeffrey.zer.meta;

import io.jeffrey.zer.Syncable;

import java.util.Map;
import java.util.Map.Entry;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Edit the meta class
 *
 * @author jeffrey
 *
 */
public class MetaClassEditor extends AbstractMapEditor<MetaClass> {
    private static final String DEFAULT_CLASS = "Default";
    private final VBox          core          = new VBox();
    private final VBox          parts         = new VBox();
    private final Stage         stage         = new Stage();

    /**
     * construct the editor
     * 
     * @param allClasses
     *            all the classes
     * @param currentClassId
     *            the class we are editing
     * @param parent
     *            the parent that needs to be notified when the value has been updated
     */
    public MetaClassEditor(final Map<String, MetaClass> allClasses, final String currentClassId, final Syncable parent) {
        super(allClasses, currentClassId, parent);

        updateBody();
        syncSelector();

        final HBox top = new HBox();
        final Text text = new Text("Meta Class Name:");
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
    protected MetaClass create(final String name) {
        return new MetaClass(newId(), name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MetaClass getNewDefault() {
        return new MetaClass(newId(), DEFAULT_CLASS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isDefault(final MetaClass thing) {
        return thing.name().equals(DEFAULT_CLASS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void takeFocusAwayFromBody() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void title(final String name, final boolean official) {
        stage.setTitle("Class:" + name + (official ? "*" : ""));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateBody() {
        parts.getChildren().clear();

        final GridPane existing = new GridPane();
        existing.setHgap(3);
        existing.setVgap(3);

        Text headerTxt = new Text("Existing Fields");
        headerTxt.setStyle("-fx-background-color: #ccccff; -fx-font: 15px Tahoma; -fx-fill: white;");
        parts.getChildren().add(headerTxt);
        parts.getChildren().add(existing);

        int row = 0;
        for (final Entry<String, String> e : current.fields.entrySet()) {
            final Label name = new Label(e.getKey());
            final Label def = new Label(e.getValue());
            final Button buttonDel = new Button("Del");
            buttonDel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent arg0) {
                    current.fields.remove(e.getKey());
                    checkAndOfficiate();
                    updateBody();
                }
            });
            existing.add(name, 0, row);
            existing.add(def, 1, row);
            existing.add(buttonDel, 2, row);
            row++;

        }

        headerTxt = new Text("Add New Field");
        headerTxt.setStyle("-fx-background-color: #ccccff; -fx-font: 15px Tahoma; -fx-fill: white;");
        existing.add(headerTxt, 0, row, 3, 1);
        row++;

        final TextField newName = new TextField();
        final TextField newDef = new TextField();
        final Button buttonAdd = new Button("Add");
        buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent arg0) {
                current.add(newName.getText(), newDef.getText());
                checkAndOfficiate();
                updateBody();
            }
        });

        existing.add(newName, 0, row);
        existing.add(newDef, 1, row);
        existing.add(buttonAdd, 2, row);

    }
}
