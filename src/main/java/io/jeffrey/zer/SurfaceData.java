package io.jeffrey.zer;

import io.jeffrey.zer.meta.LayerProperties;
import io.jeffrey.zer.meta.MetaClass;
import io.jeffrey.zer.plugin.Model;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.canvas.GraphicsContext;

/**
 * Methods to adapt the surface global events and state to the actual surface
 *
 * @author jeffrey
 */
public abstract class SurfaceData {

    /**
     * action that surfaces can execute
     *
     * @author jeffrey
     */
    public static enum SurfaceAction {
        Copy, Cut, DeleteSelection, InverseSelection, NewFile, Paste, Redo, Save, SelectAll, Undo, ZoomAll, ZoomSelection
    }

    /**
     * indicate that we should open
     *
     * @param file
     *            the file that we should open
     */
    public abstract void actionOpen(File file);

    /**
     * add something
     *
     * @param type
     *            of the given type
     * @param context
     *            the surface context for the request
     */
    public abstract void add(String type, SurfaceContext context);

    /**
     * @param action
     *            the action we wish to be able to execute
     * @return if the given action is available
     */
    public abstract boolean available(SurfaceAction action);

    /**
     * indicate that we have finished doing some work on the editables
     */
    public abstract void capture();

    /**
     * draw the surface
     *
     * @param gc
     *            the graphis context to draw
     * @param context
     *            the surface context for the request
     */
    public abstract void draw(GraphicsContext gc, SurfaceContext context);

    /**
     * Execute the given action that has no arguments
     *
     * @param action
     *            the action to execute
     * @param context
     *            the surface context for the request
     * @throws Exception
     *             we (for some reason) were unable to execute the request
     */
    public abstract void execute(SurfaceAction action, SurfaceContext context) throws Exception;

    /**
     * @return the active layer for the first selected item
     */
    public abstract LayerProperties getActiveLayer();

    /**
     * @return a list of all possible things that are addable
     */
    public abstract List<String> getAddables();

    /**
     * @return the camera
     */
    public abstract Camera getCamera();

    /**
     * @return a set of all the things that can be edited
     */
    public abstract Set<Editable> getEditables();

    /**
     * @return the method for how to resolve icons
     */
    public abstract IconResolver getIconResolver();

    /**
     * @return all layers
     */
    public abstract Map<String, LayerProperties> getLayers();

    /**
     * @return all available meta classes
     */
    public abstract Map<String, MetaClass> getMetaClasses();

    /**
     * @return a model for scripts to manipulate the data
     */
    public abstract Model getModel();

    /**
     * link notifications
     *
     * @param notify
     *            where notifications should be routed
     */
    public abstract Notifications getNotifications();

    /**
     * @return where to look for plugins
     */
    public abstract File getPluginRoot();

    /**
     * Since the child owns what items are selected, this function is responsible for ensuring that the if we are going to drag things that are already selected, then they to need to be returned here
     *
     * @param event
     *            where we begin our query for the things to manipulate that are selection
     * @return a set of all ways the selection can be manipulated
     */
    public abstract MouseInteraction getSelectionMovers(AdjustedMouseEvent event);

    /**
     * @return the current title
     */
    public abstract String getTitle();

    /**
     * indicate that a selection window has been initiated
     */
    public abstract void initiateSelectionWindow();

    /**
     * Test whether or not the given event is in a selected item
     *
     * @param event
     *            the mouse event that is transformed into world space
     * @return true if the point lies in a selection region
     */
    public abstract boolean isInSelectionSet(final AdjustedMouseEvent event);

    /**
     * indicate that we are about to do work on the stuff
     */
    public abstract void ready();

    /**
     * grant the surface a file
     *
     * @param file
     *            the file for the surface
     * @return true if the name is valid
     */
    public abstract boolean setFile(File file);

    /**
     * begin a mouse interaction in the world space
     *
     * @param event
     *            the mouse event that has been transformed into the surface's world space
     * @param context
     *            the surface context for the request
     * @return a mouse interaction
     */
    public abstract MouseInteraction startSurfaceInteraction(final AdjustedMouseEvent event, SurfaceContext context);

    /**
     * force the selection window over all the details
     *
     * @param window
     *            the current selection window which just updated, so please select items based on it
     */
    public abstract void updateSelectionWindow(SelectionWindow window);
}
