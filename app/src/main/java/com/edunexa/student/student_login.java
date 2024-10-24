package com.edunexa.student;

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
import com.edunexa.data.Student_users_data;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class student_login extends AppCompatActivity {
    private RelativeLayout rl;
    private EditText student_email, student_pass;
    private String email, pass, course;
    private int year;
    private TextView register;
    private MyApi db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        db = RetrofitInstance.getInstance().create(MyApi.class);

        rl = findViewById(R.id.student_login);
        student_email = findViewById(R.id.student_id);
        student_pass = findViewById(R.id.student_password);
        register = findViewById(R.id.student_register);

        course = getIntent().getStringExtra("course");
        year = getIntent().getIntExtra("year", 1);

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openstudentRegister();
            }
        });

    }

    private void check() {
        email = student_email.getText().toString();
        pass = student_pass.getText().toString();

        if(email.isEmpty()) {
            setWarning();
        } else if (pass.isEmpty()) {
            setWarning();
        } else {
            validateInput();
        }
    }

    private void validateInput() {
        Call<List<Student_users_data>> call = db.get_Student_Users();
        call.enqueue(new Callback<List<Student_users_data>>() {
            @Override
            public void onResponse(Call<List<Student_users_data>> call, Response<List<Student_users_data>> response) {
                Toast.makeText(student_login.this, "Login in Please Wait..!", Toast.LENGTH_SHORT).show();
                List<Student_users_data> users = response.body();
                for (Student_users_data user : users) {
                    if (email.equals(user.getEmail()) && pass.equals(user.getPassword())) {
                        if(user.getStatus() == Boolean.TRUE) {
                            course = user.getCourse();
                            year = user.getYear();
                            openstudentMain(course, year);
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
            public void onFailure(Call<List<Student_users_data>> call, Throwable t) {
                Toast.makeText(student_login.this, "Unable to get data, please try again later..!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage("Fields cannot be empty...!");
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

    private void openstudentRegister() {
        startActivity(new Intent(this, Register_student.class));
        finish();
    }

    private void openstudentMain(String course, int year) {
        Intent intent = new Intent(student_login.this, student_main.class);
        intent.putExtra("course", course);
        intent.putExtra("year", year);
        startActivity(intent);
        finish();
    }
}