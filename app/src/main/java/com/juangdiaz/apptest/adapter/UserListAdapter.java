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
import com.juangdiaz.apptest.model.User;
import com.juangdiaz.apptest.view.activity.SentHistoryActivity;

import java.util.List;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ListViewHolder> {

    private Context context;
    private final List<User> users;


    public static class ListViewHolder extends RecyclerView.ViewHolder {

        public User currentItem;
        private final Context context;

        public TextView textViewName;
        public TextView textViewPhone;
        public TextView textViewEmail;


        public ListViewHolder(View view) {

            super(view);

            context = view.getContext();
            textViewName = (TextView) view.findViewById(R.id.text_view_name);
            textViewPhone = (TextView) view.findViewById(R.id.text_view_phone);
            textViewEmail = (TextView) view.findViewById(R.id.text_view_email);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showUser(context, currentItem);
                }
            });
        }
    }

    public UserListAdapter(Context context, List<User> list) {

        this.users = list;
        this.context = context;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        final View view = LayoutInflater.from(context).inflate(R.layout.view_user, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder viewHolder, int position) {

        viewHolder.currentItem = users.get(position);
        viewHolder.textViewName.setText(viewHolder.currentItem.getFirstName());
        viewHolder.textViewEmail.setText(viewHolder.currentItem.getEmail());
        viewHolder.textViewPhone.setText(viewHolder.currentItem.getPhoneNumb());
    }

    @Override
    public int getItemCount() {

        return users.size();
    }


    private static void showUser(Context context, User user) {

       Intent intent = new Intent(context, SentHistoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SentHistoryActivity.PARAM_USER, user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
