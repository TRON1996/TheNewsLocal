package com.hzz.thenewslocal.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hzz.thenewslocal.R;
import com.hzz.thenewslocal.ui.fragment.HomeFragment;
import com.hzz.thenewslocal.ui.fragment.MeFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView ivHome;
    private ImageView ivMe;
    private HomeFragment homeFragment;
    private MeFragment meFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        ivHome=findViewById(R.id.ivHome);
        ivMe=findViewById(R.id.ivMe);
        ivHome.setOnClickListener(this);
        ivMe.setOnClickListener(this);
        homeFragment=new HomeFragment();
        meFragment=new MeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.flcontent,homeFragment).commit();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); // import android.support.v4.app.FragmentTransaction;



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivHome:
                if(homeFragment==null)homeFragment=new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flcontent,homeFragment).commit();
                break;
            case R.id.ivMe:
                if(meFragment==null)meFragment=new MeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flcontent,meFragment).commit();
                break;
        }
    }
}
