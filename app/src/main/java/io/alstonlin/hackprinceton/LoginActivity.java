package io.alstonlin.hackprinceton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(Color.parseColor("#007E92"));
        setContentView(R.layout.activity_login);
    }

    public void login(View v){
        String username = String.valueOf(((EditText)findViewById(R.id.username)).getText());
        String password = String.valueOf(((EditText)findViewById(R.id.username)).getText());
        DAO.getInstance().login(username, password, this);
    }

    public void signup(View v){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
