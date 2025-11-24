package org.example.notification.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatTimestamp(LocalDateTime timestamp) {
        return timestamp.format(formatter);
    }

    public static boolean isValidChannel(String channel) {
        return channel != null &&
                (channel.equals("push") || channel.equals("websocket") || channel.equals("sms"));
    }

    public static boolean isValidAlertType(String alertType) {
        return alertType != null &&
                (alertType.equals("sound_and_vibration") ||
                        alertType.equals("vibration_only") ||
                        alertType.equals("silent"));
    }
}