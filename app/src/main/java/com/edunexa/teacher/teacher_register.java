package com.edunexa.teacher;

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
import com.edunexa.principal.principal_login;
import com.edunexa.data.teacher_user_data;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class teacher_register extends AppCompatActivity {
    private String email, password, name, college_name, des;
    private TextView teacher_Login, welcome;
    private RelativeLayout relativeLayout;
    private AutoCompleteTextView select_teacher_college;
    private EditText email_id, pass, teacher_name;
    private MyApi db;
    private String[] clg_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        teacher_Login = findViewById(R.id.teacher_login_link);
        relativeLayout = findViewById(R.id.teacher_register);
        email_id = findViewById(R.id.teacher_email);
        pass = findViewById(R.id.teacher_password);
        teacher_name = findViewById(R.id.teacher_name);
        select_teacher_college = findViewById(R.id.teacher_college_select);

        welcome = findViewById(R.id.welcome);

        des = getIntent().getStringExtra("des");
        db = RetrofitInstance.getInstance().create(MyApi.class);

        clg_list = getResources().getStringArray(R.array.clg_lst);
        ArrayAdapter<String> college_adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, clg_list);
        select_teacher_college.setAdapter(college_adapter);
        select_teacher_college.setThreshold(2);

        switch (des) {
            case "guest":
                welcome.setText(getResources().getText(R.string.welcome_guest_teacher));
                break;
            case "permanent":
                welcome.setText(getResources().getText(R.string.welcome_permanent_teacher));
                break;
            case "non":
                welcome.setText(getResources().getText(R.string.welcome_non_teaching_teacher));
                break;
        }

        teacher_Login.setOnClickListener(new View.OnClickListener() {
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

    private void creatUser() {
        teacher_user_data users = new teacher_user_data();
        users.setEmail(email);
        users.setPassword(password);
        users.setStatus(Boolean.FALSE);
        users.setName(name);
        users.setCollege(college_name);
        users.setDes(des);

        Call<String> call = db.addTeacherusers(users);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                setWarning1();
                openLogin();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(teacher_register.this, "User cannot be created, please try again later..!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateInput() {
        email = email_id.toString();
        password = pass.toString();
        name = teacher_name.toString();
        college_name = select_teacher_college.toString();

        if (email.isEmpty()) {
            setWarning();
        } else if (password.isEmpty()) {
            setWarning();
        } else if (name.isEmpty()) {
            setWarning();
        } else {
            Call<List<teacher_user_data>> all_users = db.get_teacher_Users();
            all_users.enqueue(new Callback<List<teacher_user_data>>() {
                @Override
                public void onResponse(Call<List<teacher_user_data>> call, Response<List<teacher_user_data>> response) {
                    List<teacher_user_data> users = response.body();
                    for (teacher_user_data user : users) {
                        if (email.equals(user.getEmail())) {
                            setWarning2();
                            break;
                        } else {
                            creatUser();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<teacher_user_data>> call, Throwable t) {
                    Toast.makeText(teacher_register.this, "Failed to create user, Try again Later", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
        builder.setMessage("User has been Created, Please wait until Principal approves you..!");
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
        builder.setMessage("Email already exists, Please use another..!");
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