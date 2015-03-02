package com.github.mongomap;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentMongoMap<K, V extends Serializable> extends ConcurrentHashMap<K, V> {

    private DBCollection collection;

    public void setMongoCollection(DBCollection collection) {
        this.collection = collection;
    }

    public void load() throws IOException, ClassNotFoundException {
        DBCursor cursor = collection.find();
        try {
            while(cursor.hasNext()) {
                DBObject object = cursor.next();
                try {
                    byte[] v = (byte[]) object.get("v");
                    K k = (K) object.get("_id");
                    put(k, (V) deserialize(v));
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            cursor.close();
        }
    }

    public V store(K key, V value) throws IOException {
        if (collection == null) {
            throw new IllegalStateException("Mongodb collection is not set");
        }

        collection.update(new BasicDBObject("_id", key), new BasicDBObject("v", serialize(value)).append("_id", key), true, false);

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

    private byte[] serialize(Serializable obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
