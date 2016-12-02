package com.juangdiaz.apptest.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juangdiaz.apptest.BaseApplication;
import com.juangdiaz.apptest.R;
import com.juangdiaz.apptest.presenter.UserPresenter;
import com.juangdiaz.apptest.view.UserView;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserFragment extends Fragment implements UserView {

    @Inject UserPresenter userPresenter;

    @BindView(R.id.title) TextView name;
    @BindView(R.id.title) TextView phone;
    @BindView(R.id.title) TextView email;

    public UserFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication)getActivity().getApplication()).getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
