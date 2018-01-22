package dsa.upc.edu.talesofeetacclient.View;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
public class RegisterActivity extends Activity implements View.OnClickListener {

    private String username;
    private String password;
    private String email;
    protected User user;
    protected EditText usernameText;
    protected EditText passwordText;
    protected EditText emailText;
    private Button registerButton;
    protected Intent intent;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
        usernameText = findViewById(R.id.nameText);
        passwordText = findViewById(R.id.passwordText);
        emailText = findViewById(R.id.emailText);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        intent = getIntent();
        usernameText.requestFocus();

    }
    @Override
    public void onClick(View view) {
        if (usernameText.getText().length() != 0 && passwordText.getText().length() != 0 && emailText.getText().length() != 0) {
            username = usernameText.getText().toString();
            password = passwordText.getText().toString();
            email = emailText.getText().toString();
            getUserRegister(username,password, email);
        }
    }

    private void getUserRegister(String username, String password, String email) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(10);
        User u = new User();
        u.setName(username);
        u.setPassword(password);
        u.setEmail(email);
        Call<User> call = ApiAdapter.getApiService("http://10.193.96.32:8080/talesofeetac/db/").getUserRegisterService(u);
        call.enqueue(new GetUserRegisterCallback());
    }


    private class GetUserRegisterCallback implements Callback<User> {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if (response.isSuccessful()) {
                progressBar.setProgress(20);
                User user = response.body();
                Toast.makeText(getBaseContext(), "User "+user.getName()+" has been registered!", Toast.LENGTH_SHORT).show();
                progressBar.setProgress(30);
                intent.putExtra("data", user);
                progressBar.setProgress(100);
                progressBar.setVisibility(View.GONE);
                setResult(1001,intent);
                finish();
            } else if (response.errorBody() != null) {
               progressBar.setVisibility(View.GONE);
               Toast.makeText(getBaseContext(), "Duplicated username", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getBaseContext(), "Could't complete the registration", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

