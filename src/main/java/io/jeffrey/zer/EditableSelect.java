package io.jeffrey.zer;

import java.util.Iterator;
import java.util.TreeSet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

/**
 * Handy dandy helper to create and bind controls to manipulate the currently
 * selected editable
 * 
 * @author jeffrey
 *
 */
public class EditableSelect {

	private final TreeSet<String> ids;
	private final Syncable parent;
	private final SurfaceData data;

	private String id = null;
	private String priorId = null;
	private String nextId = null;

	private Editable current;

	public EditableSelect(Syncable parent, SurfaceData data) {
		this.parent = parent;
		this.data = data;
		this.ids = new TreeSet<String>();
	}

	public Editable current() {
		return current;
	}

	private void set(String newId) {
		id = newId;
		updateCache();

		parent.sync();
	}

	private void updateCache() {
		String last = null;
		boolean keep = false;
		ids.clear();
		for (Editable edit : data.getEditables()) {
			ids.add(edit.id());
			if (edit.id().equals(id)) {
				current = edit;
			}
		}
		Iterator<String> it = ids.iterator();
		while (it.hasNext()) {
			String me = it.next();
			if (me.equals(id)) {
				priorId = last;
				keep = true;
				if (it.hasNext()) {
					nextId = it.next();
				}
			}
			last = me;
		}
		if (!keep) {
			id = null;
		}
	}

	public HBox create() {
		HBox hbox = new HBox();

		updateCache();

		if (data.getEditables().size() <= 1)
			return hbox;

		Button All = new Button("*");
		All.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent dontcare) {
				set(null);
			}
		});
		hbox.getChildren().add(All);

		if (priorId != null) {
			Button Previous = new Button(priorId);
			Previous.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent dontcare) {
					set(priorId);
				}
			});
			hbox.getChildren().add(Previous);
		}

		{
			ComboBox<String> Select = new ComboBox<String>();
			Select.itemsProperty().get().addAll(ids);
			if (id != null) {
				Select.valueProperty().set(id);
			} else {
				Select.valueProperty().set("*");
			}
			Select.valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(
						final ObservableValue<? extends String> val,
						final String before, final String after) {
					id = after;
					parent.sync();
				}
			});
			hbox.getChildren().add(Select);
		}

		if (nextId != null) {
			Button Next = new Button(nextId);
			Next.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent dontcare) {
					set(nextId);
				}
			});
			hbox.getChildren().add(Next);
		}
		return hbox;
	}
}
