package digital.pedda.sternenwarte;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class App extends AppCompatActivity {

    CountdownService countdownService = new CountdownService();
    private LinearLayout linear_layout_1, linear_layout_2;
    private TextView tv_days, tv_hour, tv_minute, tv_second, timePreview;
    private EditText inputMinuteCustom;
    private int selectedMinutes;
    private Vibrator vibrator;
    private UserSettings userSettings;
    View.OnClickListener onButtonClickListener = v -> {
        String button = Utils.getResourceName(v);
        switch (Objects.requireNonNull(button)) {
            case "buttonMinute1":
                selectedMinutes = 1;
                break;
            case "buttonMinute2":
                selectedMinutes = 2;
                break;
            case "buttonMinute3":
                selectedMinutes = 3;
                break;
            case "buttonMinuteCustom":
                String text = String.valueOf(inputMinuteCustom.getText());
                if (text.length() < 1) {
                    selectedMinutes = 0;
                } else {
                    selectedMinutes = Integer.parseInt(text);
                }
                break;
            case "startButton":
                Calendar currentTimeNow = Calendar.getInstance();
                currentTimeNow.add(Calendar.MINUTE, selectedMinutes);
                Date futureTime = currentTimeNow.getTime();
                countdownService.startCountdown(futureTime);
                if(userSettings.isSetVolume()){
                    System.out.println("SET VOLUME");
                    AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, userSettings.getVolume(), 0);
                }

                break;
            case "stopButton":
                linear_layout_1.setVisibility(View.VISIBLE);
                linear_layout_2.setVisibility(View.GONE);
                countdownService.stopCountdown();
            default:
                return;
        }
        timePreview.setText(String.valueOf(selectedMinutes));
    };
    private Button startButton, stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        initUI();

        userSettings = getUserSettings();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void initUI() {
        linear_layout_1 = findViewById(R.id.linear_layout_1);
        linear_layout_2 = findViewById(R.id.linear_layout_2);
        tv_days = findViewById(R.id.tv_days);
        tv_hour = findViewById(R.id.tv_hour);
        tv_minute = findViewById(R.id.tv_minute);
        tv_second = findViewById(R.id.tv_second);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        countdownService.initLayouts(this);

        timePreview = findViewById(R.id.selectedMinutes);
        inputMinuteCustom = findViewById(R.id.inputMinuteCustom);

        List<Button> buttonList = new ArrayList<>();
        buttonList.add(findViewById(R.id.buttonMinute1));
        buttonList.add(findViewById(R.id.buttonMinute2));
        buttonList.add(findViewById(R.id.buttonMinute3));
        buttonList.add(findViewById(R.id.buttonMinuteCustom));

        buttonList.add(findViewById(R.id.startButton));
        buttonList.add(findViewById(R.id.stopButton));

        buttonList.forEach(button -> button.setOnClickListener(onButtonClickListener));
    }

    public void playCountdownAudio(Integer count) {
        switch (count) {
            case 10:
                play(R.raw.zehn);
                break;
            case 5:
                play(R.raw.fuenf);
                break;
            case 3:
                play(R.raw.drei);
                break;
            case 2:
                play(R.raw.zwei);
                break;
            case 1:
                play(R.raw.eins);
                break;
            case 0:
                play(R.raw.uooooa);
                break;
        }
    }

    private void play(int songId) {
        MediaPlayer player = MediaPlayer.create(this, songId);
        player.start();
    }

    protected void onStop() {
        super.onStop();
    }

    public TextView getTv_days() {
        return tv_days;
    }

    public LinearLayout getLinear_layout_1() {
        return linear_layout_1;
    }

    public LinearLayout getLinear_layout_2() {
        return linear_layout_2;
    }

    public TextView getTv_hour() {
        return tv_hour;
    }

    public TextView getTv_minute() {
        return tv_minute;
    }

    public TextView getTv_second() {
        return tv_second;
    }

    public TextView getTimePreview() {
        return timePreview;
    }

    public EditText getInputMinuteCustom() {
        return inputMinuteCustom;
    }

    public int getSelectedMinutes() {
        return selectedMinutes;
    }

    public Vibrator getVibrator() {
        return vibrator;
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getStopButton() {
        return stopButton;
    }


    public void startVibrator(VibrationEffect vibrationEffect) {
        vibrator.vibrate(vibrationEffect);
    }

    public void stopVibrator() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    public UserSettings getUserSettings() {
        return new UserSettings(getSharedPreferences("UserInfo", 0));
    }

}