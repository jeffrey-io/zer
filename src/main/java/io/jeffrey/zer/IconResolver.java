package io.jeffrey.zer;

import javafx.scene.image.Image;

public interface IconResolver {
	public static enum IconType {
		Plugin, Addable, Action
	}

	public Image get(IconType type, String name);
}
