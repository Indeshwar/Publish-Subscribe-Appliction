package org.example;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ClientDau {
    public void add(ArrayList<Integer> arrayList, String[] strArr){
        for(int i = 0; i < strArr.length; i++){
            //parsing the string into integer
            int num = Integer.parseInt(strArr[i]);
            //add the num in the arrayList
            arrayList.add(num);
        }

    }

    public void remove(ArrayList<Integer> arrayList, String[] strArr){
        for(int i = 0; i < strArr.length; i++){
            //parsing the string into integer
            int num = Integer.parseInt(strArr[i]);
            //remove the num from the arrayList
            arrayList.remove(new Integer(num));
        }

    }

    public int summationValues(ArrayList<Integer> arrayList){
        int sum = 0;
        for(int i = 0; i < arrayList.size(); i++){
            sum += arrayList.get(i);
        }
        return sum;
    }

    public String reviewCommand(HashMap<Integer, String> map){
        String s = "[";
        for(int key = 1; key <= map.size(); key++){
            s += map.get(key) + ", ";
        }
        s += "]";
        return s;
    }


}
