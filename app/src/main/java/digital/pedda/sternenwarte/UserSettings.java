package digital.pedda.sternenwarte;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

public class UserSettings {
    private final SharedPreferences.Editor editor;
    private final SharedPreferences settings;


    @SuppressLint("CommitPrefEdits")
    public UserSettings(SharedPreferences settings) {
        this.settings = settings;
        this.editor = settings.edit();
    }

    public boolean isSetVolume() {
        return settings.getBoolean("setVolume", true);
    }

    public void setSetVolume(boolean setVolume) {
        editor.putBoolean("setVolume", setVolume);
        editor.commit();
    }

    public int getVolume() {
        return settings.getInt("volume", 10);
    }

    public void setVolume(int volume) {
        editor.putInt("volume", volume);
        editor.commit();
    }

    public boolean isCountdownInMessages() {
        return settings.getBoolean("countdownInMessages", true);
    }

    public void setCountdownInMessages(boolean countdownInMessages) {
        editor.putBoolean("countdownInMessages", countdownInMessages);
        editor.commit();
    }

    public boolean isVibrate() {
        return settings.getBoolean("vibrate", true);
    }

    public void setVibrate(boolean vibrate) {
        editor.putBoolean("vibrate", vibrate);
        editor.commit();
    }
}
