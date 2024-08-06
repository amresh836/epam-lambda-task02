package com.task08;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonObject;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaLayer;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.ArtifactExtension;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;
import org.epam.openapi.App;

import java.io.IOException;


@LambdaHandler(lambdaName = "api_handler",
        roleName = "api_handler-role",
        layers = "sdk-layer",
        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaLayer(
        layerName = "sdk-layer",
        libraries = {"lib/open-meteo-api-1.0-SNAPSHOT.jar"},
        artifactExtension = ArtifactExtension.JAR
)
@LambdaUrlConfig(
        authType = AuthType.NONE,
        invokeMode = InvokeMode.BUFFERED
)
public class ApiHandler implements RequestHandler<Object, JsonObject> {

    public JsonObject handleRequest(Object request, Context context) {

        App client = new App(50.4375, 30.5);
        JsonObject weatherData = null;
        try {
            weatherData = client.getWeatherForecast();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return weatherData;
    }
}
