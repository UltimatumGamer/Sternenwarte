package digital.pedda.sternenwarte;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

public class UserSettings {
    private final SharedPreferences.Editor editor;
    private boolean vibrate;
    private boolean setVolume;
    private int volume;
    private boolean countdownInMessages;

    @SuppressLint("CommitPrefEdits")
    public UserSettings(SharedPreferences settings) {
        editor = settings.edit();
        vibrate = settings.getBoolean("vibrate", true);
        setVolume = settings.getBoolean("setVolume", true);
        volume = settings.getInt("volume", 10);
        countdownInMessages = settings.getBoolean("countdownInMessages", true);
    }

    public boolean isSetVolume() {
        return setVolume;
    }

    public void setSetVolume(boolean setVolume) {
        editor.putBoolean("setVolume", setVolume);
        editor.commit();
        this.setVolume = setVolume;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        editor.putInt("volume", volume);
        editor.commit();
        this.volume = volume;
    }

    public boolean isCountdownInMessages() {
        return countdownInMessages;
    }

    public void setCountdownInMessages(boolean countdownInMessages) {
        editor.putBoolean("countdownInMessages", countdownInMessages);
        editor.commit();
        this.countdownInMessages = countdownInMessages;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        editor.putBoolean("vibrate", vibrate);
        editor.commit();
        this.vibrate = vibrate;
    }
}
