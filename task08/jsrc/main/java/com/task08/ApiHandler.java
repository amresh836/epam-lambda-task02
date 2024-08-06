package com.task08;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaLayer;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;

import java.io.IOException;
import java.util.Map;

@LambdaHandler(lambdaName = "api_handler",
        roleName = "api_handler-role",
        layers = "sdk-layer",
        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaLayer(
        layerName = "sdk-layer"
)
@LambdaUrlConfig(
        authType = AuthType.NONE,
        invokeMode = InvokeMode.BUFFERED
)
public class ApiHandler implements RequestHandler<Map<String,Double>, JsonObject> {

    public JsonObject handleRequest(Map<String,Double> request, Context context) {

        double latitude=request.get("latitude");
        double longitude= request.get("longitude");
        OpenMeteo client = new OpenMeteo(latitude, longitude);

        JsonObject weatherData=null;
        try {
            weatherData = client.getWeatherForecast();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return weatherData;
    }
}
