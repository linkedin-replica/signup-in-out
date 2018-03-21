package messaging;

import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import services.UserAuthenticationService;
import utils.ConfigReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class MessagesReceiver {

    private static final Logger LOGGER = LogManager.getLogger(MessagesReceiver.class.getName());
    private Properties appConfig = ConfigReader.readAppConfig();
    private UserAuthenticationService userAuthenticationService = new UserAuthenticationService();
    private final String QUEUE_NAME = appConfig.getProperty("rabbitmq.queue");
    private final String RABBIT_MQ_IP = appConfig.getProperty("rabbitmq.ip");
    private static final String[] attributes = {"email", "password", "jwtToken", "firstName", "lastName"};

    private ConnectionFactory factory;
    private Channel channel;
    private Connection connection;

    /**
     * Bind a Consumer to AUTHENTICATION queue that directs requests
     * to corresponding commands in the authentication service
     * @throws IOException
     * @throws TimeoutException
     */

    public MessagesReceiver() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost(RABBIT_MQ_IP);
        connection = factory.newConnection();
        channel = connection.createChannel();

        // declare the queue if it does not exist
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        LOGGER.info("Started notification receiver successfully.");

        // Create the consumer (listener) for the new messages
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {

                // extract notification info from body and send it
                String message = new String(body, "UTF-8");
                JSONParser parser = new JSONParser();
                JSONObject object;
                LinkedHashMap<String, Object> response;
                try {
                    object = (JSONObject) parser.parse(message);
                    String commandName = object.get("commandName").toString();

                    HashMap<String, String> args = new HashMap<String, String>();
                    for(String attribute : attributes)
                        args.put(attribute, object.get(attribute).toString());

                    response = userAuthenticationService.serve(commandName, args);

                } catch (ParseException e) {
                    e.printStackTrace();
                    LOGGER.error("Corrupt json format");
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("Authentication service failed");
                }
            }
        };
    }

    // Method below not needed

    public void closeConnection() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
