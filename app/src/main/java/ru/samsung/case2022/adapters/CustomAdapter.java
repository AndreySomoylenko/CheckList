package ru.samsung.case2022.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ru.samsung.case2022.R;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.Money;

/**
 * The CustomAdapter class
 * @author Dmitry Kovalchuk
 * This class is used to work with RecyclerView
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {



    private List<Item> localDataSet;
    private final Context context;

    private final OnNoteListener mOnNoteListener;

    private final int[] itemIds = {
            R.drawable.biscuits,
            R.drawable.broccoli,
            R.drawable.cheese,
            R.drawable.coffee,
            R.drawable.curd,
            R.drawable.dough,
            R.drawable.milk,
            R.drawable.pancakes,
            R.drawable.sourcream,
            R.drawable.tea
    };


    /**
     * it is the constructor of our adapter
     * @param localDataSet is the user's list of products
     * @param context is the application context
     * @param onNoteListener is the api we use to catch click on element
     */
    public CustomAdapter(List<Item> localDataSet, Context context, OnNoteListener onNoteListener) {
        this.localDataSet = localDataSet;
        this.context = context;
        this.mOnNoteListener = onNoteListener;
    }

    public void refresh(List<Item> localDataSet) {
        this.localDataSet = localDataSet;
    }

    /**
     * @param viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                  an adapter position.
     * @param viewType  The view type of the new View.
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_layout, viewGroup, false);

        return new ViewHolder(view, mOnNoteListener );
    }


    /**
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Item local = localDataSet.get(position);


        viewHolder.getTextView().setText(local.name);



        viewHolder.getPhoto().setImageResource(getIdByName(local.name));


        String rubles;
        String cents;
        Money price = getMoneyByName(local).multiply(local.count);
        rubles = String.valueOf(price.getRubles());
        cents = String.valueOf(price.getCents());
        Log.d("LOCAL DATASET", localDataSet.toString());
        Log.d("BUYSMANAG BUYS", BuysManager.buys.toString());

        viewHolder.getPrice().setText(rubles + context.getString(R.string.rub) + " " + cents + context.getString(R.string.kop) + " " + "(" + local.count + " " + context.getString(R.string.count_short) + ")");
    }


    private int getIdByName(String name) {
        for (int i = 0; i < BuysManager.possibleItems.size(); i++) {
            if (Objects.equals(name.toLowerCase(), BuysManager.possibleItems.get(i).toLowerCase())) {
                return itemIds[i];
            }
        }
        return R.drawable.unknown_product;
    }

    public static Money getMoneyByName(Item item) {
        String name = item.name;
        for (String key: BuysManager.prices.keySet()) {
            if (Objects.equals(name.toLowerCase(), key.toLowerCase())) {
                return BuysManager.prices.get(key);
            }
        }
        return new Money(0, 0);
    }


    /**
     * @return size of our list
     */
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    /**
     * it is constructor of our class
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView name;

        TextView price;

        ImageView photo;

        OnNoteListener onNoteListener;

        /**
         * Class wich contains item of recycler and it's onClicklistener
         * @param itemView
         * @param onNoteListener is interface to catch clicks
         */
        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            photo = itemView.findViewById(R.id.productImage);
            price = itemView.findViewById(R.id.price);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            this.onNoteListener = onNoteListener;
        }

        /**
         * Detter for Textview text
         * @return value of text
         */
        public TextView getTextView() {
            return name;
        }

        public TextView getPrice() {return price;}

        public ImageView getPhoto() {return photo;}

        /**
         * I's function to catch user OnItemClick
         * @param view
         */
        @Override
        public void onClick(View view) {
            onNoteListener.OnNoteClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onNoteListener.onLongClick(getAdapterPosition());
            return true;
        }
    }

    /**
     *This interface is used to catch click on element
     */
    public interface OnNoteListener {
        void OnNoteClick(int position);
        void onLongClick(int position);
    }

}
