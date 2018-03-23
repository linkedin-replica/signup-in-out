package com.linkedin.replica.signing.messaging;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.linkedin.replica.signing.config.Configuration;
import com.linkedin.replica.signing.exceptions.BadRequestException;
import com.linkedin.replica.signing.exceptions.SigningException;
import com.linkedin.replica.signing.services.SigningService;
import com.linkedin.replica.signing.services.Workers;
import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeoutException;

public class ClientMessagesReceiver {

    private static final Logger LOGGER = LogManager.getLogger(ClientMessagesReceiver.class.getName());
    private Configuration configuration = Configuration.getInstance();
    private SigningService signingService = new SigningService();
    private final String QUEUE_NAME = configuration.getAppConfigProp("rabbitmq.queue.client");
    private final String RABBIT_MQ_IP = configuration.getAppConfigProp("rabbitmq.ip");

    private ConnectionFactory factory;
    private Channel channel;
    private Connection connection;
    private Gson gson = new Gson();

    /**
     * Bind a Consumer to signing queue that directs requests
     * to corresponding commands in the signing service
     * @throws IOException
     * @throws TimeoutException
     */

    public ClientMessagesReceiver() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost(RABBIT_MQ_IP);
        connection = factory.newConnection();
        channel = connection.createChannel();
        // declare the queue if it does not exist
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        LOGGER.info("Started signing receiver successfully.");

        // set unacknowledged limit to 1 message
        channel.basicQos(1);

        // Create the consumer (listener) for the new messages
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {

                Runnable messageProcessorRunnable = () -> {

                    // Create the response message properties
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();

                    // Extract the request arguments
                    JsonObject object = new JsonParser().parse(new String(body)).getAsJsonObject();

                    String commandName = object.get("commandName").getAsString();
                    HashMap<String, String> args = new HashMap<String, String>();

                    for (String key : object.keySet())
                        if (!key.equals("commandName"))
                            args.put(key, object.get(key).getAsString());

                    // Call the service and form the response
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();

                    try {
                        Object results = signingService.serve(commandName, args);
                        response.put("results", results);
                        response.put("statusCode", 200);
                    } catch (BadRequestException e) {
                        // set status code to 400
                        response.put("statusCode", 400);
                        response.put("error", e.getMessage());
                    } catch (SigningException e) {
                        // set status code to 401
                        response.put("statusCode", 401);
                        response.put("error", e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.put("statusCode", 500);
                        response.put("error", "Internal server error.");
                        LOGGER.error("Authentication service failed");
                    }

                    // publish the response to the "replyTo" queue
                    byte[] jsonResponse = gson.toJson(response).getBytes();
                    try {
                        channel.basicPublish("", properties.getReplyTo(), replyProps, jsonResponse);
                    } catch (IOException e) {
                        LOGGER.error("json response in failed");
                    }
                };
                Workers.getInstance().submit(messageProcessorRunnable);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

    // Method below not needed

    public void closeConnection() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
