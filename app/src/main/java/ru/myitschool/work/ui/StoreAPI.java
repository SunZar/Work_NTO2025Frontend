package ru.myitschool.work.ui;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface StoreAPI {

    @GET("api/{login}/auth")
    Call<Boolean> authenticateUser(@Path("login") String login);

    @GET("api/{login}/info")
    Call<User> informationUser(@Path("login") String login);

    @PATCH("api/{login}/open")
    Call<Void> openDoor(@Path("login") String login, @Body Door request);
}
