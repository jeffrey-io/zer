package io.jeffrey.zer;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * The place where all notifications/events and other interesting exceptions go; this could have been done with log4j, but I would rather have explicit control
 *
 * @author jeffrey
 *
 */
public class Notifications {

    /**
     * a notification structure
     *
     * @author jeffrey
     */
    public static class Notification {
        public final String longMessage;
        public final String shortMessage;
        public final long   timestamp;

        public Notification(final String shortMessage, final String longMessage) {
            timestamp = System.currentTimeMillis();
            this.shortMessage = shortMessage;
            this.longMessage = longMessage;
        }
    }

    private final ArrayList<Runnable>     events;

    private final ArrayList<Notification> history;

    /**
     * create an empty set of notifications
     */
    public Notifications() {
        events = new ArrayList<>();
        history = new ArrayList<Notifications.Notification>();
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
     * @return the latest notification
     */
    public Notification latest() {
        if (history.size() == 0) {
            return null;
        }
        return history.get(history.size() - 1);
    }

    /**
     * @param count
     *            the number of notifications to return
     * @return a list of notifications
     */
    public List<Notification> list(final int count) {
        final ArrayList<Notification> result = new ArrayList<Notifications.Notification>();
        final int start = Math.max(0, history.size() - count);
        for (int k = start; k < history.size(); k++) {
            result.add(history.get(k));
        }
        return result;
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
     * @param failure
     *            the exception we are interested in
     * @param parts
     *            the message being shared
     */
    public void println(final Exception failure, final String... parts) {
        final StringBuilder sb = new StringBuilder();
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
            final ByteArrayOutputStream mem = new ByteArrayOutputStream();
            final PrintWriter pw = new PrintWriter(mem);
            failure.printStackTrace(pw);
            pw.flush();
            longMessage += new String(mem.toByteArray());
        }
        final Notification notification = new Notification(shortMessage, longMessage);
        history.add(notification);
        if (history.size() > 100) {
            history.remove(0);
        }
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
