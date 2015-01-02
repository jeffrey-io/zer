package io.jeffrey.zer;

import javafx.scene.image.Image;

/**
 * Defines how zer will lookup images for the action bar
 *
 * @author jeffrey
 *
 */
public interface IconResolver {
    /**
     * the type of an icon
     * 
     * @author jeffrey
     *
     */
    public static enum IconType {
        Action, Addable, Plugin
    }

    /**
     * lookup an image
     * 
     * @param type
     *            the type of the image
     * @param name
     *            the name of the image
     * @return an Image (null if it doesn't exist)
     */
    public Image get(IconType type, String name);
}
