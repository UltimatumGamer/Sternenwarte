package digital.pedda.sternenwarte;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        initToolBar(myToolbar);
        initUI();
    }

    private void initUI() {
        UserSettings userSettings = new UserSettings(getSharedPreferences("UserInfo", 0));
        Switch vibrate = findViewById(R.id.switch1);
        vibrate.setChecked(userSettings.isVibrate());
        Switch setVolume = findViewById(R.id.switch2);
        setVolume.setChecked(userSettings.isSetVolume());
        Switch countdownInMessages = findViewById(R.id.switch3);
        countdownInMessages.setChecked(userSettings.isCountdownInMessages());
        SeekBar volume = findViewById(R.id.seekBar);
        volume.setProgress(userSettings.getVolume());

        vibrate.setOnCheckedChangeListener((buttonView, isChecked) -> userSettings.setVibrate(isChecked));
        setVolume.setOnCheckedChangeListener((buttonView, isChecked) -> userSettings.setSetVolume(isChecked));
        countdownInMessages.setOnCheckedChangeListener((buttonView, isChecked) -> userSettings.setCountdownInMessages(isChecked));
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                userSettings.setVolume(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    @SuppressLint("RestrictedApi")
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("RestrictedApi")
    public void initToolBar(Toolbar toolbar) {
        MenuBuilder menuBuilder = (MenuBuilder) toolbar.getMenu();
        menuBuilder.setOptionalIconsVisible(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            this.finish();
        }// If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

}