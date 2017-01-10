package com.will.custom_rxandroid.ui.slidefinish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.will.custom_rxandroid.R;

import java.util.ArrayList;
import java.util.List;

public class SlideFinishActivity extends AppCompatActivity implements SlidingLayout.OnActivityFinishListener {

    private List<String> list = new ArrayList<String>();

    SlidingLayout slidingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_finish);

        slidingLayout = (SlidingLayout) findViewById(R.id.sildingFinishLayout);
        slidingLayout.attachActivity(this);
        slidingLayout.setOnActivityFinish(this);

        for (int i = 0; i <= 30; i++) {
            list.add("测试数据" + i);
        }

        ListView mListView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                SlideFinishActivity.this, android.R.layout.simple_list_item_1, list);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });
    }

    // Press the back button in mobile phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onActivityFinish() {
        finish();
    }
}
