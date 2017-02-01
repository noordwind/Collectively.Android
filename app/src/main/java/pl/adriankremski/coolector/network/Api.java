package pl.adriankremski.coolector.network;

import io.reactivex.Observable;
import pl.adriankremski.coolector.model.AuthRequest;
import pl.adriankremski.coolector.model.AuthResponse;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    @Headers({"Accept: application/json", "Content-type: application/json"})
    @POST("api/sign-in")
    Observable<AuthResponse> login(@Body AuthRequest authRequest);
}
