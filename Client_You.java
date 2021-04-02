package org.example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client_You {
    public static void main(String[] args) throws InterruptedException{
        String broker = "tcp://broker.hivemq.com:1883";
        String clientId = "ChaudharyCient_You";

        try{
            //create an object of mqttClient
            final MqttClient client_You = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions conOpts = new MqttConnectOptions();
            conOpts.setCleanSession(true);

            client_You.connect();
            System.out.print("connected");

            client_You.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("\nReceived a Message!" +
                            "\n\tTopic:   " + topic +
                            "\n\tMessage: " + new String(message.getPayload()) +
                            "\n\tQoS:     " + message.getQos() + "\n");
                }


                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

            client_You.subscribe("/DS341/ResultsFrom/Chaudhary/#", 1);
            System.out.println("Subscribed");
            System.out.println("Listening");

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the target Client");
            String targetClient = sc.nextLine();
            String statement = "";
            while (!statement.equals("exit")){
                System.out.println("Enter the command: ");
                statement = sc.nextLine();
                MqttMessage message = new MqttMessage(statement.getBytes(StandardCharsets.UTF_8)); //build the message
                message.setQos(1); //Set QOS 1

                client_You.publish("/DS341/TaskTo/Chaudhary/" + targetClient, message); //publish data to client_B
                System.out.println("I published message " + message);
            }

            client_You.disconnect(); // disconnect the client_You to broker
            System.out.println("Disconnected");
            System.exit(0);

        }catch(MqttException me){
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
}
