package dsa.upc.edu.talesofeetacclient.View;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import dsa.upc.edu.talesofeetacclient.Controller.ApiAdapter;
import dsa.upc.edu.talesofeetacclient.Model.Main.User;
import dsa.upc.edu.talesofeetacclient.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private String username;
    private String password;
    protected static User user;
    protected static EditText usernameText;
    protected static EditText passwordText;
    private Button loginButton;
    protected Intent intent;
    private ProgressBar progressBar;
    private static TextView registerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        usernameText = (EditText) findViewById(R.id.nameText);
        passwordText = (EditText) findViewById(R.id.emailText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        intent = new Intent(getBaseContext(), MainActivity.class);
        registerView = findViewById(R.id.registerView);
        registerView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivityForResult(intent, 1000);
            }
        });
        usernameText.requestFocus();
    }
    @Override
    public void onClick(View view) {
        if (usernameText.getText().length() != 0 && passwordText.getText().length() != 0) {
            username = usernameText.getText().toString();
            password = passwordText.getText().toString();
            getUserLogin(username,password);
        }
    }

    private void getUserLogin(String username, String password) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(10);
        User u = new User();
        u.setName(username);
        u.setPassword(password); // "http://10.0.2.2:8080/talesofeetac/db/"
        Call<User> call = ApiAdapter.getApiService("http://10.193.96.32:8080/talesofeetac/db/").getUserLoginService(u);
        call.enqueue(new GetUserLoginCallback());
    }


    private class GetUserLoginCallback implements Callback<User> {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if (response.isSuccessful()) {
                progressBar.setProgress(20);
                Toast.makeText(getBaseContext(), "We've got connection!", Toast.LENGTH_SHORT).show();
                User user = response.body();
                progressBar.setProgress(30);
                intent.putExtra("data", user);
                progressBar.setProgress(100);
                progressBar.setVisibility(View.GONE);
                startActivityForResult(intent,1);
            } else if (response.errorBody() != null) {

                try {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getBaseContext(), "We've got NO connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case (1000): {
                if (resultCode == 1001) {
                    User user = intent.getParcelableExtra("data");
                    usernameText.setText(user.getName());
                    passwordText.setText(user.getPassword());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

