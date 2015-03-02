# mongomap
Lightweigth wrapper around ConcurrentHashMap. Provides ability to persist HashMap state to mongodb

# Example

```
MongoClient client = new MongoClient("localhost");
DBCollection collection = ...

ConcurrentMongoMap<String, String> map = new ConcurrentMongoMap<String, String>();
map.setMongoCollection(collection);
map.load();

map.store("foo", "bar");

...
...
...

ConcurrentMongoMap<String, String> map = new ConcurrentMongoMap<String, String>();
map.setMongoCollection(collection);
map.load();

String barString = map.get("foo");
```
