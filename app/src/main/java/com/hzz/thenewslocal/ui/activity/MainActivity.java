package com.hzz.thenewslocal.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.ui.fragment.CollectFragment;
import com.hzz.thenewslocal.ui.fragment.HeatFragment;
import com.hzz.thenewslocal.ui.fragment.HomeFragment;
import com.hzz.thenewslocal.ui.fragment.MeFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivHome;
    private ImageView ivMe;
    private ImageView ivFeat;
    private ImageView ivCollect;
    private HeatFragment heatFragment;
    private HomeFragment homeFragment;
    private MeFragment meFragment;
    private CollectFragment collectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        ivHome = findViewById(R.id.ivHome);
        ivMe = findViewById(R.id.ivMe);
        ivFeat = findViewById(R.id.ivHot);
        ivCollect = findViewById(R.id.ivCollection);
        ivCollect.setOnClickListener(this);
        ivFeat.setOnClickListener(this);
        ivHome.setOnClickListener(this);
        ivMe.setOnClickListener(this);
        homeFragment = new HomeFragment();
        meFragment = new MeFragment();
        heatFragment = new HeatFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.flcontent, homeFragment).commit();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//不必须
        // import android.support.v4.app.FragmentTransaction;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivHome:
                if (homeFragment == null)
                    homeFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flcontent, homeFragment).commit();
                break;
            case R.id.ivMe:
                if (meFragment == null)
                    meFragment = new MeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flcontent, meFragment).commit();
                break;
            case R.id.ivHot:
                if (heatFragment == null)
                    heatFragment = new HeatFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flcontent, heatFragment).commit();
                break;
            case R.id.ivCollection:
                if (collectFragment == null)
                    collectFragment = new CollectFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flcontent, collectFragment).commit();
                break;
        }
    }
}
