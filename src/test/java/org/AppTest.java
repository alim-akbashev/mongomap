package org;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.junit.Assert;
import org.junit.Test;
import org.mongomap.ConcurrentMongoMap;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    @Test
    public void generalMap() {
        ConcurrentMongoMap<String, String> generalMap = new ConcurrentMongoMap<String, String>();
        generalMap.put("foo", "bar");
        Assert.assertEquals("General map usage", generalMap.get("foo"), "bar");
    }

    @Test
    public void storeLoadClear() throws IOException, ClassNotFoundException {
        Fongo fongo = new Fongo("test mongodb server");
        DB db = fongo.getDB("test");
        DBCollection collection = db.getCollection("test-collection");

        ConcurrentMongoMap<String, String> persistentMap = new ConcurrentMongoMap<String, String>();
        persistentMap.setMongoCollection(collection);

        persistentMap.store("foo", "bar");


        ConcurrentMongoMap<String, String> persistentMapLoaded = new ConcurrentMongoMap<String, String>();
        persistentMapLoaded.setMongoCollection(collection);
        persistentMapLoaded.load();

        Assert.assertEquals("Loaded from mongodb collection", persistentMapLoaded.get("foo"), "bar");

        persistentMapLoaded.clear();

        ConcurrentMongoMap<String, String> persistentMapCleared = new ConcurrentMongoMap<String, String>();
        persistentMapCleared.setMongoCollection(collection);
        persistentMapCleared.load();

        Assert.assertNull("Cleared collection", persistentMapCleared.get("foo"));
    }

}
