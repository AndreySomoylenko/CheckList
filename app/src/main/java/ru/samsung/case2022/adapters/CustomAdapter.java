package ru.samsung.case2022.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
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



    private List<String> localDataSet;
    private final Context context;

    private final OnNoteListener mOnNoteListener;

    private int[] itemIds = {
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
    public CustomAdapter(List<String> localDataSet, Context context, OnNoteListener onNoteListener) {
        this.localDataSet = localDataSet;
        this.context = context;
        this.mOnNoteListener = onNoteListener;
    }

    public void refresh(List<String> localDataSet) {
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

        viewHolder.getTextView().setText(localDataSet.get(position));



        viewHolder.getPhoto().setImageResource(getIdByName(localDataSet.get(position)));


        String rubles;
        String cents;
        Money price = getMoneyByName(localDataSet.get(position));
        rubles = String.valueOf(price.getRubles());
        cents = String.valueOf(price.getCents());

        viewHolder.getPrice().setText(rubles + "руб " + cents + "коп");
    }


    private int getIdByName(String name) {
        for (int i = 0; i < BuysManager.possibleItems.size(); i++) {
            if (Objects.equals(name, BuysManager.possibleItems.get(i)) || Objects.equals(name, BuysManager.possibleItems.get(i).split(" ")[0])) {
                return itemIds[i];
            }
        }
        return R.drawable.unknown_product;
    }

    private Money getMoneyByName(String name) {
        for (String key: BuysManager.prices.keySet()) {
            if (Objects.equals(name, key) || Objects.equals(name, key.split(" ")[0])) {
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
    }

    /**
     *This interface is used to catch click on element
     */
    public interface OnNoteListener {
        void OnNoteClick(int position);
    }
}
