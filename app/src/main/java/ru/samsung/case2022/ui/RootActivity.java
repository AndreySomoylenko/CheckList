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
import ru.samsung.case2022.db.AppDao;
import ru.samsung.case2022.db.BagSync;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.DBJson;
import ru.samsung.case2022.db.FullSync;
import ru.samsung.case2022.db.ListSync;
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

    public static SyncApi syncApi;




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

    static SwipeRefreshLayout swipeRefreshLayout;

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
    public static DBJson db;

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
        swipeRefreshLayout= findViewById(R.id.swipeRefreshLayout);
        recycler = findViewById(R.id.recycler);
        scan = findViewById(R.id.scan);
        add = findViewById(R.id.add);
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
                    break;
                case "only_bag":
                    syncApi = new BagSync(this);
                    break;
                case "only_list":
                    syncApi = new ListSync(this);
                    break;
                default:
                    syncApi = null;
            }
        } else {
            syncApi = null;
        }

        bag.setOnClickListener(v -> {
            Intent intent = new Intent(this, BagActivity.class);
            startActivity(intent);
        });
        if (DBJson.start) {
            Start();
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("REFRESH", "ON REFRESH");
                updateList(true, new ListSync(getApplicationContext()));

            }
        });


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
        Start();
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


    private void Start() {
        db.init();
        String login = appDao.getLogin();
        if (!login.equals("")) {
            ScheduledExecutorService executorService
                    = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleWithFixedDelay(() -> {
                Log.d("Philipp", "Ismail");
                if (!appDao.getLogin().equals("")) {
                    updateList(false, syncApi);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                    executorService.shutdown();
                }

            }, 4, 4, TimeUnit.SECONDS);
        } else {
            swipeRefreshLayout.setEnabled(false);
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
                if (!ServerDB.hasConnection) {
                    localSyncApi.sync().enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ServerDB.hasConnection = true;
                            swipeRefreshLayout.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            ServerDB.hasConnection = true;
                            swipeRefreshLayout.setEnabled(true);

                        }
                    });
                } else {
                    if (response.body()[0] != null) {
                        BuysManager.buys = response.body()[0];
                        RootActivity.adapter.refresh(BuysManager.buys);
                        RootActivity.recycler.setAdapter(adapter);
                        db.save();
                    }
                    if (response.body()[1] != null) {
                        BuysManager.bag = response.body()[1];
                        try {
                            BagActivity.adapter.refresh(BuysManager.bag);
                            BagActivity.recyclerView.setAdapter(BagActivity.adapter);
                        } catch (Exception ignored) {}
                        db.save();
                    }
                    if (forRefresh) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("REFRESH", "STOPPED");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<String>[]> call, Throwable t) {
                ServerDB.hasConnection = false;
                swipeRefreshLayout.setEnabled(false);
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