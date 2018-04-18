package com.example.administrator.smartclient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreeFragment extends Fragment implements View.OnClickListener {


    private ThreadPoolExecutor threadPoolExecutor;
    private Socket socket;
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

    private void connectServer() {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    socket = new Socket("10.22.1.58", 6000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMsg(final String num) {

        if (!socket.isConnected()) {
            connectServer();
        }

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket != null) {
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(num.getBytes("utf-8"));
                        outputStream.flush();
                        // outputStream.close();
                        //socket.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("HardWareClient");
                return thread;
            }
        };
        threadPoolExecutor = new ThreadPoolExecutor(5,
                200,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(1024),
                threadFactory
        );

        connectServer();
    }

    public static ThreeFragment newInstance() {
        Bundle args = new Bundle();
        ThreeFragment fragment = new ThreeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
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
