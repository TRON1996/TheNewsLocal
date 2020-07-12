package com.hzz.thenewslocal.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.ui.activity.LoginActivity;
import com.hzz.thenewslocal.ui.activity.NewsPublishActivity;
import com.hzz.thenewslocal.ui.activity.UserInfoActivity;

import static android.content.Context.MODE_PRIVATE;


public class MeFragment extends Fragment implements View.OnClickListener {
    private TextView contextEdit;
    private TextView meName;
    private TextView userInfoChange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        contextEdit = view.findViewById(R.id.contextEdit);
        meName = view.findViewById(R.id.mename);
        userInfoChange = view.findViewById(R.id.userinfochange);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contextEdit.setOnClickListener(this);
        userInfoChange.setOnClickListener(this);
        MeinfoLoad();

    }

    private void MeinfoLoad() {
        SharedPreferences spMe = getActivity().getSharedPreferences("logindata", MODE_PRIVATE);
        String spName = spMe.getString("name", "");
        Log.i("AAAA", "我的里面的数据" + spName);
        meName.setText(spName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contextEdit:
                tocontextEdit();
                break;
            case R.id.userinfochange:
                UserINfoChange();
                break;
        }


    }

    private void UserINfoChange() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), UserInfoActivity.class);
        startActivity(intent);
    }

    private void tocontextEdit() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewsPublishActivity.class);
        startActivity(intent);
    }
}

