package com.will.custom_rxandroid.ui.custome_view_viewdraphelper;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.will.custom_rxandroid.R;

public class DrawActivity extends AppCompatActivity {

    private LeftMenuFragment mLeftMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        FragmentManager fm = getSupportFragmentManager();
        mLeftMenuFragment = (LeftMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mLeftMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mLeftMenuFragment = new LeftMenuFragment())
                    .commit();
        }
    }
}
