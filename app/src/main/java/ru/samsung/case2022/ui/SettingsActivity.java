package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.appDao;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.io.IOException;
import java.util.Objects;

import ru.samsung.case2022.R;
import ru.samsung.case2022.db.ServerDB;

public class SettingsActivity extends AppCompatActivity {

    public static ActionBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        bar = getSupportActionBar();
        if (appDao.getLogin() != "") {
            bar.setTitle(appDao.getName());
        }
        if (!ServerDB.hasConnection) {
            bar.setSubtitle(getString(R.string.no_connection));
        }
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                Log.v("KEY", s);
                if (Objects.equals(s, "name")) {
                    bar.setTitle(appDao.getName());
                    Log.v("NAME", appDao.getName());
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                new ServerDB(getApplicationContext()).setName(appDao.getName()).execute();
                            } catch (IOException ignored){}
                        }
                    }.start();
                }
            }
        });

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }


}