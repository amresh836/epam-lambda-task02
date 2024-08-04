package com.task07;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
import com.syndicate.deployment.annotations.events.RuleEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@LambdaHandler(lambdaName = "uuid_generator",
	roleName = "uuid_generator-role",
	isPublishVersion = false,
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@EnvironmentVariables(value = {
		@EnvironmentVariable(key = "target_bucket", value = "${target_bucket}")
})
@RuleEventSource(
		targetRule = "uuid_trigger"
)
public class UuidGenerator implements RequestHandler<Object, String> {

	private final AmazonS3 s3Client=AmazonS3ClientBuilder.defaultClient();
	private final String S3_BUCKET_NAME = System.getenv("target_bucket");

	@Override
	public String handleRequest(Object input, Context context) {

		LambdaLogger logger = context.getLogger();
		LocalDateTime now = LocalDateTime.now();
		String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		StringWriter uuidWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(uuidWriter);
		for (int i = 0; i < 10; i++) {
			printWriter.println(UUID.randomUUID().toString());
		}
		printWriter.close();
		String uuids = uuidWriter.toString();
		String filePath = "UUIDs-" + formattedDate + ".txt";
		s3Client.putObject(new PutObjectRequest(S3_BUCKET_NAME, filePath, uuidWriter.toString()));
		logger.log("Successfully uploaded UUIDs to S3 bucket.");

		return "Success";
	}
}
