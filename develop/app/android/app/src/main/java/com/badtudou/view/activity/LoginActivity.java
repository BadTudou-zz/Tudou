package com.badtudou.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.badtudou.tudou.R;
import com.badtudou.util.Rest;
import com.badtudou.model.ResultCallBack;
import com.badtudou.controller.User;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public static final String ACTION_Login = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button btn_login = (Button)findViewById(R.id.button_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, password;
                EditText edit_name, edit_password;
                User user = new User(new ResultCallBack() {
                    @Override
                    public void handleResult(String resultString) {
                        try {
                            JSONObject json = new JSONObject(resultString);
                            String action = json.getString(Rest.ACTION);
                            Boolean state = json.getString(Rest.STATE).equals("ok") ? true : false;
                            String message = json.getString(Rest.MSG);
                            switch (action) {
                                case ACTION_Login:
                                        btn_login.setEnabled(true);
                                        Toast toast = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG);
                                        toast.show();
                                        if(state){
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        }
                                    break;
                            }
                            Log.d("Test", json.getString(Rest.STATE));
                            Log.d("Test", json.getString(Rest.MSG));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                edit_name     = (EditText)findViewById(R.id.edit_username);
                edit_password = (EditText)findViewById(R.id.edit_userpassword);

                name     = edit_name.getText().toString();
                password = edit_password.getText().toString();

                btn_login.setEnabled(false);
                user.login(name, password);

            }
        });
    }

}
