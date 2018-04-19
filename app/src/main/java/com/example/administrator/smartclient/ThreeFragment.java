package com.example.administrator.smartclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @description:
 * @author: ljn
 * @time: 2018/4/19
 */
public class ThreeFragment extends BaseFragment implements View.OnClickListener {
    private boolean isLightA;
    private boolean isLightB;
    private boolean isFanA;
    private boolean isFanB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_three, container, false);
        ImageView lightbulb_a = view.findViewById(R.id.lightbulb_a);
        ImageView lightbulb_b = view.findViewById(R.id.lightbulb_b);
        ImageView fan_a = view.findViewById(R.id.fan_a);
        ImageView fan_b = view.findViewById(R.id.fan_b);
        lightbulb_a.setOnClickListener(this);
        lightbulb_b.setOnClickListener(this);
        fan_a.setOnClickListener(this);
        fan_b.setOnClickListener(this);
        return view;
    }

    public static ThreeFragment newInstance() {
        Bundle args = new Bundle();
        ThreeFragment fragment = new ThreeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        //激活
        sendMsg("唤醒");


        switch (v.getId()) {
            case R.id.lightbulb_a:
                if (!isLightA) {
                    sendMsg("1");
                } else {
                    sendMsg("2");
                }
                isLightA = !isLightA;

                break;
            case R.id.lightbulb_b:
                if (!isLightB) {
                    sendMsg("5");
                } else {
                    sendMsg("6");
                }
                isLightB = !isLightB;
                break;
            case R.id.fan_a:
                if (!isFanA) {
                    sendMsg("3");
                } else {
                    sendMsg("4");
                }
                isFanA = !isFanA;
                break;
            case R.id.fan_b:
                if (!isFanB) {
                    sendMsg("7");
                } else {
                    sendMsg("8");
                }
                isFanB = !isFanB;
                break;
            default:
                break;
        }
    }
}
