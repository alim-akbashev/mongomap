package org.mongomap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMongoMap<K, V> extends ConcurrentHashMap<K, V> {

    private DBCollection collection;

    public void setMongoCollection(DBCollection collection) {
        this.collection = collection;
    }

    public void load() {
        DBCursor cursor = collection.find();
        try {
            while(cursor.hasNext()) {
                DBObject object = cursor.next();
                try {
                    V v = (V) object.get("v");
                    K k = (K) object.get("_id");
                    put(k, v);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            cursor.close();
        }
    }

    public V store(K key, V value) {
        if (collection == null) {
            throw new IllegalStateException("Mongodb collection is not set");
        }

        collection.update(new BasicDBObject("_id", key), new BasicDBObject("v", value).append("_id", key), true, false);

        return put(key, value);
    }

    @Override
    public V remove(Object key) {
        collection.remove(new BasicDBObject("_id", key));
        return super.remove(key);
    }

    @Override
    public boolean remove(Object key, Object value) {
        if (super.remove(key, value)) {
            collection.remove(new BasicDBObject("_id", key));
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        collection.drop();
        super.clear();
    }
}
