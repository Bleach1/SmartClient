package com.example.administrator.smartclient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.apache.http.Header;

import javax.net.ssl.SSLSocketFactory;

public class TwoFragment extends Fragment {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_two, container, false);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);
        tv5 = view.findViewById(R.id.tv5);
        tv6 = view.findViewById(R.id.tv6);
        tv7 = view.findViewById(R.id.tv7);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWeather();
    }

    private void getWeather() {

        OkGo.<String>get("https://www.sojson.com/open/api/weather/json.shtml?city=大连").tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("ljn", "onSuccess: " + response.body());
                        final Weather weather = new Gson().fromJson(response.body(), Weather.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fillData(weather);
                            }
                        });

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("ljn", "onError: " + response.body());
                    }
                });


    }


    @SuppressLint("SetTextI18n")
    private void fillData(Weather weather) {
        tv1.setText(weather.getCity());
        Weather.DataBean.ForecastBean forecastBean = weather.getData().getForecast().get(0);
        tv2.setText(forecastBean.getFx() + "---" + forecastBean.getFl());
        tv3.setText(forecastBean.getLow() + "---" + forecastBean.getHigh());
        tv4.setText(weather.getData().getShidu());
        tv5.setText(forecastBean.getDate());
        tv6.setText(forecastBean.getType());
        tv7.setText(forecastBean.getNotice());

    }

    public static TwoFragment newInstance() {
        Bundle args = new Bundle();
        TwoFragment fragment = new TwoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
