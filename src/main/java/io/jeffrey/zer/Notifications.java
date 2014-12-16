package io.jeffrey.zer;

import java.util.ArrayList;

/**
 * The place where all notifications/events and other interesting exceptions go; this could have been done with log4j, but I would rather have explicit control
 *
 * @author jeffrey
 *
 */
public class Notifications {

    private final ArrayList<Runnable> events;

    /**
     * create an empty set of notifications
     */
    public Notifications() {
        events = new ArrayList<>();
    }

    /**
     * listen for new problems to happen
     * 
     * @param event
     *            the runnable to run once something new happens
     */
    public void listen(final Runnable event) {
        events.add(event);
    }

    /**
     * Helper: tell everyone listening to update
     */
    private void fire() {
        for (final Runnable event : events) {
            event.run();
        }
    }

    /**
     * @param failure
     *            the exception we are interested in
     * @param parts
     *            the message being shared
     */
    public void println(final Exception failure, final String... parts) {
        println(parts);
    }

    /**
     * @param parts
     *            the message being shared
     */
    public void println(final String... parts) {
        for (final String p : parts) {
            System.err.print(p);
        }
        System.err.println();
        fire();
    }
}
