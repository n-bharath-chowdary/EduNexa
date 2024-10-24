package com.edunexa.student;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.edunexa.data.teacher_user_data;
import com.edunexa.teacher.teacher_register;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register_student extends AppCompatActivity {
    private EditText student_name, student_email, student_password;
    private String name, email, password, course_selected, college_name;
    private int year_selected;
    private RelativeLayout rl;
    private TextView student_login;
    private Spinner course, year;
    private List<String> courses = new ArrayList<>();
    private List<Integer> years = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<Integer> adapter1;
    private MyApi db;
    private AutoCompleteTextView student_clg_select;
    private String[] clg_lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        rl = findViewById(R.id.student_register);
        student_name = findViewById(R.id.student_name);
        student_password = findViewById(R.id.student_password);
        student_email = findViewById(R.id.student_email);
        student_login = findViewById(R.id.student_login_link);
        course = findViewById(R.id.course);
        year = findViewById(R.id.year);
        student_clg_select = findViewById(R.id.student_college_select);

        db = RetrofitInstance.getInstance().create(MyApi.class);

        clg_lst = getResources().getStringArray(R.array.clg_lst);
        ArrayAdapter<String> college_adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, clg_lst);
        student_clg_select.setAdapter(college_adapter);
        student_clg_select.setThreshold(2);

        courses.add("BCA");
        courses.add("BCom");
        courses.add("BSc");
        courses.add("BA");
        courses.add("BBA");
        courses.add("BBM");
        courses.add("BSW");
        years.add(1);
        years.add(2);
        years.add(3);
        years.add(4);
        adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, courses);
        adapter1 = new ArrayAdapter<>(this, R.layout.spinner_layout, years);
        course.setAdapter(adapter);
        year.setAdapter(adapter1);
        course.setSelection(0);
        year.setSelection(0);

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });

        student_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStudentLogin();
            }
        });

        course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course_selected = courses.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                course_selected = null;
            }
        });

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year_selected = years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                year_selected = 0;
            }
        });

    }

    private void openStudentLogin() {
        startActivity(new Intent(this, student_login.class));
        finish();
    }

    private void validateInput() {
        name = student_name.getText().toString();
        email = student_email.getText().toString();
        password = student_password.getText().toString();
        college_name = student_clg_select.toString();

        if(name.isEmpty()){
            setWarning();
        } else if (email.isEmpty()) {
            setWarning();
        } else if (password.isEmpty()) {
            setWarning();
        } else if(course_selected == null) {
            setWarning();
        } else if(year_selected == 0) {
            setWarning();
        } else {
            Call<List<Student_users_data>> all_users = db.get_Student_Users();
            all_users.enqueue(new Callback<List<Student_users_data>>() {
                @Override
                public void onResponse(Call<List<Student_users_data>> call, Response<List<Student_users_data>> response) {
                    List<Student_users_data> users = response.body();
                    for (Student_users_data user : users) {
                        if (email.equals(user.getEmail())) {
                            setWarning2();
                            break;
                        } else {
                            add_student_user();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Student_users_data>> call, Throwable t) {
                    Toast.makeText(Register_student.this, "Failed to create user, Try again Later", Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    private void add_student_user() {
        Student_users_data users = new Student_users_data();
        users.setEmail(email);
        users.setPassword(password);
        users.setStatus(Boolean.FALSE);
        users.setName(name);
        users.setCollege_name(college_name);
        users.setCourse(course_selected);
        users.setYear(year_selected);

        Call<String> call = db.add_student_users(users);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                setWarning1();
                openStudentLogin();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Register_student.this, "User cannot be created, please try again later..!", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void setWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage("Fields cannot be empty...!, Select Course & Year too");
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
}