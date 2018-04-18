package com.example.administrator.smartclient;

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

public class RegisterActivity extends AppCompatActivity {

    private EditText et_email;
    private EditText et_name;
    private EditText et_pwd;
    private EditText et_confirm_pwd;
    private TextView tv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        et_email = findViewById(R.id.et_email);
        et_name = findViewById(R.id.et_name);
        et_pwd = findViewById(R.id.et_pwd);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("注册");
        et_confirm_pwd = findViewById(R.id.et_confirm_pwd);
    }


    private void register(String email_text, String psw, String name_text) {
        RequestParams params = new RequestParams();
        params.put("password", psw);
        params.put("name", name_text);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post("http://10.22.1.58:8080/register", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                Log.i("ljn", "onFailure: " + throwable.toString());
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.i("ljn", "onSuccess: " + responseString);
                if ("success".equals(responseString)) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                }
            }


        });
    }

    public void onClickRegister(View view) {
        String email_text = et_email.getText().toString();
        String name_text = et_name.getText().toString();
        String psw_text = et_pwd.getText().toString();
        String confirm_text = et_confirm_pwd.getText().toString();
        if (TextUtils.isEmpty(email_text) ||
                TextUtils.isEmpty(name_text) ||
                TextUtils.isEmpty(psw_text) ||
                TextUtils.isEmpty(confirm_text)) {
            Toast.makeText(this, "请输入", Toast.LENGTH_LONG).show();
        } else if (!confirm_text.equals(psw_text)) {
            Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_LONG).show();
        } else {
            register(email_text, psw_text, confirm_text);
        }
    }
}
