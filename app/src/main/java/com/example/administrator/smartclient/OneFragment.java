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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * https://blog.csdn.net/yyhaohaoxuexi/article/details/77772498
 */
public class OneFragment extends Fragment implements View.OnClickListener {


    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv_monitor;
    private ImageView imageView;
    private int type;
    private boolean isLightA;
    private boolean isLightB;
    private boolean isFanA;
    private boolean isFanB;
    private Socket socket;
    private Handler mHandler = new Handler();
    Runnable r = new Runnable() {

        @Override
        public void run() {
            //do something

            switch (type) {
                case 0:
                    sendMsg("0");
                    break;
                case 1:
                    if (isLightA) {
                        //开
                        sendMsg("1");
                    } else {
                        //关
                        sendMsg("2");
                    }
                    break;
                case 2:
                    if (isLightB) {
                        //开
                        sendMsg("3");
                    } else {
                        //关
                        sendMsg("4");
                    }
                    break;
                case 3:
                    if (isFanA) {
                        //开
                        sendMsg("5");
                    } else {
                        //关
                        sendMsg("6");
                    }
                    break;
                case 4:
                    if (isFanB) {
                        //开
                        sendMsg("7");
                    } else {
                        //关
                        sendMsg("8");
                    }
                    break;
                case 5:
                    break;
                default:
                    break;
            }
            mHandler.postDelayed(this, 3000);
        }
    };

    private void connectServer() {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    socket = new Socket("10.22.1.175", 6000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private synchronized void sendMsg2(final String num) {
        Log.i("ljn", "sendMsg: " + num);
        if (socket != null && !socket.isConnected()) {
            connectServer();
        }
        if (socket != null) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(num.getBytes("utf-8"));
                        outputStream.flush();
                        outputStream.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void sendMsg(final String num) {
        Log.i("ljn", "run: " + num);
        if (socket != null && !socket.isConnected()) {
            connectServer();
        }

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket != null) {
                        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                        printWriter.println(num);
                        printWriter.flush();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mReceiveHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String[] results = ((String) msg.obj).split(",");
            try {
                if (results.length != 0) {

                    tv1.setText("光感值" + results[0]);
                    if ("1".equals(results[1])) {
                        tv2.setText("报警   是");
                        imageView.setBackgroundResource(R.drawable.police);
                    } else {
                        tv2.setText("报警   否");
                        imageView.setBackgroundResource(R.drawable.no_police);
                    }

                    tv3.setText("温度" + results[2]);
                    tv4.setText("湿度" + results[3]);


                }
            } catch (Exception ignored) {
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
        tv_monitor = view.findViewById(R.id.tv_monitor);
        tv_monitor.setOnClickListener(this);
        SwitchButton sb_btn_time = view.findViewById(R.id.sb_btn_time);
        sb_btn_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    connectServer();
                }
            }
        });


        ImageView lightbulb_a = view.findViewById(R.id.lightbulb_a);
        ImageView lightbulb_b = view.findViewById(R.id.lightbulb_b);
        ImageView fan_a = view.findViewById(R.id.fan_a);
        ImageView fan_b = view.findViewById(R.id.fan_b);
        lightbulb_a.setOnClickListener(this);
        lightbulb_b.setOnClickListener(this);
        fan_a.setOnClickListener(this);
        fan_b.setOnClickListener(this);
        imageView = view.findViewById(R.id.iv2);
        return view;
    }

    public static OneFragment newInstance() {
        Bundle args = new Bundle();
        OneFragment fragment = new OneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ThreadPoolExecutor threadPoolExecutor;

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

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                receiveMsg();
            }
        });


    }

    private void receiveMsg() {
        try {
            Socket receiveSocket = new Socket("10.22.1.175", 6000);
            for (; ; ) {
                InputStream inputStream = receiveSocket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String result = bufferedReader.readLine();
                Log.i("ljn", "receiveMsg: " + result);
                if (!TextUtils.isEmpty(result) && result.length() > 1) {
                    Message msg = Message.obtain();
                    msg.obj = result;
                    mReceiveHandler.sendMessage(msg);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lightbulb_a:
                type = 1;
                isLightA = !isLightA;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(r, 100);
                break;
            case R.id.lightbulb_b:
                type = 2;
                isLightB = !isLightB;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(r, 100);
                break;
            case R.id.fan_a:
                isFanA = !isFanA;
                type = 3;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(r, 100);
                break;
            case R.id.fan_b:
                type = 4;
                isFanB = !isFanB;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(r, 100);
                break;
            case R.id.tv_monitor:
                type = 0;
                tv_monitor.setEnabled(false);
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(r, 100);
                break;
            default:
                break;
        }
    }
}
