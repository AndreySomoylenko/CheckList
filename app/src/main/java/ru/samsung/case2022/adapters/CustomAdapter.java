package ru.samsung.case2022.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.samsung.case2022.R;

/**
 * The CustomAdapter class
 * @author Dmitry Kovalchuk
 * This class is used to work with RecyclerView
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {



    private final List<String> localDataSet;
    private final Context context;

    private final OnNoteListener mOnNoteListener;


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
        TextView text;

        OnNoteListener onNoteListener;

        /**
         * @param itemView
         * @param onNoteListener
         */
        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            text = itemView.findViewById(R.id.productName);

            itemView.setOnClickListener(this);
            this.onNoteListener = onNoteListener;
        }

        /**
         * @return
         */
        public TextView getTextView() {
            return text;
        }

        /**
         * @param view
         */
        @Override
        public void onClick(View view) {
            onNoteListener.OnNoteClick(getAdapterPosition());
        }
    }

    /**
     *This interface is used to hear catch click on element
     */
    public interface OnNoteListener {
        void OnNoteClick(int position);
    }
}
