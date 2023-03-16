package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.appDao;

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

import java.util.Objects;

import ru.samsung.case2022.R;
import ru.samsung.case2022.adapters.BagAdapter;
import ru.samsung.case2022.db.AppDao;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.DBJson;
import ru.samsung.case2022.db.Money;
import ru.samsung.case2022.db.ServerDB;

public class BagActivity extends AppCompatActivity implements BagAdapter.OnNoteListener{


    /**
     * This is the recycler view which is used to show priducts in bag
     */
    RecyclerView recyclerView;

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
    BagAdapter adapter;

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
        adapter = new BagAdapter(BuysManager.bag, this, this);
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
        if (appDao.getLogin() != "") {
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
            if (BuysManager.prices.get(BuysManager.bag.get(position)) != null) {
                Money price = BuysManager.prices.get(BuysManager.bag.get(position));
                BuysManager.sum = BuysManager.sum.minus(price);
            }
            Money sum = BuysManager.sum;
            String rubles = String.valueOf(sum.getRubles());
            String cents = String.valueOf(sum.getCents());
            suma.setText("ИТОГО: " + rubles + "руб " + cents + "коп");
            RootActivity.db.removeByIndexBag(position);
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        });
        alert.setNegativeButton("Нет", (dialog, whichButton) -> {
        });
        alert.show();
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
                    BuysManager.sum.makeZero();
                    suma.setText(getString(R.string.total_0_rub_0_kop));
                    recyclerView.getAdapter().notifyDataSetChanged();
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