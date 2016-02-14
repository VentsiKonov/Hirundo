import mongoEntities.Message;
import mongoEntities.User;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.springframework.stereotype.Service;
import spark.ModelAndView;
import spark.Request;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;


/**
 * Created by vkonov on 2/8/16.
 */

@Service
public class HirundoService {

    private HirundoDatastore datastore = new HirundoDatastore();

    HirundoService(){
        port(8080);
        staticFileLocation("/public");
        setupRoutes();
    }

    private Map<String, Object> prepareMap(Request req){
        Map<String, Object> map = new HashMap<>();
        //map.put("title", pageTitle);
        User user = req.session().attribute("user");
        map.put("user", user);

        return map;
    }

    private void follow(User user, User targetUser) throws Exception {
        if(user == null || targetUser == null){
            throw new Exception("Cannot follow missing user!");
        }
        if(user.getId().equals(targetUser.getId()))
            throw new Exception("Cannot follow yourself!");

        user.addFollowee(targetUser);
        datastore.updateUser(user);
    }

    private boolean unfollow(User user, User targetUser) {
        boolean res = user.removeFollowee(targetUser);
        if(res) {
            try {
                datastore.updateUser(user);
            } catch (Exception e) {
                res = false;
            }
        }
        return res;
    }

    private boolean isFollowing(User user, User targetUser) {
        if(user == null || targetUser == null)
            return false;

        return datastore.isUserFollowingTarget(user, targetUser);
    }

    private void setupRoutes() {

        get("/", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            User user = req.session().attribute("user");
            map.put("user", user);
            if(user != null){
                map.put("messages", datastore.getMessages(0, 10, user));
            }
            return new ModelAndView(map, "home.ftl");
        }, new FreeMarkerEngine());

        get("/following", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            User user = req.session().attribute("user");
            if(user == null)
                return new ModelAndView(map, "home.ftl");
            map.put("messages", datastore.getTimelineMessages(user));
            return new ModelAndView(map, "following.ftl");
        }, new FreeMarkerEngine());

        get("/login", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);

            return new ModelAndView(map, "login.ftl");
        }, new FreeMarkerEngine());

        get("/users", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            User user = (User) map.get("user");
            if(user != null)
                map.put("following", user.getFollowees());

            map.put("users", datastore.getUsersPartial(0, 100));
            return new ModelAndView(map, "users.ftl");
        }, new FreeMarkerEngine());

        get("/register", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            return new ModelAndView(map, "register.ftl");
        }, new FreeMarkerEngine());

        get("/logout", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            map.remove("user");
            req.session().attribute("user", null);
            return new ModelAndView(map, "home.ftl");
        }, new FreeMarkerEngine());

        get("/statistics/:username", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            User user = (User) map.get("user");
            User targetUser;
            String username = req.params(":username");
            if(username == null || username.isEmpty()){
                targetUser = user;
            }
            else{
                targetUser = datastore.getUser(username);
            }
            map.put("timeStats", datastore.getTimeStatsForUser(targetUser));
            map.put("placeStats", datastore.getPlaceStatsForUser(targetUser));
            map.put("topTen", datastore.getTopUsers(10));
            map.put("targetUser", targetUser);
            return new ModelAndView(map, "statistics.ftl");
        }, new FreeMarkerEngine());

        get("/statistics", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            User u = (User) map.get("user");
            if(u != null){
                map.put("timeStats", datastore.getTimeStatsForUser(u));
                map.put("placeStats", datastore.getPlaceStatsForUser(u));
                map.put("targetUser", u);
            }
            map.put("topTen", datastore.getTopUsers(10));
            return new ModelAndView(map, "statistics.ftl");
        }, new FreeMarkerEngine());

        get("/user/:username", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            String username = req.params(":username");
            User targetUser = datastore.getUser(username);
            map.put("messages", datastore.getMessages(0, 10, targetUser));
            map.put("targetUser", targetUser);
            map.put("following", isFollowing((User)map.get("user"), targetUser));
            return new ModelAndView(map, "profile.ftl");
        }, new FreeMarkerEngine());

        get("/follow/:username", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            User user = (User) map.get("user");
            String username = req.params(":username");
            if(username == null || username.isEmpty() || user == null){
                resp.redirect("/");
            }
            User targetUser = datastore.getUser(username);
            if(targetUser == null){
                map.put("error", "No such user!");
                map.put("messages", datastore.getMessages(0, 10, user));
                return new ModelAndView(map, "home.ftl");
            }
            try{
                follow(user, targetUser);
                resp.redirect("/user/" + targetUser.getUsername());
                return null;
            }catch (Exception e){
                map.put("error", e.getLocalizedMessage());
            }
            map.put("messages", datastore.getMessages(0, 10, user));
            return new ModelAndView(map, "home.ftl");
        }, new FreeMarkerEngine());

        get("/unfollow/:username", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            User user = (User) map.get("user");
            String username = req.params(":username");
            if(username == null || username.isEmpty() || user == null){
                resp.redirect("/");
            }
            User targetUser = datastore.getUser(username);
            if(targetUser == null){
                map.put("error", "No such user!");
                map.put("messages", datastore.getMessages(0, 10, user));
                return new ModelAndView(map, "home.ftl");
            }
            boolean success = unfollow(user, targetUser);
            if(!success) {
                map.put("error", "Could not unfollow user " + targetUser.getUsername());
                map.put("messages", datastore.getMessages(0, 10, user));
                return new ModelAndView(map, "home.ftl");
            }
            resp.redirect("/user/" + targetUser.getUsername());
            return null;
        }, new FreeMarkerEngine());

        post("/statistics", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            try {
                MultiMap<String> params = new MultiMap<String>();
                UrlEncoded.decodeTo(req.body(), params, "UTF-8");
                String word = params.getString("word");
                if(word == null || word.isEmpty())
                    resp.redirect("/");

                map.put("word", word);
                map.put("topTen", datastore.getTopUsers(10, word));
                return new ModelAndView(map, "statistics.ftl");
            } catch (Exception e) {
                map.put("error", "Internal error!");
                return new ModelAndView(map, "statistics.ftl");
            }

        }, new FreeMarkerEngine());

        post("/register", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            try {
                MultiMap<String> params = new MultiMap<String>();
                UrlEncoded.decodeTo(req.body(), params, "UTF-8");
                User user = new User();
                user.setEmail(params.getString("email"));
                user.setUsername(params.getString("username"));
                user.setPassword(params.getString("password"));
                user.setRegistrationDate(new Date());
                map.put("email", user.getEmail());
                map.put("username", user.getUsername());
                datastore.addUser(user);
                map.put("success", "User " + user.getUsername() + " registered!");
                return new ModelAndView(map, "home.ftl");
            } catch (Exception e) {
                map.put("error", e.getLocalizedMessage());
                return new ModelAndView(map, "register.ftl");
            }
        }, new FreeMarkerEngine());

        post("/login", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            User user = new User();
            try {
                MultiMap<String> params = new MultiMap<>();
                UrlEncoded.decodeTo(req.body(), params, "UTF-8");
                user.setUsername(params.getString("username"));
                user.setPassword(params.getString("password"));
            } catch (Exception e) {
                halt(501);
                return null;
            }
            map.put("username", user.getUsername());
            user = datastore.authUser(user);
            if(user == null){
                map.put("error", "Unknown username/password combination!");
                return new ModelAndView(map, "login.ftl");
            }
            map.put("user", user);
            req.session().attribute("user", user);
            resp.redirect("/");
            return null;
        }, new FreeMarkerEngine());

        post("/message", (req, resp) -> {
            Map<String, Object> map = prepareMap(req);
            User user = req.session().attribute("user");
            if(user != null){
                try{
                    MultiMap<String> params = new MultiMap<>();
                    UrlEncoded.decodeTo(req.body(), params, "UTF-8");
                    String content = params.getString("content");
                    String location = params.getString("location");
                    map.put("msgContent", content);
                    map.put("msgLocation", location);
                    Message message = new Message();
                    message.setContent(content);
                    message.setPlace(location);
                    message.setAuthorName(user.getUsername());
                    message.setDatePublished(new Date());
                    message.setAuthor(user);
                    datastore.addMessage(message);

                    //map.put("success", "Message sent!");
                }catch (Exception e){
                    map.put("error", e.getLocalizedMessage());
                    return new ModelAndView(map, "home.ftl");
                }
            }
            map.remove("msgContent");
            map.remove("msgLocation");
            resp.redirect("/");
            return null; //new ModelAndView(map, "home.ftl");
        }, new FreeMarkerEngine());
    }
}
