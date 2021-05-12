package digital.pedda.sternenwarte;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Date;

public class CountdownService extends Service {
    private final Handler handler = new Handler();
    private Runnable runnable;
    private TextView tv_days, tv_hour, tv_minute, tv_second;
    private LinearLayout linear_layout_1, linear_layout_2;
    private App app;

    private Button startButton, stopButton;



    @Override
    public void onCreate() {
    }

    public void initLayouts(App app) {


        this.app = app;

        this.tv_days = app.getTv_days();
        this.tv_hour = app.getTv_hour();
        this.tv_minute = app.getTv_minute();
        this.tv_second = app.getTv_second();

        this.startButton = app.getStartButton();
        this.stopButton =app.getStopButton();

        this.linear_layout_1 = app.getLinear_layout_1();
        this.linear_layout_2 = app.getLinear_layout_2();


    }


    private void countDownStart(Date futureDate) {
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
                        if (Seconds == 30) {
                            final VibrationEffect vibrationEffect1;

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                                vibrationEffect1 = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);

                                app.stopVibrator();
                                app.startVibrator(vibrationEffect1);
                            }
                        }
                        if ((Days + Hours + Minutes) == 0) {
                            app.playCountdownAudio((int) Seconds);
                        }

                    } else {
                        linear_layout_1.setVisibility(View.VISIBLE);
                        linear_layout_2.setVisibility(View.GONE);
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        linear_layout_1.setVisibility(View.GONE);
        linear_layout_2.setVisibility(View.VISIBLE);
        handler.postDelayed(runnable, 0);
    }

    public void stopCountdown() {
        handler.removeCallbacks(runnable);
    }

    public void startCountdown(Date futureDate) {
        countDownStart(futureDate);
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
