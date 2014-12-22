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

public class EditableSelect {

	private final Syncable parent;
	private String selectedId = null;
	private Editable current;
	private final SurfaceData data;

	String officialLast = null;
	String officialNext = null;

	public EditableSelect(Syncable parent, SurfaceData data) {
		this.parent = parent;
		this.data = data;
	}

	public Editable current() {
		return current;
	}

	private void set(String id) {
		selectedId = id;
		updateCache();

		parent.sync();
	}

	TreeSet<String> ids = new TreeSet<String>();

	private void updateCache() {
		String last = null;
		boolean keep = false;
		ids.clear();
		for (Editable edit : data.getEditables()) {
			ids.add(edit.id());
			if (edit.id().equals(selectedId)) {
				current = edit;
			}
		}
		Iterator<String> it = ids.iterator();
		while (it.hasNext()) {
			String me = it.next();
			if (me.equals(selectedId)) {
				officialLast = last;
				keep = true;
				if (it.hasNext()) {
					officialNext = it.next();
				}
			}
			last = me;
		}

		if (!keep) {
			selectedId = null;
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
			public void handle(ActionEvent arg0) {
				set(null);
			}
		});
		hbox.getChildren().add(All);

		if (officialLast != null) {
			Button Previous = new Button(officialLast);
			Previous.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					set(officialLast);
				}
			});
			hbox.getChildren().add(Previous);
		}

		{
			ComboBox<String> Select = new ComboBox<String>();
			Select.itemsProperty().get().addAll(ids);
			if (selectedId != null) {
				Select.valueProperty().set(selectedId);
			} else {
				Select.valueProperty().set("*");
			}
			Select.valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(
						final ObservableValue<? extends String> val,
						final String before, final String after) {
					selectedId = after;
					parent.sync();
				}
			});
			hbox.getChildren().add(Select);
		}

		if (officialNext != null) {
			Button Next = new Button(officialNext);
			Next.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					set(officialNext);
				}
			});
			hbox.getChildren().add(Next);
		}
		return hbox;
	}
}
