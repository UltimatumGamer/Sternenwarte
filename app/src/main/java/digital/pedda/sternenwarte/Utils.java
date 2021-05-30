package digital.pedda.sternenwarte;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Utils {
    public static String getResourceName(View view) {
        if (view.getId() == View.NO_ID) return null;
        else return view.getResources().getResourceEntryName(view.getId());
    }

    public static void createNotificationChannel(AppCompatActivity appCompatActivity, CharSequence name, String description, String id) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = appCompatActivity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
