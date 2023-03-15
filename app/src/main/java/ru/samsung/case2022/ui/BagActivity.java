package ru.samsung.case2022.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

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

public class BagActivity extends AppCompatActivity implements BagAdapter.OnNoteListener{



    RecyclerView recyclerView;

    TextView suma;

    FloatingActionButton back;

    BagAdapter adapter;


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
        suma.setText("ИТОГО: " + rubles + "руб " + cents + "коп");
        back = findViewById(R.id.back_bag);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
            finish();
        });
    }


    /**
     * This method calls when user click on element in bag
     * @param position
     */
    @Override
    public void OnNoteClick(int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Удалить");
        alert.setMessage("Вы хотите удалить этот элемент из корзины?");
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
                alert.setTitle("Удалить");
                alert.setMessage("Вы хотите очистить корзину?");
                alert.setPositiveButton("Да", (dialog, whichButton) -> {
                    (new DBJson(this)).clearBag();
                    BuysManager.sum.makeZero();
                    suma.setText("ИТОГО: " + "0" + "руб " + "0" + "коп");
                    recyclerView.getAdapter().notifyDataSetChanged();
                });
                alert.setNegativeButton("Нет", (dialog, whichButton) -> {
                });
                alert.show();
                return true;
            default:

                return super.onOptionsItemSelected(item);

        }
    }
}