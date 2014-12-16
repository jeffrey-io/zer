package io.jeffrey.zer;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * The place where all notifications/events and other interesting exceptions go; this could have been done with log4j, but I would rather have explicit control
 *
 * @author jeffrey
 *
 */
public class Notifications {

    private final ArrayList<Runnable>     events;
    private final ArrayList<Notification> history;

    /**
     * create an empty set of notifications
     */
    public Notifications() {
        this.events = new ArrayList<>();
        this.history = new ArrayList<Notifications.Notification>();
    }

    /**
     * a notification structure
     * 
     * @author jeffrey
     */
    public static class Notification {
        public final long   timestamp;
        public final String shortMessage;
        public final String longMessage;

        public Notification(String shortMessage, String longMessage) {
            this.timestamp = System.currentTimeMillis();
            this.shortMessage = shortMessage;
            this.longMessage = longMessage;
        }
    }

    /**
     * @return the latest notification
     */
    public Notification latest() {
        if (history.size() == 0)
            return null;
        return history.get(history.size() - 1);
    }

    /**
     * listen for new problems to happen
     * 
     * @param event
     *            the runnable to run once something new happens
     */
    public void listen(final Runnable event) {
        events.add(event);
        event.run();
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
        StringBuilder sb = new StringBuilder();
        for (final String p : parts) {
            sb.append(p);
        }

        String shortMessage = sb.toString();
        String longMessage = shortMessage;
        if (shortMessage.length() > 256) {
            shortMessage = shortMessage.substring(0, 253) + "...";
        }
        if (failure != null) {
            longMessage += "\n--------------------------\n";
            ByteArrayOutputStream mem = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(mem);
            failure.printStackTrace(pw);
            pw.flush();
            longMessage += new String(mem.toByteArray());
        }
        Notification notification = new Notification(shortMessage, longMessage);
        history.add(notification);
        fire();
    }

    /**
     * @param parts
     *            the message being shared
     */
    public void println(final String... parts) {
        println(null, parts);
    }
}
