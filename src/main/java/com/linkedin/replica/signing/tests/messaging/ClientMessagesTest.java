package com.linkedin.replica.signing.tests.messaging;

import com.arangodb.ArangoDatabase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linkedin.replica.signing.config.Configuration;
import com.linkedin.replica.signing.database.DatabaseConnection;
import com.linkedin.replica.signing.messaging.ClientMessagesReceiver;
import com.linkedin.replica.signing.models.User;
import com.linkedin.replica.signing.tests.TestsUtils;
import com.linkedin.replica.signing.utils.JwtUtils;
import com.linkedin.replica.signing.utils.SHA512;
import com.rabbitmq.client.*;
import org.junit.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

public class ClientMessagesTest {
    private static Configuration config;
    private static String QUEUE_NAME;
    private static ClientMessagesReceiver messagesReceiver;
    private static ArangoDatabase arangoDb;
    private static java.sql.Connection mysqlDBInstance;

    private static ConnectionFactory factory;
    private static Connection connection;
    private static Channel channel;

    @BeforeClass
    public static void init() throws IOException, TimeoutException, SQLException {
        String rootFolder = "src/main/resources/config/";
        Configuration.init(rootFolder + "app.config",
                rootFolder + "arango.config",
                rootFolder + "mysql.config",
                rootFolder + "command.config",
                rootFolder + "controller.config");
        DatabaseConnection.init();
        config = Configuration.getInstance();

        // init message receiver
        QUEUE_NAME = config.getAppConfigProp("rabbitmq.queue.client");
        messagesReceiver = new ClientMessagesReceiver();

        // init db
        mysqlDBInstance = DatabaseConnection.getInstance().getMysqlDriver();

        arangoDb = DatabaseConnection.getInstance().getArangoDriver().db(
                Configuration.getInstance().getArangoConfigProp("db.name")
        );

        factory = new ConnectionFactory();
        factory.setHost(config.getAppConfigProp("rabbitmq.ip"));
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    @Test
    public void testSuccessfulSignUp() throws IOException, InterruptedException {
        JsonObject object = new JsonObject();
        object.addProperty("commandName", "signing.signUp");
        object.addProperty("email", "ahmed@gmail.com");
        object.addProperty("password", "1234");
        object.addProperty("firstName", "ahmed");
        object.addProperty("lastName", "ouda");
        byte[] message = object.toString().getBytes();
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", QUEUE_NAME, props, message);

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body));
                }
            }
        });

        String resMessage = response.take();
        JsonObject resObject = new JsonParser().parse(resMessage).getAsJsonObject();

        assertEquals("Expecting 200 status code", 200, resObject.get("statusCode").getAsInt());
        assertEquals("Expecting result to be true", true, resObject.get("results").getAsBoolean());
    }
    @Test
    public void testSuccessfulSignIn() throws IOException, InterruptedException {
        User user = new User();
        user.setEmail("ahmed@gmail.com");
        user.setPassword(SHA512.hash("1234"));
        TestsUtils.createUserSQL(user, mysqlDBInstance);
        JsonObject object = new JsonObject();
        object.addProperty("commandName", "signing.signIn");
        object.addProperty("email", "ahmed@gmail.com");
        object.addProperty("password", "1234");
        byte[] message = object.toString().getBytes();
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", QUEUE_NAME, props, message);

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body));
                }
            }
        });

        String resMessage = response.take();
        JsonObject resObject = new JsonParser().parse(resMessage).getAsJsonObject();

        assertEquals("Expecting 200 status code for successfull signin", 200, resObject.get("statusCode").getAsInt());
        assertEquals("Expecting a valid token", true, JwtUtils.validateToken(resObject.get("results").getAsString()));
    }
    @Test
    public void testBadRequestSignIn() throws IOException, InterruptedException {
        User user = new User();
        user.setEmail("ahmed@gmail.com");
        user.setPassword(SHA512.hash("1234"));
        TestsUtils.createUserSQL(user, mysqlDBInstance);
        JsonObject object = new JsonObject();
        object.addProperty("commandName", "signing.signIn");
        object.addProperty("email", "ahmed@gmail.com");
        byte[] message = object.toString().getBytes();
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", QUEUE_NAME, props, message);

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body));
                }
            }
        });

        String resMessage = response.take();
        JsonObject resObject = new JsonParser().parse(resMessage).getAsJsonObject();

        assertEquals("Expecting 400 status code for successfull signin", 400, resObject.get("statusCode").getAsInt());
    }

    @Test
    public void testBadRequestSignUp() throws IOException, InterruptedException {
        JsonObject object = new JsonObject();
        object.addProperty("commandName", "signing.signUp");
        object.addProperty("password", "1234");
        object.addProperty("firstName", "ahmed");
        object.addProperty("lastName", "ouda");
        byte[] message = object.toString().getBytes();
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", QUEUE_NAME, props, message);

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body));
                }
            }
        });

        String resMessage = response.take();
        JsonObject resObject = new JsonParser().parse(resMessage).getAsJsonObject();

        assertEquals("Expecting 400 status code", 400, resObject.get("statusCode").getAsInt());
    }


    @Test
    public void testSigningExceptionSignIn() throws IOException, InterruptedException {
        JsonObject object = new JsonObject();
        object.addProperty("commandName", "signing.signIn");
        object.addProperty("email", "ahmed@gmail.com");
        object.addProperty("password", "1234");
        byte[] message = object.toString().getBytes();
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", QUEUE_NAME, props, message);

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body));
                }
            }
        });

        String resMessage = response.take();
        JsonObject resObject = new JsonParser().parse(resMessage).getAsJsonObject();

        assertEquals("Expecting 401 status code", 401, resObject.get("statusCode").getAsInt());
        assertEquals("Expecting \"Incorrect username/password\" error message", "Incorrect username/password", resObject.get("error").getAsString());

    }

    @Test
    public void testSigningExceptionSignUp() throws IOException, InterruptedException {
        String email = "ahmed@gmail.com";
        String password = "1234";
        User user = new User();
        user.setEmail(email);
        user.setPassword(SHA512.hash(password));
        TestsUtils.createUserSQL(user, mysqlDBInstance);

        JsonObject object = new JsonObject();
        object.addProperty("commandName", "signing.signUp");
        object.addProperty("email", email);
        object.addProperty("password", "1234");
        object.addProperty("firstName", "ahmed");
        object.addProperty("lastName", "ouda");
        byte[] message = object.toString().getBytes();
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", QUEUE_NAME, props, message);

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body));
                }
            }
        });

        JsonObject resObject = new JsonParser().parse(response.take()).getAsJsonObject();

        assertEquals("Expecting 401 status code", 401, resObject.get("statusCode").getAsInt());
        assertEquals("Expecting \"This user already exists, Do you want to sign in?\" error message", "This user already exists, Do you want to sign in?", resObject.get("error").getAsString());
    }


    @Before
    public void initBeforeTest() throws IOException {
        arangoDb.createCollection(
                config.getArangoConfigProp("collection.name")
        );
    }

    @After
    public void cleanAfterTest() throws IOException {
        arangoDb.collection(
                config.getArangoConfigProp("collection.name")
        ).drop();


        TestsUtils.deleteUsersSQL(mysqlDBInstance);
    }

    @AfterClass
    public static void clean() throws IOException, TimeoutException, SQLException {
        // close message queue connection
        messagesReceiver.closeConnection();
        channel.close();
        connection.close();

        TestsUtils.deleteUsersSQL(mysqlDBInstance);

        DatabaseConnection.getInstance().closeConnections();
    }
}
