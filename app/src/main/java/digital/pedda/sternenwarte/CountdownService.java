package digital.pedda.sternenwarte;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Date;

public class CountdownService extends Service {
    private final Handler handler = new Handler();
    private Runnable runnable;
    private TextView tv_days, tv_hour, tv_minute, tv_second;
    private LinearLayout linear_layout_1, linear_layout_2;
    private App app;

    private UserSettings userSettings;

    private Button startButton, stopButton;


    @Override
    public void onCreate() {
    }

    public void initLayouts(App app) {


        this.app = app;

        this.userSettings = App.getUserSettings();

        this.tv_days = app.getTv_days();
        this.tv_hour = app.getTv_hour();
        this.tv_minute = app.getTv_minute();
        this.tv_second = app.getTv_second();

        this.startButton = app.getStartButton();
        this.stopButton = app.getStopButton();

        this.linear_layout_1 = app.getLinear_layout_1();
        this.linear_layout_2 = app.getLinear_layout_2();


    }


    private void countDownStart(Date futureDate) {
        NotificationCompat.Builder builder = null;
        NotificationManager notificationManager = null;

        if (App.getUserSettings().isCountdownInMessages()) {
            Intent intentAction = new Intent(app.getBaseContext(), ActionReceiver.class);

            intentAction.putExtra("action", "stopCountdown");

            PendingIntent pIntentlogin = PendingIntent.getBroadcast(app.getBaseContext(), 1, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);

            builder = new NotificationCompat.Builder(app, "countdown")
                    .setSmallIcon(R.drawable.ic_telescope)
                    .setContentTitle("Countdown")
                    .setContentText("START")
                    .setOnlyAlertOnce(true)
                    .addAction(R.drawable.ic_telescope, "Stop", pIntentlogin) // #0
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            notificationManager = (NotificationManager) app.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, builder.build());
        }
        NotificationCompat.Builder finalBuilder = builder;
        NotificationManager finalNotificationManager = notificationManager;
        runnable = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    Date current_date = new Date();
                    if (!current_date.after(futureDate)) {
                        startButton.setEnabled(false);
                        stopButton.setEnabled(true);

                        long diff = (futureDate != null ? futureDate.getTime() : 0) - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        //
                        tv_days.setText(String.format("%02d", Days));
                        tv_hour.setText(String.format("%02d", Hours));
                        tv_minute.setText(String.format("%02d", Minutes));
                        tv_second.setText(String.format("%02d", Seconds));

                        StringBuilder stringBuilder = new StringBuilder();
                        if (Days > 0) {
                            stringBuilder.append(Days).append(":").append(Hours).append(":");
                        } else {
                            if (Hours > 0) {
                                stringBuilder.append(Hours).append(":");
                            }
                        }

                        stringBuilder.append(Minutes).append(":").append(Seconds);

                        if (App.getUserSettings().isCountdownInMessages()) {
                            finalBuilder.setContentText(stringBuilder.toString());

                            finalNotificationManager.notify(1, finalBuilder.build());
                        }

                        if (Seconds == 30) {
                            if (userSettings.isVibrate()) {
                                final VibrationEffect vibrationEffect1;

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                                    vibrationEffect1 = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);

                                    app.stopVibrator();
                                    app.startVibrator(vibrationEffect1);
                                }
                            }
                        }

                        if ((Days + Hours + Minutes) == 0) {
                            app.playCountdownAudio((int) Seconds);
                        }

                    } else {
                        stopCountdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public void stopCountdown() {
        handler.removeCallbacks(runnable);
        linear_layout_1.setVisibility(View.VISIBLE);
        linear_layout_2.setVisibility(View.GONE);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    public void startCountdown(Date futureDate) {
        countDownStart(futureDate);
        linear_layout_1.setVisibility(View.GONE);
        linear_layout_2.setVisibility(View.VISIBLE);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }


    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
