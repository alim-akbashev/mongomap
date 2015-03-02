package com.github.mongomap;

import com.mongodb.MongoClient;

import java.io.IOException;
import java.util.Map;

public class ExampleApp
{
    public static void main( String[] args ) throws IOException, ClassNotFoundException {
        MongoClient client = new MongoClient("localhost");

        ConcurrentMongoMap<Integer, Blah> map = new ConcurrentMongoMap<Integer, Blah>();
        map.setMongoCollection(client.getDB("chat").getCollection("test"));
        map.load();

        Blah t = new Blah();
        t.a = 1;
        t.b = 1.5f;

        map.store(123, t);
        map.remove(123);

        map.store(432, t);


        for (Map.Entry<Integer, Blah> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " := (" + entry.getValue().a + "," + entry.getValue().b + ")");
        }

        map.clear();

    }
}
