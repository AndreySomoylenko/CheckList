package ru.samsung.case2022.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.samsung.case2022.R;
import ru.samsung.case2022.adapters.CustomAdapter;
import ru.samsung.case2022.adapters.Item;
import ru.samsung.case2022.db.AppDao;
import ru.samsung.case2022.db.BagSync;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.DBJson;
import ru.samsung.case2022.db.FullSync;
import ru.samsung.case2022.db.ListSync;
import ru.samsung.case2022.db.Money;
import ru.samsung.case2022.db.ServerDB;
import ru.samsung.case2022.db.SyncApi;
import ru.samsung.case2022.retrofit.models.ServerString;

/**
 * The RootActivity
 * @author Ismail Velidzhanov Philipp Shepnov
 * @version 1.0
 * It is the first screen the user see
 * In this screen user can manage his list of products
 */
public class RootActivity extends AppCompatActivity implements CustomAdapter.OnNoteListener {

    /**
     * Button which starts camera
     */
    FloatingActionButton scan;

    public static ActionBar bar;

    private static boolean showDialog = false;

    public static SyncApi syncApi;



    Handler mHandler;

    /**
     * Button which open AddActivity to add element to list
     */
    FloatingActionButton add;

    FloatingActionButton bag;

    /**
     * Recyclerview which shows list of products
     */

    static CustomAdapter adapter;
    static RecyclerView recycler;

    /**
     * Path for imageFile
     */
    private String pictureImagePath = "";

    /**
     * Bitmap to save user's photo
     */
    public static Bitmap  bitmap;

    /**
     * Class which help us to manage list of products
     */
    public DBJson db;

    public static AppDao appDao;
    private ServerDB serverDB;

    /**
     * Method to start this activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         No night mode
         */
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        recycler = findViewById(R.id.recycler);
        scan = findViewById(R.id.scan);
        add = findViewById(R.id.add);
        mHandler = new Handler();
        bag = findViewById(R.id.bag);
        serverDB = new ServerDB(this);
        appDao = new AppDao(this);
        db = new DBJson(this);
        // Listener for scan button
        scan.setOnClickListener(v -> {
            takePicture();
        });
        // Listener for add button
        add.setOnClickListener(v -> {
            Intent add_intent = new Intent(this, AddActivity.class);
            startActivity(add_intent);
        });
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSync = spref.getBoolean("sync", false);
        if (isSync) {
            String choice = spref.getString("reply", null);
            switch (choice) {
                case "full":
                    syncApi = new FullSync(this);
                    DBJson.startServ = true;
                    break;
                case "only_bag":
                    syncApi = new BagSync(this);
                    DBJson.startServ = true;
                    break;
                case "only_list":
                    syncApi = new ListSync(this);
                    DBJson.startServ = true;
                    break;
                default:
                    syncApi = null;
            }
        } else {
            syncApi = null;
        }

        if (DBJson.startServ) {
            startService();
            DBJson.startServ = false;
        }

        bag.setOnClickListener(v -> {
            Intent intent = new Intent(this, BagActivity.class);
            startActivity(intent);
        });
        if (DBJson.start) {
            db.init();
            BuysManager.getMapFromJSON(getApplicationContext());
            DBJson.start = false;
        }
        bar = getSupportActionBar();
        if (!ServerDB.hasConnection) {
            bar.setSubtitle(getString(R.string.no_connection));
        }
        String lang = appDao.getLang();
        if (lang.equals("default")) {
            appDao.setLocale(Locale.getDefault().getLanguage().equals("ru") ? "ru" : "en");
        } else {
            appDao.setLocale();
        }

        if (!appDao.getLogin().equals("")) {
            bar.setTitle(appDao.getName());
            serverDB.getName().enqueue(new Callback<ServerString>() {
                @Override
                public void onResponse(Call<ServerString> call, Response<ServerString> response) {
                    appDao.setName(response.body().str);
                    bar.setTitle(appDao.getName());
                }

                @Override
                public void onFailure(Call<ServerString> call, Throwable t) {

                }
            });
        }


    }

    /**
     * Method to start taking picture if we can use camera
     */
    private void takePicture() {
        try{
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = timeStamp + ".jpg";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                File file = new File(pictureImagePath);
                Uri outputFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            } catch (Exception ignored) {

            } finally {
                someActivityResultLauncher.launch(cameraIntent);
            }
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, getString(R.string.camera_not_recog), Toast.LENGTH_SHORT).show();
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                /**
                 * Method to take user's photo
                 * @param result
                 */
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        try {
                            File imgFile = new File(pictureImagePath);
                            // Check if file exists
                            if (imgFile.exists()) {
                                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            } else {
                                assert data != null;
                                Bundle extras = data.getExtras();
                                bitmap = (Bitmap) extras.get("data");
                            }
                        } catch (Exception e) {
                            assert data != null;
                            Bundle extras = data.getExtras();
                            bitmap = (Bitmap) extras.get("data");
                        } finally {
                            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            });

    /**
     * This method is called after onCreate method
     * In this method we set Adapter for RecyclerView
     */
    @Override
    protected void onResume() {
        super.onResume();

        adapter = new CustomAdapter(BuysManager.buys, this, this);
        recycler.setAdapter(adapter);
    }

    /**
     *Method to catches user's onItemClick
     * @param position index of item in the list
     */
    @Override
    public void OnNoteClick(int position) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {
        Log.d("s", "s");
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.add));
        alert.setMessage(getString(R.string.sure_add));
        alert.setPositiveButton("Да", (dialog, whichButton) -> {
            db.addToBag(BuysManager.buys.get(position));
            db.removeByIndex(position);
            adapter.refresh(BuysManager.buys);
            recycler.setAdapter(adapter);
            if (syncApi != null) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            syncApi.sync().execute();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.start();
            }
        });
        alert.setNegativeButton("Нет", (dialog, whichButton) -> {
        });
        alert.show();
    }

    /**
     * This method sets menu to AppBar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        if (Objects.equals(appDao.getLogin(), "")) {
            mi.inflate(R.menu.main_bar_menu, menu);
        } else {
            mi.inflate(R.menu.logged_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method catches user's click on menu button
     * @param item button on the menu which user clicked
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.log_in:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);

                return true;

            case R.id.log_out:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(getString(R.string.exit));
                alert.setMessage(getString(R.string.exit_sure));
                alert.setPositiveButton(getString(R.string.yes), (dialog, whichButton) -> {
                    appDao.setLogin("");
                    Toast.makeText(this, getString(R.string.you_logged_out), Toast.LENGTH_SHORT).show();
                    Intent restart = getIntent();
                    finish();
                    startActivity(restart);
                });
                alert.setNegativeButton(getString(R.string.no), (dialog, whichButton) -> {
                });
                alert.show();
                return true;

            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * This method is called when user close application
     * In this method we save user's list to GetSharedPreferences
     */
    @Override
    protected void onStop() {
        super.onStop();
        db.save();
    }


    private void startService() {
        String login = appDao.getLogin();
        if (!login.equals("")) {
            ScheduledExecutorService executorService
                    = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleWithFixedDelay(() -> {
                Log.d("Philipp", "Ismail");
                if (!appDao.getLogin().equals("") && syncApi != null && !showDialog) {
                    updateList(false, syncApi);
                    serverDB.getName().enqueue(new Callback<ServerString>() {
                        @Override
                        public void onResponse(Call<ServerString> call, Response<ServerString> response) {
                            String s = response.body().str;
                            appDao.setName(s);
                            try {
                                RootActivity.bar.setTitle(s);
                                SettingsActivity.bar.setTitle(s);
                                AddActivity.bar.setTitle(s);
                                BagActivity.bar.setTitle(s);
                                CameraActivity.bar.setTitle(s);
                                EditActivity.bar.setTitle(s);
                                LoginActivity.bar.setTitle(s);
                                RegisterActivity.bar.setTitle(s);
                            } catch (Exception ignored) {
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerString> call, Throwable t) {

                        }
                    });
                } else {
                    executorService.shutdownNow();
                }

            }, 1, 1, TimeUnit.SECONDS);
        }
    }

    public void updateList(boolean forRefresh, SyncApi localSyncApi) {
        localSyncApi.getList().enqueue( new Callback<List<String>[]>() {
            @Override
            public void onResponse(Call<List<String>[]> call, Response<List<String>[]> response) {
                try {
                    RootActivity.bar.setSubtitle("");
                    SettingsActivity.bar.setSubtitle("");
                    AddActivity.bar.setSubtitle("");
                    BagActivity.bar.setSubtitle("");
                    CameraActivity.bar.setSubtitle("");
                    EditActivity.bar.setSubtitle("");
                    LoginActivity.bar.setSubtitle("");
                    RegisterActivity.bar.setSubtitle("");
                } catch (Exception ignored) {}
                if (!ServerDB.hasConnection && !showDialog && response.body() != null) {
                    ServerDB.hasConnection = true;
                    if (response.body()[0] != null && ((!Objects.equals(BuysManager.unpack(BuysManager.buys), response.body()[0])) ||  (response.body()[1] != null && (!Objects.equals(BuysManager.unpack(BuysManager.bag), response.body()[1]))))) {
                        showDialog = true;
                        mHandler.post(() -> {

                            AlertDialog.Builder alert = new AlertDialog.Builder(RootActivity.this);
                            alert.setTitle("Несовпадение данных на сервере");
                            alert.setMessage("Какой список оставить");
                            alert.setPositiveButton("На устройстве", (dialog, whichButton) -> {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            localSyncApi.sync().execute();
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }.start();
                                showDialog = false;
                                startService();
                            });
                            alert.setNegativeButton("На сервере", (dialog, whichButton) -> {
                                showDialog = false;
                                startService();
                            });
                            alert.show();
                        });
                    }

                } else if (!showDialog && response.body() != null){
                    if (response.body()[0] != null) {
                        if (!Objects.equals(BuysManager.unpack(BuysManager.buys), response.body()[0])) {
                            BuysManager.buys = BuysManager.pack(response.body()[0]);
                            RootActivity.adapter.refresh(BuysManager.buys);
                            RootActivity.recycler.setAdapter(adapter);
                            db.save();
                        }
                    }
                    if (response.body()[1] != null) {
                        if (!Objects.equals(BuysManager.unpack(BuysManager.bag), response.body()[1])) {
                            BuysManager.bag = BuysManager.pack(response.body()[1]);
                            try {
                                BagActivity.adapter.refresh(BuysManager.bag);
                                BagActivity.recyclerView.setAdapter(BagActivity.adapter);
                                BuysManager.sum = BuysManager.countSum();
                                try {
                                    BagActivity.suma.setText(getString(R.string.total) + " " + BuysManager.sum.getRubles() +  getString(R.string.rub) + " " + BuysManager.sum.getCents() + getString(R.string.kop));
                                } catch (Exception ignored) {}
                                db.save();
                                Log.d("refresss", "refr");
                            } catch (Exception ignored) {
                            }
                        }
                    }
                    if (forRefresh) {
                        Log.d("REFRESH", "STOPPED");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<String>[]> call, Throwable t) {
                ServerDB.hasConnection = false;
                try {
                    String s = getString(R.string.no_connection);
                    RootActivity.bar.setSubtitle(s);
                    SettingsActivity.bar.setSubtitle(s);
                    AddActivity.bar.setSubtitle(s);
                    BagActivity.bar.setSubtitle(s);
                    CameraActivity.bar.setSubtitle(s);
                    EditActivity.bar.setSubtitle(s);
                    LoginActivity.bar.setSubtitle(s);
                    RegisterActivity.bar.setSubtitle(s);
                } catch (Exception ignored) {
                }
            }
        });
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}