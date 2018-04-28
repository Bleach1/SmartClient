package com.example.administrator.smartclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {


    RadioGroup radioGroup;
    private Fragment[] mFragments;
    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.rg_container);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                for (int index = 0; index < group.getChildCount(); index++) {
                    RadioButton rb = (RadioButton) group.getChildAt(index);
                    //如果被选中
                    if (rb.isChecked()) {
                        setIndexSelected(index);
                        break;
                    }
                }
            }
        });
        initFragment();
    }

    private void initFragment() {

        //添加到数组
        mFragments = new Fragment[]{OneFragment.newInstance(), TwoFragment.newInstance(),
                FourFragment.newInstance()};

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, mFragments[0])
                .add(R.id.fl_container, mFragments[1])
                .add(R.id.fl_container, mFragments[2])
                .hide(mFragments[1])
                .hide(mFragments[2])
                .show(mFragments[0]).commit();
        //默认设置为第0个
        setIndexSelected(0);
    }

    private void setIndexSelected(int index) {

        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.fl_container, mFragments[index]);
        }
        ft.show(mFragments[index]).commit();
        //再次赋值
        mIndex = index;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // App.getInstance().removeAll("name");
    }
}
