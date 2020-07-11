package com.hzz.thenewslocal.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.ui.activity.NewsPublishActivity;


public class MeFragment extends Fragment implements View.OnClickListener{
private TextView contextEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_me, container, false);
        contextEdit=view.findViewById(R.id.contextEdit);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contextEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contextEdit:
              tocontextEdit();
                break;
    }



}

    private void tocontextEdit() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewsPublishActivity.class);
        startActivity(intent);
    }
}

