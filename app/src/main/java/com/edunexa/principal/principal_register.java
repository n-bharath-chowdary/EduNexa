package com.edunexa.principal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class principal_register extends AppCompatActivity {

    private String email, password, name, college_name;
    private TextView princi_Login;
    private RelativeLayout relativeLayout;
    private AutoCompleteTextView select_princi_college;
    private EditText email_id, pass, princi_name;
    private MyApi db;
    private String[] clg_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        princi_Login = findViewById(R.id.princi_login_link);
        relativeLayout = findViewById(R.id.princi_register);
        email_id = findViewById(R.id.princi_email);
        pass = findViewById(R.id.princi_password);
        princi_name = findViewById(R.id.princi_name);
        select_princi_college = findViewById(R.id.princi_college_select);
        
        db = RetrofitInstance.getInstance().create(MyApi.class);

        clg_list = getResources().getStringArray(R.array.clg_lst);
        ArrayAdapter<String> college_adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, clg_list);
        select_princi_college.setAdapter(college_adapter);
        select_princi_college.setThreshold(2);

        princi_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });
    }

    private void validateInput() {
        email = email_id.toString();
        password = pass.toString();
        name = princi_name.toString();
        college_name = select_princi_college.toString();

        if(email.isEmpty()) {
            setWarning();
        } else if (password.isEmpty()) {
            setWarning();
        }  else if (name.isEmpty()) {
            setWarning();
        }
        else {
            Call<List<Princi_Users_data>> all_users = db.getPrinci_Users();
            all_users.enqueue(new Callback<List<Princi_Users_data>>() {
                @Override
                public void onResponse(Call<List<Princi_Users_data>> call, Response<List<Princi_Users_data>> response) {
                    List<Princi_Users_data> users = response.body();
                    for (Princi_Users_data user : users) {
                        if (email.equals(user.getEmail())) {
                            setWarning2();
                            break;
                        } else {
                            creatUser();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Princi_Users_data>> call, Throwable t) {
                    Toast.makeText(principal_register.this, "Failed to create user, Try again Later", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void creatUser() {
        Princi_Users_data users = new Princi_Users_data();
        users.setEmail(email);
        users.setPassword(password);
        users.setStatus(Boolean.FALSE);
        users.setName(name);
        users.setCollege_name(college_name);

        Call<String> call = db.addPrinciusers(users);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                setWarning1();
                openLogin();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(principal_register.this, "User cannot be created, please try again later..!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage("Invalid credentials, Please check the details..!");
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
        builder.setMessage("User has been Created, Please wait until university approves you..!");
        builder.setTitle("Successful...!").setCancelable(true);

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

    private void setWarning2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage("Email already exists, Please use another..1");
        builder.setTitle("Failed...!").setCancelable(true);

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

    private void openLogin() {
        startActivity(new Intent(this, principal_login.class));
        finish();
    }
}
