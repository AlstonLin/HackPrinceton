package io.alstonlin.hackprinceton;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
