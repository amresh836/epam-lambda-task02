package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;

import java.util.HashMap;
import java.util.Map;

@LambdaHandler(lambdaName = "hello_world",
        roleName = "hello_world-role",
        isPublishVersion = true,
        aliasName = "${lambdas_alias_name}",
        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig(
		authType = AuthType.NONE,
		invokeMode = InvokeMode.BUFFERED
)
public class HelloWorld implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

	@Override
	public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent apiGatewayV2HTTPEvent, Context context) {


		APIGatewayV2HTTPResponse apiGatewayV2HTTPResponse;
		String getRawPath = apiGatewayV2HTTPEvent.getRawPath();
		String httpMethod = (apiGatewayV2HTTPEvent.getRequestContext() != null && apiGatewayV2HTTPEvent.getRequestContext().getHttp().getMethod() != null) ? apiGatewayV2HTTPEvent.getRequestContext().getHttp().getMethod() : null;

		if (getRawPath.equals("/hello") && httpMethod.equalsIgnoreCase("GET")) {
			apiGatewayV2HTTPResponse = APIGatewayV2HTTPResponse.builder().withBody("{\"statusCode\": 200, \"message\": \"Hello from Lambda\"}")
					.withStatusCode(200).build();
		} else {
			apiGatewayV2HTTPResponse = APIGatewayV2HTTPResponse
					.builder()
					.withStatusCode(400)
					.withBody("{\"statusCode\": 400, \"message\": \"Bad request syntax or unsupported method. Request path: " + (getRawPath != null ? getRawPath : "null") + ". HTTP method: " + (httpMethod != null ? httpMethod : "null") + "\"}")
					.build();
		}

		return apiGatewayV2HTTPResponse;
	}
}
