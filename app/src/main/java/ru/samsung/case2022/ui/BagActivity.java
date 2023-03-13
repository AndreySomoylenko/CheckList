package ru.samsung.case2022.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.samsung.case2022.R;
import ru.samsung.case2022.adapters.BagAdapter;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.Money;

public class BagActivity extends AppCompatActivity implements BagAdapter.OnNoteListener{

    RecyclerView recyclerView;

    TextView suma;

    FloatingActionButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        recyclerView = findViewById(R.id.recycler_bag);
        recyclerView.setAdapter(new BagAdapter(BuysManager.bag, this, this));
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
        });
        alert.setNegativeButton("Нет", (dialog, whichButton) -> {
        });
        alert.show();
    }
}