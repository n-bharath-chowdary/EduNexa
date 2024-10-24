package com.edunexa.teacher;

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
import com.edunexa.data.teacher_user_data;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class teacher_login extends AppCompatActivity {
    private String des;
    private TextView welcome;
    private RelativeLayout teacher_login;
    private String id, password;
    private EditText teacher_id, teacher_password;
    private TextView teacher_register;
    private MyApi db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        teacher_id = findViewById(R.id.teacher_id);
        teacher_password = findViewById(R.id.teacher_password);
        teacher_login = findViewById(R.id.teacher_login);
        teacher_register = findViewById(R.id.teacher_register);
        welcome = findViewById(R.id.welcome);

        db = RetrofitInstance.getInstance().create(MyApi.class);

        des = getIntent().getStringExtra("des");

        switch (des) {
            case "guest":
                welcome.setText(getResources().getString(R.string.welcome_guest_teacher));
                break;
            case "permanent":
                welcome.setText(getResources().getString(R.string.welcome_permanent_teacher));
                break;
            case "non":
                welcome.setText(getResources().getString(R.string.welcome_non_teaching_teacher));
                break;
        }

        teacher_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        teacher_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

    }

    private void openRegister() {
        Intent intent = new Intent(teacher_login.this, teacher_register.class);
        startActivity(intent);
        finish();
    }

    private void check() {
        id = teacher_id.getText().toString();
        password = teacher_password.getText().toString();
        if (id.isEmpty()) {
            setWarning();
        } else if (password.isEmpty()) {
            setWarning();
        } else {
            validateInput();
        }
    }

    private void validateInput() {
        Call<List<teacher_user_data>> call = db.get_teacher_Users();
        call.enqueue(new Callback<List<teacher_user_data>>() {
            @Override
            public void onResponse(Call<List<teacher_user_data>> call, Response<List<teacher_user_data>> response) {
                Toast.makeText(teacher_login.this, "Login in Please Wait..!", Toast.LENGTH_SHORT).show();
                List<teacher_user_data> users = response.body();
                for (teacher_user_data user : users) {
                    if (id.equals(user.getName()) && password.equals(user.getPassword()) && des.equals(user.getDes())) {
                        if(user.isStatus() == Boolean.TRUE) {
                            des = user.getDes();
                            openTeacherMain(des);
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
            public void onFailure(Call<List<teacher_user_data>> call, Throwable t) {
                Toast.makeText(teacher_login.this, "Unable to get data, please try again later..!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openTeacherMain(String des) {
        Intent intent = new Intent(teacher_login.this, teacher_main.class);
        intent.putExtra("des", des);
        startActivity(intent);
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