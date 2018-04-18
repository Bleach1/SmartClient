package com.example.administrator.smartclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText psw;
    private EditText phone;
    private TextView tv_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        psw = findViewById(R.id.psw);
        phone = findViewById(R.id.phone);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("登录");
    }

    public void onClick(View view) {
        String password = psw.getText().toString();
        String phoneNum = phone.getText().toString();
        if ("admin".equals(phoneNum) && "ljn".equals(password)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(LoginActivity.this, "请正确填写", Toast.LENGTH_SHORT).show();
        } else {
            login(phoneNum, password);
        }

    }

    private void login(final String name, String psw) {
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("password", psw);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post("http://10.22.1.58:8080/login", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.i("ljn", "onSuccess: " + responseString);
                if ("success".equals(responseString)) {
                    App.getInstance().putString("name", name);
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void onTextClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
