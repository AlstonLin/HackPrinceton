package io.alstonlin.hackprinceton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(Color.parseColor("#007E92"));
        setContentView(R.layout.activity_signup);
    }

    public void signup(View v){
        String username = String.valueOf(((EditText)findViewById(R.id.username)).getText());
        String password = String.valueOf(((EditText)findViewById(R.id.username)).getText());
        DAO.getInstance().signup(username, password);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
