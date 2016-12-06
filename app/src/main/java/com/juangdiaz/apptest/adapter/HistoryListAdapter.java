package com.juangdiaz.apptest.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juangdiaz.apptest.R;
import com.juangdiaz.apptest.model.Places;
import com.juangdiaz.apptest.view.activity.PlaceDetailsActivity;

import java.util.List;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ListViewHolder> {

    private Context context;
    final List<Places> places;

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        public Places currentItem;
        private final Context context;
        public TextView textViewName;
        public TextView textViewDate;


        public ListViewHolder(View view) {

            super(view);
            context = view.getContext();

            textViewName = (TextView) view.findViewById(R.id.text_view_place_name);
            textViewDate = (TextView) view.findViewById(R.id.text_view_history_date);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showHistory(context, currentItem);
                }
            });
        }
    }

    public HistoryListAdapter(Context context, List<Places> list) {

        this.places = list;
        this.context = context;
    }

    @Override
    public HistoryListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        final View view = LayoutInflater.from(context).inflate(R.layout.view_history, parent, false);
        return new HistoryListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HistoryListAdapter.ListViewHolder viewHolder, int position) {

        viewHolder.currentItem = places.get(position);
        viewHolder.textViewName.setText(viewHolder.currentItem.getName());
        viewHolder.textViewDate.setText("Date Sent: " + viewHolder.currentItem.getFormattedDate());

    }

    @Override
    public int getItemCount() {

        return places.size();
    }


    private static void showHistory(Context context, Places places) {

        Intent intent = new Intent(context, PlaceDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PlaceDetailsActivity.PARAM_PLACE, places.getPlaceid());
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
