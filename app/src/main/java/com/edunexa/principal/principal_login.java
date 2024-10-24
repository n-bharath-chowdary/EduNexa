package com.edunexa.principal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.edunexa.data.Princi_Users_data;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class principal_login extends AppCompatActivity {

    private RelativeLayout princi_login;
    private String id, password;
    private EditText princi_id, princi_password;
    private TextView princi_register;
    private MyApi db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        princi_id = findViewById(R.id.princi_id);
        princi_password = findViewById(R.id.princi_password);
        princi_login = findViewById(R.id.princi_login);
        princi_register = findViewById(R.id.princi_register);

        db = RetrofitInstance.getInstance().create(MyApi.class);

        princi_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        princi_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });
    }

    private void openRegister() {
        startActivity(new Intent(this, principal_register.class));
        finish();
    }

    private void check() {
        id = princi_id.getText().toString();
        password = princi_password.getText().toString();

        if(id.isEmpty()){
            setWarning();
        } else if (password.isEmpty()) {
            setWarning();
        } else {
            validateInput();
        }
    }

    private void validateInput() {
        Call<List<Princi_Users_data>> call = db.getPrinci_Users();
        call.enqueue(new Callback<List<Princi_Users_data>>() {
            @Override
            public void onResponse(Call<List<Princi_Users_data>> call, Response<List<Princi_Users_data>> response) {
                Toast.makeText(principal_login.this, "Login in Please Wait..!", Toast.LENGTH_SHORT).show();
                List<Princi_Users_data> users = response.body();
                for (Princi_Users_data user : users) {
                    if (id.equals(user.getName()) && password.equals(user.getPassword())) {
                        if(user.getStatus() == Boolean.TRUE) {
                            openPrinciMain();
                        } else {
                            setWarning1();
                        }
                        break;
                    } else {
                        setWarning();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Princi_Users_data>> call, Throwable t) {
                Toast.makeText(principal_login.this, "Unable to get data, please try again later..!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openPrinciMain() {
        startActivity(new Intent(this, princi_main.class));
        finish();
    }

    private void setWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage("No user found, try again or register");
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

    private void setWarning1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage("Approval is still in process, please contact University..!");
        builder.setTitle("Waiting...!").setCancelable(true);

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
}