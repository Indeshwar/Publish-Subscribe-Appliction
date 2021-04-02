package org.example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Client_B {
    public static ArrayList<Integer> list = new ArrayList<>(); //create an arrayList that holds the integer data
    public static HashMap<Integer, String> map = new HashMap<>();//create an object of HashMap that holds received command

    public static void main(String[] args) throws InterruptedException{
        String broker = "tcp://broker.hivemq.com:1883"; //address of broker
        String clientId = "ChaudharyClient_B"; //Client Id

        try{
            //create an mqttClient
            final MqttClient client_B = new MqttClient(broker, clientId, new MemoryPersistence());

            //create an object of MqttConnectOptions
            //Holds the set of options that control how the client connects to a server.
            MqttConnectOptions conOpts = new MqttConnectOptions();

            //set non persistent session
            //the broker won't save any content for future subscriber
            conOpts.setCleanSession(true);

            //Initialize the TCP connection with broker
            client_B.connect();
            System.out.println("Connected to Broker" + broker);

            //set callback method to execute after a message is returned from the publisher
            client_B.setCallback(new MqttCallback(){
                ClientDau cleintDau = new ClientDau(); //create an object of clientDau
                int key = 1; //key of hash map
                public void messageArrived(String topic, MqttMessage message) throws MqttException {
                    System.out.println("\nReceived a Message!" +
                            "\n\tTopic:   " + topic +
                            "\n\tMessage: " + new String(message.getPayload()) +
                            "\n\tQoS:     " + message.getQos() + "\n");
                    String msg = new String(message.getPayload());

                    map.put(key, new String(message.getPayload())); //put the instruction in hash map
                    key++;//increment the key
                    String content = "";
                    if(msg.equals("Get_Summation")){
                        int total = cleintDau.summationValues(list); //return the sum of all the element of list
                        System.out.println("Total" + total);
                        content = "Summation is " + total;
                    }else if(msg.equals("Review_Old_Commands")){
                        content = cleintDau.reviewCommand(map);

                    } else {
                        String[] strArr = msg.split(":"); //split the message into two parts instruction and data
                        String[] data = strArr[1].split(",");
                        if (strArr[0].equals("Add") || strArr[0].equals("add")) {
                            cleintDau.add(list, data); //insert data into list"
                            content = "Added Successfully";
                        } else if (strArr[0].equals("Remove") || strArr[0].equals("remove")) {
                            cleintDau.remove(list, data); //remove data from list"
                            content = "Removed Successfully";
                        }
                    }

                    MqttMessage m = new MqttMessage(content.getBytes(StandardCharsets.UTF_8)); //build the message
                    m.setQos(1); // set quality of service 1
                    client_B.publish("/DS341/ResultsFrom/Chaudhary/Client_B", m); //publish the message

                }

                public void connectionLost(Throwable cause) {
                    // no need to implement this function for this assignment
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    // no need to implement this function for this assignment
                }
            });

            client_B.subscribe("/DS341/TaskTo/Chaudhary/ClientB", 1); //subcribe the topic
            System.out.println("Subscribed");
            System.out.println("Listening");


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
