package io.jeffrey.zer;

import java.util.Iterator;
import java.util.Set;
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

	public EditableSelect(Syncable parent) {
		this.parent = parent;
	}

	public Editable current() {
		return current;
	}

	public HBox create(Set<Editable> edits) {
		HBox hbox = new HBox();

		if (edits.size() == 1) {
			current = edits.iterator().next();
			selectedId = current.id();
		}
		
		if (edits.size() <= 1)
			return hbox;

		TreeSet<String> ids = new TreeSet<String>();
		for (Editable edit : edits) {
			ids.add(edit.id());
			if (edit.id().equals(selectedId)) {
				current = edit;
			}
		}

		String officialLast = null;
		String officialNext = null;
		String last = null;
		boolean keep = false;

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
		// TODO: create control to show a '*', '<', dropdown, '>'

		Button All = new Button("*");
		All.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				selectedId = null;
				parent.sync();
			}
		});
		hbox.getChildren().add(All);

		if (officialLast != null) {
			final String oLast = officialLast;
			Button Previous = new Button(oLast);
			Previous.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					selectedId = oLast;
					parent.sync();
				}
			});
			hbox.getChildren().add(Previous);
		}

		ComboBox<String> Select = new ComboBox<String>();
		Select.itemsProperty().get().addAll(ids);
		if (selectedId != null) {
			Select.valueProperty().set(selectedId);
		} else {
			Select.valueProperty().set("*");
		}
		Select.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> val,
					final String before, final String after) {
				selectedId = after;
				parent.sync();
			}
		});
		hbox.getChildren().add(Select);

		if (officialNext != null) {
			Button Next = new Button(officialNext);
			String oNext = officialNext;
			Next.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					selectedId = oNext;
					parent.sync();
				}
			});
			hbox.getChildren().add(Next);
		}
		return hbox;
	}
}
