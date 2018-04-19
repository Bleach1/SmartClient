package com.example.administrator.smartclient;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by asus on 2018/4/18.
 */

public class BaseFragment extends Fragment {


    private ThreadPoolExecutor threadPoolExecutor;
    private Socket socket;

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


    public void receiveMsg() {
        try {
            socket = new Socket("10.22.1.58", 6000);
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String result = null;
            while (bufferedReader.readLine() != null) {
                result = bufferedReader.readLine();
                Log.i("ljn", "receiveMsg: " + result);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("ljn", "IOException: " + e.toString());
        }
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


    public void sendMsg(final String num) {

        if (!socket.isConnected()) {
            connectServer();
        }

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket != null) {
                        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                        printWriter.println(num );
                        printWriter.flush();
                        Log.i("ljn", "run: " + num);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
