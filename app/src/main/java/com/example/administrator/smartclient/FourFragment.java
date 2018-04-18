package com.example.administrator.smartclient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class FourFragment extends Fragment {

    private EditText et_light;
    private EditText et_temp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_four, container, false);
        Button btn_save = view.findViewById(R.id.btn_save);
        et_light = view.findViewById(R.id.et_light);
        et_temp = view.findViewById(R.id.et_temp);
        final String key = App.getInstance().getString("name");
        getSetting(key);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_light.getText().toString()) || TextUtils.isEmpty(et_temp.getText().toString())) {
                    Toast.makeText(getActivity(), "请填写", Toast.LENGTH_LONG).show();
                } else {
                    insertValue(key, Integer.valueOf(et_temp.getText().toString()), Integer.valueOf(et_light.getText().toString()));
                }
            }
        });
        return view;
    }

    public static FourFragment newInstance() {
        Bundle args = new Bundle();
        FourFragment fragment = new FourFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void insertValue(String name, Integer temp, Integer prpr) {
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("temp", temp);
        params.put("prpr", prpr);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post("http://10.22.1.58:8080/insertValue", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getContext(), "保存失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.i("ljn", "onSuccess: " + responseString);
                if ("success".equals(responseString)) {
                    Toast.makeText(getContext(), "保存成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "保存失败", Toast.LENGTH_LONG).show();
                }

            }


        });
    }


    private void getSetting(String name) {
        RequestParams params = new RequestParams();
        params.put("name", name);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post("http://10.22.1.58:8080/getSetting", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                if (!"fail".equals(responseString)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        final int temp = jsonObject.getInt("temp");
                        final int prpr = jsonObject.getInt("prpr");
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                et_temp.setText(temp + "");
                                et_light.setText(prpr + "");
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


        });


    }


}
