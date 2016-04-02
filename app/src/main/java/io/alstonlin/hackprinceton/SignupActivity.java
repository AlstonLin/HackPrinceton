package io.alstonlin.hackprinceton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
