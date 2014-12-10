package io.jeffrey.zer.meta;

import io.jeffrey.zer.Syncable;
import io.jeffrey.zer.edits.Edit;

import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * This makes it easy to enforce consistent building of components for the surface editor
 *
 * @author jeffrey
 *
 */
public class SurfaceItemEditorBuilderImpl implements SurfaceItemEditorBuilder {

    /**
     * This defines a grid where rows can be added simply with four columns
     *
     * @author jeffrey
     *
     */
    public class SurfaceFourColumnGridImpl implements SurfaceFourColumnGrid {
        private final GridPane grid;
        private int            row;

        /**
         * the grid pane that was added to hold these elements
         *
         * @param grid
         */
        private SurfaceFourColumnGridImpl(final GridPane grid) {
            this.grid = grid;
            row = 0;
        }

        /**
         * add an element with a label and a value that spaces 3 columns
         */
        @Override
        public void add(final String label, final Edit value) {
            final Text txt = new Text(label);
            final TextField field = new TextField();
            binding.bindTextField(field, value);
            grid.add(txt, 0, row);
            grid.add(field, 1, row, 3, 1);
            row++;
        }

        /**
         * add an element with label, value, label, value for things like x,y coordinates
         */
        @Override
        public void add(final String label0, final Edit value0, final String label1, final Edit value1) {

            final Text txt0 = new Text(label0);
            final TextField edit0 = new TextField();
            binding.bindTextField(edit0, value0);

            final Text txt1 = new Text(label1);
            final TextField edit1 = new TextField();
            binding.bindTextField(edit1, value1);

            grid.add(txt0, 0, row);
            grid.add(edit0, 1, row);
            grid.add(txt1, 2, row);
            grid.add(edit1, 3, row);
            row++;
        }

        /**
         * add an element with two labels
         */
        @Override
        public void add(final String label, final String value) {
            final Text txt = new Text(label);
            final TextField field = new TextField(value);
            field.setEditable(false);
            grid.add(txt, 0, row);
            grid.add(field, 1, row, 3, 1);
            row++;
        }

    }

    private static final int  PAD = 2;

    private final EditBinding binding;
    private VBox              currentBox;
    private final VBox        rootBox;

    /**
     * @param rootBox
     *            the root box that will be cleared and where elements will be added
     * @param binding
     *            how data gets bound
     */
    public SurfaceItemEditorBuilderImpl(final VBox rootBox, final EditBinding binding) {
        this.rootBox = rootBox;
        currentBox = rootBox;
        this.binding = binding;
        rootBox.getChildren().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addBoolean(final String label, final Edit link) {
        final CheckBox box = new CheckBox(label);
        binding.bindBoolean(box, link);
        currentBox.getChildren().add(box);
        return box.isSelected();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String addColor(final String label, final Edit link) {
        final GridPane pane = new GridPane();

        final Text labelX = new Text(label);
        pane.add(labelX, 0, 0);

        final ColorPicker colorPicker = new ColorPicker();
        binding.bindColor(colorPicker, link);
        pane.add(colorPicker, 1, 0);

        currentBox.getChildren().add(pane);
        return link.getAsText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends AbstractMapEditorItemRequirements, E extends AbstractMapEditor<T>> T addCombo(final boolean canEdit, final String label, final Edit link, final Map<String, T> values, final Class<E> clazz, final Syncable parent) {
        final HBox position = new HBox();
        final ComboBox<T> dropdown = new ComboBox<>();
        binding.bindComboBox(dropdown, link, values, parent);
        final Text labelX = new Text(label);

        if (canEdit) {
            final Button buttonEditLayer = new Button("...");
            buttonEditLayer.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent arg0) {
                    try {
                        clazz.getConstructors()[0].newInstance(values, link.getAsText(), parent);
                    } catch (final Exception e) {
                    }
                }
            });
            position.getChildren().addAll(labelX, dropdown, buttonEditLayer);
        } else {
            position.getChildren().addAll(labelX, dropdown);
        }
        currentBox.getChildren().add(position);
        return dropdown.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String addFile(final String title, final String label, final Edit link, final FileSerializer normalize) {
        final GridPane pane = new GridPane();

        final Text labelX = new Text(label);
        pane.add(labelX, 0, 0);

        final TextField uri = new TextField();
        binding.bindTextField(uri, link);
        pane.add(uri, 1, 0);

        final Button choose = new Button("...");
        binding.bindFile(title, choose, uri, link, normalize);
        pane.add(choose, 2, 0);

        currentBox.getChildren().add(pane);

        return link.getAsText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endBorder() {
        currentBox = rootBox;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startBorder(final String label) {
        final VBox childBox = new VBox();
        final Text labelTxt = new Text(label);

        final HBox labelbox = new HBox();

        labelbox.setStyle("-fx-background-color: #ccccff; -fx-font: 15px Tahoma; -fx-fill: white;");
        labelbox.getChildren().add(labelTxt);

        childBox.setPadding(new Insets(PAD, PAD, PAD, PAD));

        currentBox.getChildren().add(labelbox);
        childBox.setStyle("-fx-border-color: black; -fx-border: 5px; -fx-border-radius: 5.0;");
        currentBox.getChildren().add(childBox);
        VBox.setMargin(childBox, new Insets(PAD, PAD, PAD, PAD));
        HBox.setMargin(labelbox, new Insets(PAD, PAD, PAD, PAD));
        currentBox = childBox;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SurfaceFourColumnGrid startFourColumnGrid() {
        final GridPane grid = new GridPane();
        grid.setHgap(PAD);
        grid.setVgap(PAD);
        currentBox.getChildren().add(grid);
        VBox.setMargin(grid, new Insets(PAD, PAD, PAD, PAD));
        return new SurfaceFourColumnGridImpl(grid);
    }

}
