package info.ininfo.smstransmitter.models;

import android.content.Context;
import android.content.SharedPreferences;

import info.ininfo.smstransmitter.App;
import info.ininfo.smstransmitter.R;

public class Settings {

    public static boolean IsDemo = false;

    private SharedPreferences _preferences;
    private Context _context;

    public Settings(Context context) {
        _preferences = context.getSharedPreferences("settingsOfSmsTransmitter", Context.MODE_PRIVATE);
        _context = context;
    }

    public String GetKey() {
        if (IsDemo) return _context.getString(R.string.demo_settings_key);

        return _preferences.getString("key", "");
    }

    public void SetKey(String value) {
        _preferences.edit().putString("key", value.toUpperCase()).apply();
    }

    public String GetUrlGateway() {
        if (IsDemo) return _context.getString(R.string.settings_url_gateway_default);

        return _preferences.getString("urlGateway", _context.getString(R.string.settings_url_gateway_default));
    }

    public void SetUrlGateway(String value) {
        _preferences.edit().putString("urlGateway", value.toLowerCase()).apply();
    }

    public boolean GetSwitchSendAutomatically() {
        if (GetKey() == null || GetKey().isEmpty()) return false;
        return _preferences.getBoolean("switchSendAutomatically", true);
    }

    /**
     * Allow periodical workers
     */
    public void SetSwitchSendAutomatically(boolean value) {
        _preferences.edit().putBoolean("switchSendAutomatically", value).apply();
        resetWorker();
    }

    /**
     * Periodical scheduled workers interval in minute
     */
    public int GetFrequency() {
        return _preferences.getInt("frequency", 30);
    }

    public void SetFrequency(int value) {
        int previousValue = GetFrequency();

        _preferences.edit().putInt("frequency", value).apply();

        if (previousValue != value) {
            resetWorker();
        }
    }

    /**
     * Save last request time
     * */
    public void setLastRequestTime(long ms) {
        _preferences.edit().putLong("lastRequestTime", ms).apply();
    }

    /**
     * Used by periodical workers to preventing excessive requests
     * */
    public long getLastRequestTime() {
        return _preferences.getLong("lastRequestTime", 0);
    }

    private void resetWorker() {
        ((App) (_context.getApplicationContext())).checkWorker(true);
    }

}
