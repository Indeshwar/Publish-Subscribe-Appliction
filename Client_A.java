package org.example;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Client_A {
    public static ArrayList<Integer> list = new ArrayList<>(); //create an arrayList that holds the integer data
    public static HashMap<Integer, String> map = new HashMap<>();//create an object of HashMap that holds received command

    public static void main(String[] args) throws InterruptedException{
        String broker = "tcp://broker.hivemq.com:1883"; //address of broker
        String clientId = "ChudharyClient_A"; //client ID

        try{
            //create an object of MqttClient that can communicate with server
            final MqttClient client_A = new MqttClient(broker, clientId, new MemoryPersistence());

            //create an object of MqttConnectOptions
            //Holds the set of options that control how the client connects to a server.
            MqttConnectOptions conOpts = new MqttConnectOptions();

            //establish non persistent session
            //the broker won't save any content for future subscriber
            conOpts.setCleanSession(true);

            //Establish a TCP connection with broker
            client_A.connect();

            System.out.println("Connected to Broker" + broker);

            //set callback method to execute after a message is returned from the publisher
            client_A.setCallback(new MqttCallback(){
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
                        content = "Summation is " + total;

                    }else if(msg.equals("Review_Old_Commands")){
                        content = cleintDau.reviewCommand(map);//retur the all the  commands stored in map

                    } else {
                        String[] strArr = msg.split(":"); //split the message into two parts instruction and data
                        String[] data = strArr[1].split(",");
                        if (strArr[0].equals("Add") || strArr[0].equals("add")) {
                            cleintDau.add(list, data); //remove data from arrayList and return message"
                            content = "Added Successfully";

                        } else if (strArr[0].equals("Remove") || strArr[0].equals("remove")) {
                            cleintDau.remove(list, data); //remove data from arrayList
                            content = "Removed Successfully";

                        }
                    }

                    MqttMessage m = new MqttMessage(content.getBytes(StandardCharsets.UTF_8)); //build the message
                    m.setQos(1); // set quality of service 1
                    client_A.publish("/DS341/ResultsFrom/Chaudhary/Client_A", m); //publish the

                }

                public void connectionLost(Throwable cause) {
                    // no need to implement this function for this assignment
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    // no need to implement this function for this assignment
                }
            });

            //subscribe the topic assignment3
            client_A.subscribe("/DS341/TaskTo/Chaudhary/ClientA", 1);

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
