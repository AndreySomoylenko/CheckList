package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.syncApi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Collections;

import ru.samsung.case2022.R;
import ru.samsung.case2022.adapters.CustomAdapter;
import ru.samsung.case2022.db.AppDao;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.DBJson;
import ru.samsung.case2022.db.Money;
import ru.samsung.case2022.db.ServerDB;

public class BagActivity extends AppCompatActivity implements CustomAdapter.OnNoteListener{


    /**
     * This is the recycler view which is used to show priducts in bag
     */
    static RecyclerView recyclerView;

    DBJson db;

    AppDao appDao;

    /**
     * This is the TextView to show sum of user's buys
     */
    TextView suma;

    /**
     * This is button to return to home
     */
    FloatingActionButton back;

    /**
     * This is adapter to manage recycler
     */
    static CustomAdapter adapter;

    public static ActionBar bar;


    /**
     * This method calls on activity start
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        recyclerView = findViewById(R.id.recycler_bag);
        appDao = new AppDao(this);
        db = new DBJson(this);
        adapter = new CustomAdapter(BuysManager.bag, this, this);
        recyclerView.setAdapter(adapter);
        suma = findViewById(R.id.sum);
        Money sum = BuysManager.sum;
        String rubles = String.valueOf(sum.getRubles());
        String cents = String.valueOf(sum.getCents());
        suma.setText(getString(R.string.total) + " " + rubles +  getString(R.string.rub) + " " + cents + getString(R.string.kop));
        back = findViewById(R.id.back_bag);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
            finish();
        });
        if (!appDao.getLogin().equals("")) {
            getSupportActionBar().setTitle(appDao.getName());
        }
        bar = getSupportActionBar();
        if (!ServerDB.hasConnection) {
            bar.setSubtitle(getString(R.string.no_connection));
        }
    }


    /**
     * This method calls when user click on element in bag
     * @param position
     */
    @Override
    public void OnNoteClick(int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.delete));
        alert.setMessage(getString(R.string.delete_sure));
        alert.setPositiveButton("Да", (dialog, whichButton) -> {
            Money price = CustomAdapter.getMoneyByName(BuysManager.bag.get(position));
            BuysManager.sum = BuysManager.sum.minus(price.multiply(BuysManager.bag.get(position).count));
            Money sum = BuysManager.sum;
            String rubles = String.valueOf(sum.getRubles());
            String cents = String.valueOf(sum.getCents());
            suma.setText("ИТОГО: " + rubles + "руб " + cents + "коп");
            db.removeByIndexBag(position);
            db.save();
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
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

    @Override
    public void onLongClick(int position) {

    }


    /**
     * This is function to create menu on AppBar
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.bag_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * This method called when user clicks on button in menu
     * @param item clicked button
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.removeAll:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(getString(R.string.delete));
                alert.setMessage(getString(R.string.clear_bag_sure));
                alert.setPositiveButton(getString(R.string.yes), (dialog, whichButton) -> {
                    (new DBJson(this)).clearBag();

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

                    BuysManager.sum.makeZero();
                    suma.setText(getString(R.string.total_0_rub_0_kop));
                    recyclerView.getAdapter().notifyDataSetChanged();
                    db.save();
                });
                alert.setNegativeButton(getString(R.string.no), (dialog, whichButton) -> {
                });
                alert.show();
                return true;
            default:

                return super.onOptionsItemSelected(item);

        }
    }
}