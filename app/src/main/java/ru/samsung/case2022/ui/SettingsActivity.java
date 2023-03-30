package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.appDao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import ru.samsung.case2022.R;
import ru.samsung.case2022.db.ServerDB;

public class SettingsActivity extends AppCompatActivity {

    FloatingActionButton back;

    public static ActionBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
            if (appDao.getLogin() != "")  getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
            else getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment2())
                    .commit();
        bar = getSupportActionBar();
        if (appDao.getLogin() != "") {
            bar.setTitle(appDao.getName());
        } else {
            bar.setTitle(R.string.app_name);
        }
        if (!ServerDB.hasConnection) {
            bar.setSubtitle(getString(R.string.no_connection));
        }

        back = findViewById(R.id.back_settings);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
        });
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                Log.v("KEY", s);
                switch (s) {
                    case "name":
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
                        break;
                    case "lang":
                        String lang = appDao.getLang();
                        if (lang == "default") {
                            appDao.setLocale(Locale.getDefault().getLanguage() == "ru" ? "ru" : "en");
                        } else {
                            appDao.setLocale();
                        }
                }
            }
        });

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.login_preferences, rootKey);
        }
    }

    public static class SettingsFragment2 extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.not_login_preferences, rootKey);
        }
    }
}