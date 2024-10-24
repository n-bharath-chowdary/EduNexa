package com.edunexa.rur;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.edunexa.MyApi;
import com.edunexa.R;
import com.edunexa.RetrofitInstance;
import com.edunexa.data.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_rur extends AppCompatActivity {

    private EditText rur_id, rur_password;
    private String getName, getPassword;
    private RelativeLayout rur_login_btn;
    private MyApi db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_rur);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = RetrofitInstance.getInstance().create(MyApi.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        rur_id = findViewById(R.id.rur_id);
        rur_password = findViewById(R.id.rur_password);
        rur_login_btn = findViewById(R.id.rur_login);

        rur_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_data();
            }
        });
    }
    private void get_data() {
        Call<List<Users>> call = db.getUsers();
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if(response.isSuccessful()){
                    Toast.makeText(Login_rur.this, "Login in Please Wait..!", Toast.LENGTH_SHORT).show();
                    List<Users> users = response.body();
                    validateInput(users);
                } else {
                    Toast.makeText(Login_rur.this, "Failed to connect..!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Toast.makeText(Login_rur.this, "Failed to connect..!", Toast.LENGTH_SHORT).show();
                Log.d("error", t.getMessage());
            }
        });
    }
    private void validateInput(List<Users> users) {
        getName = rur_id.getText().toString();
        getPassword = rur_password.getText().toString();

        boolean isValid = false;
        for (Users user : users) {
            if (getName.equals(user.getName()) && getPassword.equals(user.getPassword())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            setWarning();
        } else {
            Toast.makeText(this, "login Successful", Toast.LENGTH_SHORT).show();
            openrurMain();
        }
    }

    private void setWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage("Wrong credentials, Please check the details..!");
        builder.setTitle("Alert!!").setCancelable(true);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 900);
    }

    private void openrurMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}