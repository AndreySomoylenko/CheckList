package ru.samsung.case2022.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.samsung.case2022.R;
import ru.samsung.case2022.db.BuysManager;

/**
 * The CustomAdapter class
 * @author Ismail Velidzhanov
 * This class is used to work with RecyclerView
 */

public class BagAdapter extends RecyclerView.Adapter<BagAdapter.ViewHolder> {



    private final List<String> localDataSet;
    private final Context context;

    private final OnNoteListener mOnNoteListener;


    /**
     * it is the constructor of our adapter
     * @param localDataSet is the user's list of products
     * @param context is the application context
     * @param onNoteListener is the api we use to catch click on element
     */
    public BagAdapter(List<String> localDataSet, Context context, OnNoteListener onNoteListener) {
        this.localDataSet = localDataSet;
        this.context = context;
        this.mOnNoteListener = onNoteListener;
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
        switch (localDataSet.get(position)) {
            case "biscuits":
                viewHolder.getPhoto().setImageResource(R.drawable.biscuits);
                break;
            case "broccoli":
                viewHolder.getPhoto().setImageResource(R.drawable.broccoli);
                break;
            case "cheese":
                viewHolder.getPhoto().setImageResource(R.drawable.cheese);
                break;
            case "coffee":
                viewHolder.getPhoto().setImageResource(R.drawable.coffee);
                break;
            case "curd":
                viewHolder.getPhoto().setImageResource(R.drawable.curd);
                break;
            case "dough":
                viewHolder.getPhoto().setImageResource(R.drawable.dough);
                break;
            case "milk":
                viewHolder.getPhoto().setImageResource(R.drawable.milk);
                break;
            case "pancakes":
                viewHolder.getPhoto().setImageResource(R.drawable.pancakes);
                break;
            case "sourcream":
                viewHolder.getPhoto().setImageResource(R.drawable.sourcream);
                break;
            case "tea":
                viewHolder.getPhoto().setImageResource(R.drawable.tea);
                break;
            default:
                viewHolder.getPhoto().setImageResource(R.drawable.unknown_product);
                break;
        }
        String rubles;
        String cents;
        BuysManager buy = new BuysManager();
        if (buy.prices.get(localDataSet.get(position)) != null) {
            double price = buy.prices.get(localDataSet.get(position));
            rubles = String.valueOf((int) price);
            cents = String.valueOf((int)((price % 1) * 100));
        } else {
            rubles = "0";
            cents = "0";
        }
        viewHolder.getPrice().setText(rubles + "руб " + cents + "коп");
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
