package ru.samsung.case2022.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.samsung.case2022.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final ArrayList<String> localDataSet;
    private final Context context;

    private final OnNoteListener mOnNoteListener;


    public CustomAdapter(ArrayList<String> localDataSet, Context context, OnNoteListener onNoteListener) {
        this.localDataSet = localDataSet;
        this.context = context;
        this.mOnNoteListener = onNoteListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_layout, viewGroup, false);

        return new ViewHolder(view, mOnNoteListener );
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getTextView().setText(localDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView text;

        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            text = itemView.findViewById(R.id.productName);

            itemView.setOnClickListener(this);
            this.onNoteListener = onNoteListener;
        }

        public TextView getTextView() {
            return text;
        }

        @Override
        public void onClick(View view) {
            onNoteListener.OnNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void OnNoteClick(int position);
    }
}
