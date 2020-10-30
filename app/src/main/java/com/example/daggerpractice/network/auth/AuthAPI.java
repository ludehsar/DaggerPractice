package com.example.daggerpractice.network.auth;

import com.example.daggerpractice.models.User;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AuthAPI {

    @GET("/users/{id}")
    Flowable<User> getUser(
            @Path("id") int id
    );
}
