package com.edunexa;

import com.edunexa.data.Princi_Users_data;
import com.edunexa.data.Users;
import com.edunexa.data.msg_data;
import com.edunexa.data.Student_users_data;
import com.edunexa.data.teacher_user_data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MyApi {
    @GET("/users")
    Call<List<Users>> getUsers();

    @GET("/princi/users")
    Call<List<Princi_Users_data>> getPrinci_Users();

    @PUT("/princi_users/update/{id}")
    Call<Princi_Users_data> updatePrinci_Users(@Path("id") long id, @Body Princi_Users_data data);

    @POST("/princi/add_users/")
    Call<String> addPrinciusers(@Body Princi_Users_data user);

    @DELETE("/princi/del_users/{id}")
    Call<Princi_Users_data> deletePrinci_Users(@Path("id") long id);

    @GET("/student/users")
    Call<List<Student_users_data>> get_Student_Users();

    @POST("/student/add_users")
    Call<String> add_student_users(@Body Student_users_data user);

    @PUT("/student/del_users/update/{id}")
    Call<Student_users_data> update_Student_Users(@Path("id") long id, @Body Student_users_data data);

    @DELETE("/student_users/del/{id}")
    Call<Student_users_data> delete_Student_Users(@Path("id") long id);

    @GET("/teaching_users")
    Call<List<teacher_user_data>> get_teacher_Users();

    @PUT("/teacher_users/update/{id}")
    Call<teacher_user_data> update_teacher_Users(@Path("id") long id, @Body teacher_user_data data);

    @DELETE("/teacher_users/del/{id}")
    Call<teacher_user_data> delete_teacher_Users(@Path("id") long id);

    @POST("/teacher_users/add")
    Call<String> addTeacherusers(@Body teacher_user_data user);

    @POST("/users/add")
    Call<String> addusers(@Body Users user);

    @GET("/rur/messages")
    Call<List<msg_data>> getMessage();

    @POST("/rur/add_message")
    Call<String> addMessage(@Body msg_data msg);

    @DELETE("/rur/del_message/{id}")
    Call<String> deleteMessage(@Path("id") int id);

    @GET("/princi/messages")
    Call<List<msg_data>> get_princi_Message();

    @POST("/princi/add_message")
    Call<String> add_princi_Message(@Body msg_data msg);

    @DELETE("/princi/del_message/{id}")
    Call<String> delete_princi_Message(@Path("id") int id);

    @GET("/teacher/messages")
    Call<List<msg_data>> get_teacher_Message();

    @POST("/teacher/add_message")
    Call<String> add_teacher_Message(@Body msg_data msg);

    @DELETE("/teacher/del_message/{id}")
    Call<String> delete_teacher_Message(@Path("id") int id);

}
