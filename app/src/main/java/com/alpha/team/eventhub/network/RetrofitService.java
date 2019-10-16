package com.alpha.team.eventhub.network;

import com.alpha.team.eventhub.entities.Category;
import com.alpha.team.eventhub.entities.Country;
import com.alpha.team.eventhub.entities.Event;
import com.alpha.team.eventhub.entities.User;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by clasence on 24,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 */
public interface RetrofitService {

    @GET("/countries")
    Call<List<Country>> getCountries();

    @GET("/events/getbylocation")
    Call<List<Country>> getByLocation(@Body HashMap<String, Object> body);

    @GET("/categories")
    Call<List<Category>> getCategories();


    @POST("/login")
    Call<User> getUser(@Body HashMap<String, Object> body);

    @POST("/eventuser")
    Call<HashMap<String,Object>> registerUser(@Body HashMap<String, Object> body);

    @POST("/login")
    Call<HashMap<String,Object>> loginUser(@Body HashMap<String, Object> body);

    @GET("/events")
    Call<List<Event>> getEvents();

    @GET("/events/bylike")
    Call<List<Event>> getEventsByLike();

    @POST("/events/getbylocation")
    Call<List<Event>> getEventsByLocation(@Body HashMap<String, Object> map);

    @POST("/events/register")
    Call<HashMap<String,Object>> registerForEvent(@Body HashMap<String, Object> body);

    @POST("/events/like")
    Call<HashMap<String,Object>> likeEvent(@Body HashMap<String, Object> body);

}
