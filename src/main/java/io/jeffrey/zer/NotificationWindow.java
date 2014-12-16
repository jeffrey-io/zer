package io.jeffrey.zer;

import io.jeffrey.zer.Notifications.Notification;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * a window to render notifications
 *
 * @author jeffrey
 *
 */
public class NotificationWindow {

    /**
     * Show the current notifications
     *
     * @param notifications
     *            the notifications to show
     */
    public static void show(final Notifications notifications) {
        final Stage stage = new Stage();
        final VBox core = new VBox();

        for (final Notification notify : notifications.list(10)) {
            final TitledPane tp = new TitledPane(notify.shortMessage, new Label(notify.longMessage));
            tp.setExpanded(false);
            core.getChildren().add(tp);
        }

        final Scene scene = new Scene(core, 400, 600);
        stage.setScene(scene);
        stage.setTitle("Notifications");
        stage.show();
    }
}
