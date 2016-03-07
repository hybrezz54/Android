package org.technowolves.otpradar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BlueAllianceService {

    @GET("/api/v2/events/{year}")
    Call<List<Event>> listEvents(@Path("year") String year, @Query("X-TBA-App-Id") String id);

    @GET("api/v2/event/{event}/teams")
    Call<List<Team>> listTeams(@Path("event") String event, @Query("X-TBA-App-Id") String id);

}
