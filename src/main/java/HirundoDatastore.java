import com.mongodb.*;
import mongoEntities.Message;
import mongoEntities.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vkonov on 2/9/16.
 */
//class Stats{
//    private String authorName;
//    private int count;
//}

public class HirundoDatastore {
    private Datastore datastore;

    public HirundoDatastore(){
        MongoClient mongo = new MongoClient("localhost", 27017);
        Morphia morphia = new Morphia();
        morphia.mapPackage("mongoEntities");
        datastore = morphia.createDatastore(mongo, "hirundo");
        datastore.ensureIndexes();
    }

    public User authUser(User u){
        return datastore.find(User.class)
                 .field("username").equal(u.getUsername())
                 .field("passwordHash").equal(u.getPasswordHash()).get();
    }

    public void addUser(User u) throws Exception {
        if(userExists(u.getUsername())){
            throw new Exception("User already exists!");
        }

        datastore.save(u);

    }

    public void updateUser(User u) throws Exception {
        if(!userExists(u.getUsername())){
            throw new Exception("User does not exist!");
        }

        datastore.save(u);
    }

    public boolean userExists(String username){
        return datastore.find(User.class)
                        .field("username").equal(username).countAll() != 0;
    }

    public void addMessage(Message message){
        datastore.save(message);
    }

    public List<Message> getMessages(int start, int count){
        return getMessages(start, count, null);
    }

    public List<Message> getMessages(int start, int count, User user){
        Query<Message> query = datastore.find(Message.class);
        if(user != null){
            query = query.field("authorName").equal(user.getUsername());
        }
        query = query.order("datePublished").offset(start).limit(count);
        return query.asList();
    }

    public User getUser(String username){
        return datastore.find(User.class).field("username").equal(username).get();
    }

    public List<Message> getTimelineMessages(User user){
        // Last 50 messages of people who 'user' follows
        Query<Message> query = datastore.find(Message.class).filter("author in", user.getFollowees()).order("-datePublished").limit(50);
        //Map<String, Object> exp = query.explain();
        return query.asList();
    }

    public boolean isUserFollowingTarget(User user, User target){
        return datastore.find(User.class).field("username").equal(user.getUsername()).field("following").hasThisElement(target).countAll() != 0;
    }

    public List<User> getUsersPartial(int start, int count){
        List<User> users = datastore.find(User.class).offset(start).limit(count).retrievedFields(true, "username", "verified").asList();
        return users;
    }

    public List<Double> getTimeStatsForUser(User u){
        DBObject queryObject = new BasicDBObject("authorName", u.getUsername());
        String map =
                "function(){" +
                    "var hours = this.datePublished.getHours();" +
                    "var zone;" +
                    "if(hours >= 0 && hours < 4){" +
                        "zone = 0;" +
                    "}" +
                    "else if(hours >= 4 && hours < 8){" +
                        "zone = 1;" +
                    "}" +
                    "else if(hours >= 8 && hours < 12){" +
                        "zone = 2;" +
                    "}" +
                    "else if(hours >= 12 && hours < 16){" +
                        "zone = 3;" +
                    "}" +
                    "else if(hours >= 16 && hours < 20){" +
                        "zone = 4;" +
                    "}" +
                    "else {" +
                        "zone = 5;" +
                    "}" +
                    "emit(zone, 1);" +
                "}";

        String reduce =
                "function(k, v){" +
                    "return Array.sum(v);" +
                "}";

        DBCollection collection = datastore.getCollection(Message.class);
        MapReduceCommand mrc = new MapReduceCommand(
                collection,
                map, reduce, null,
                MapReduceCommand.OutputType.INLINE,
                queryObject);

        MapReduceOutput output = collection.mapReduce(mrc);
        Integer count = collection.find(queryObject).count();

        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            result.add(0.0);
        }
        result.add(count.doubleValue());
        output.results().forEach(
                (DBObject o) ->
                        result.set(((Double)o.get("_id")).intValue(), (Double) o.get("value")));


        return result;
    }

    public Map<String, Double> getPlaceStatsForUser(User u){
        DBObject queryObject = new BasicDBObject("authorName", u.getUsername());
        String map =
                "function(){" +
                    "var place = this.place;" +
                    "if(place != null){" +
                        "if(place === ''){" +
                            "place = 'Empty';" +
                        "}" +
                        "else if(place !== 'Sofia' && place !== 'Plovdiv' && place !== 'Varna' && place !== 'Burgas'){" +
                            "place = 'Other';" +
                        "}" +
                    "}" +
                    "else {" +
                        "place = 'Empty';" +
                    "}" +
                    "emit(place, 1);" +
                "}";

        String reduce =
                "function(k, v){ return Array.sum(v); }";
        DBCollection collection = datastore.getCollection(Message.class);
        MapReduceCommand mrc = new MapReduceCommand(
                collection,
                map, reduce, null,
                MapReduceCommand.OutputType.INLINE,
                queryObject);

        MapReduceOutput output = collection.mapReduce(mrc);
        Integer count = collection.find(queryObject).count();

        Map<String, Double> result = new HashMap<>();
        result.put("Total", count.doubleValue());
        result.put("Burgas", 0.0);
        result.put("Varna", 0.0);
        result.put("Sofia", 0.0);
        result.put("Plovdiv", 0.0);
        result.put("Other", 0.0);
        result.put("Empty", 0.0);
        output.results().forEach(
                (DBObject o) ->
                        result.replace((String) o.get("_id"), (Double) o.get("value")));

        return result;
    }

    public List<DBObject> getTopUsers(int count){
        return getTopUsers(count, null);
    }

    public List<DBObject> getTopUsers(int count, String word){
//        List<Stats> result = new ArrayList<>();
//        AggregationPipeline aggregation = datastore.createAggregation(Message.class)
//                .project(Projection.projection("authorName"))
//                .group("authorName", Group.grouping("count", new Accumulator("$sum", 1)))
//                .sort(Sort.ascending("authorName"))
//                .limit(count);
//        Iterator<Stats> iterator = aggregation.aggregate(Stats.class);
//        iterator.forEachRemaining(result::add);

        List<DBObject> result = new ArrayList<>();
        List<BasicDBObject> aggregationList = new ArrayList<>();
        if(word != null && !word.isEmpty()){
            aggregationList.add(new BasicDBObject("$match", new BasicDBObject("$text", new BasicDBObject("$search", word))));
        }
        aggregationList.add(new BasicDBObject("$project", new BasicDBObject("authorName", 1)));
        aggregationList.add(new BasicDBObject("$group",
                        new BasicDBObject("_id", "$authorName").append("count", new BasicDBObject("$sum", 1))
        ));
        aggregationList.add(new BasicDBObject("$sort", new BasicDBObject("count", -1)));
        aggregationList.add(new BasicDBObject("$limit", count));
        AggregationOutput aggregation = datastore.getCollection(Message.class).aggregate(aggregationList);
        aggregation.results().forEach(result::add);
        return result;
    }
}
