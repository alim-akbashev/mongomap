package org.mongomap;

import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.util.Map;

public class ExampleApp
{
    public static void main( String[] args ) throws UnknownHostException {
        MongoClient client = new MongoClient("localhost");

        ConcurrentMongoMap<Integer, String> map = new ConcurrentMongoMap<Integer, String>();
        map.setMongoCollection(client.getDB("chat").getCollection("test"));
        map.load();

        map.clear();
        map.store(123, "hello");

        //map.remove(123, "hello");

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " := " + entry.getValue());
        }

    }
}
