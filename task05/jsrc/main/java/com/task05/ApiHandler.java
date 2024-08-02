package com.task05;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@LambdaHandler(lambdaName = "api_handler",
        roleName = "api_handler-role",
        isPublishVersion = false,
        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class ApiHandler implements RequestHandler<RequestData, ResponseData> {

    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private DynamoDB dynamoDb = new DynamoDB(client);
    private String DYNAMODB_TABLE_NAME = "cmtr-75ee1823-Events";

    @Override
    public ResponseData handleRequest(RequestData event, Context context) {
        int principalId=event.getPrincipalId();
        Map<String,String> content=event.getContent();
        String newId = UUID.randomUUID().toString();
        String currentTime = Instant.now().toString();
        Table table = dynamoDb.getTable(DYNAMODB_TABLE_NAME);
        Item item=new Item()
                .withPrimaryKey("id",newId)
                .withInt("principalId",principalId)
                .withString("createdAt",currentTime)
                .withMap("body",content);
        table.putItem(item);

        EventData eventData=EventData.builder()
                .id(newId)
                .principalId(principalId)
                .createdAt(currentTime)
                .body(content)
                .build();

        ResponseData responseData=ResponseData.builder()
                .statusCode(201)
                .eventData(eventData)
                .build();
        return  responseData;

    }
}