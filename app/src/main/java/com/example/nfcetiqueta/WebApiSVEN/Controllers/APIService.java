package com.example.nfcetiqueta.WebApiSVEN.Controllers;

import com.example.nfcetiqueta.WebApiSVEN.Models.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {

    /**
     * @User
     * @Conseguir el identificador "userID" - "05070608"
     */

    @GET("api/users/listado/{id}")
    Call<List<Users>> findUsers(@Path("id") String id);

}
