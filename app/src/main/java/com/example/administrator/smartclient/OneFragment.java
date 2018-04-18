package com.example.administrator.smartclient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * https://blog.csdn.net/yyhaohaoxuexi/article/details/77772498
 */
public class OneFragment extends Fragment {

    private ThreadPoolExecutor threadPoolExecutor;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private ImageView imageView;
    private Socket socket;


    @SuppressLint("HandlerLeak")
    private Handler mReceiveHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String[] results = ((String) msg.obj).split(",");
            if (results.length != 0) {
                tv1.setText("光感值" + results[2]);
                if ("0".equals(results[3])) {
                    tv2.setText("报警   否");
                    imageView.setBackgroundResource(R.drawable.no_police);
                } else {
                    tv2.setText("报警   是");
                    imageView.setBackgroundResource(R.drawable.police);
                }
                tv3.setText("温度" + results[0]);
                tv4.setText("湿度" + results[1]);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert container != null;
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_one, container, false);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        imageView = view.findViewById(R.id.iv2);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
               // receiveMsg();
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


    }

    private void receiveMsg() {
        try {
            Socket receiveSocket = new Socket("192.168.1.113", 6000);
            InputStream inputStream = receiveSocket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String result = null;
            while (!((result = bufferedReader.readLine()) == null)) {
                result = bufferedReader.readLine();
                Log.i("ljn", "receiveMsg: " + result);
                if (!TextUtils.isEmpty(result) && result.length() > 1) {
                    Message msg = Message.obtain();
                    msg.obj = result;
                    mReceiveHandler.sendMessage(msg);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("ljn", "receiveMsg: " + e.toString());
        }
    }

    public static OneFragment newInstance() {
        Bundle args = new Bundle();
        OneFragment fragment = new OneFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
