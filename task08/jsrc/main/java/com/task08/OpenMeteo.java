package com.task08;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OpenMeteo {
    private String BASE_URL = "https://api.open-meteo.com/v1/forecast";
    private final double latitude;
    private final double longitude;
    private final OkHttpClient client;
    private final Gson gson;

    public OpenMeteo(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public JsonObject getWeatherForecast() throws IOException {
        String url = String.format("%s?latitude=%f&longitude=%f&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m&current_weather=true", BASE_URL, latitude, longitude);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return gson.fromJson(response.body().string(), JsonObject.class);
        }
    }
}
