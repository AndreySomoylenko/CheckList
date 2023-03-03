package ru.samsung.case2022.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ru.samsung.case2022.R;
import ru.samsung.case2022.adapters.BagAdapter;
import ru.samsung.case2022.db.BuysManager;

public class BagActivity extends AppCompatActivity implements BagAdapter.OnNoteListener{

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycler_bag);
        recyclerView.setAdapter(new BagAdapter(BuysManager.bag, this, this));
        TextView suma = findViewById(R.id.sum);
        float sum = BuysManager.sum;
        String rubles = String.valueOf((int) sum);
        String cents = String.valueOf((int)((sum % 1) * 100));
        suma.setText("ИТОГО: " + rubles + "руб " + cents + "коп");
    }

    @Override
    public void OnNoteClick(int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Удалить");
        alert.setMessage("Вы хотите удалить этот элемент из корзины?");
        alert.setPositiveButton("Да", (dialog, whichButton) -> {
            RootActivity.db.removeByIndexBag(position);
            recyclerView.getAdapter().notifyDataSetChanged();
        });
        alert.setNegativeButton("Нет", (dialog, whichButton) -> {
        });
        alert.show();
    }
}