var seedUsers = function() {
    for (var i = 0; i < 20; i++) {
        var uname = "user" + i;
        db.users.save({
            className: "mongoEntities.User",
            username: uname,
            email: uname+"@hirundo.com",
            passwordHash: "ee11cbb19052e40b07aac0ca060c23ee",
            "registrationDate" : ISODate("2016-02-13T17:35:41.136Z"),
            "verified" : false
        });
    }
};

var seedMessages = function(){
    var loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
        " Integer nec odio." +
        " Praesent libero." +
        " Sed cursus ante dapibus diam." +
        " Sed nisi." +
        " Nulla quis sem at nibh elementum imperdiet." +
        " Duis sagittis ipsum." +
        " Praesent mauris." +
        " Fusce nec tellus sed augue semper porta." +
        " Mauris massa." +
        " Vestibulum lacinia arcu eget nulla." +
        " Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos." +
        " Curabitur sodales ligula in libero." +
        " Sed dignissim lacinia nunc." +
        " Curabitur tortor." +
        " Pellentesque nibh." +
        " Aenean quam." +
        " In scelerisque sem at dolor." +
        " Maecenas mattis." +
        " Sed convallis tristique sem." +
        " Proin ut ligula vel nunc egestas porttitor." +
        " Morbi lectus risus, iaculis vel, suscipit quis, luctus non, massa." +
        " Fusce ac turpis quis ligula lacinia aliquet." +
        " Mauris ipsum." +
        " Nulla metus metus, ullamcorper vel, tincidunt sed, euismod in, nibh." +
        " Quisque volutpat condimentum velit." +
        " Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos." +
        " Nam nec ante." +
        " Sed lacinia, urna non tincidunt mattis, tortor neque adipiscing diam, a cursus ipsum ante quis turpis." +
        " Nulla facilisi." +
        " Ut fringilla." +
        " Suspendisse potenti." +
        " Nunc feugiat mi a tellus consequat imperdiet." +
        " Vestibulum sapien." +
        " Proin quam." +
        " Etiam ultrices." +
        " Suspendisse in justo eu magna luctus suscipit." +
        " Sed lectus." +
        " Integer euismod lacus luctus magna." +
        " Quisque cursus, metus vitae pharetra auctor, sem massa mattis sem, at interdum magna augue eget diam." +
        " Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Morbi lacinia molestie dui." +
        " Praesent blandit dolor." +
        " Sed non quam." +
        " In vel mi sit amet augue congue elementum." +
        " Morbi in ipsum sit amet pede facilisis laoreet." +
        " Donec lacus nunc, viverra nec, blandit vel, egestas et, augue." +
        " Vestibulum tincidunt malesuada tellus." +
        " Ut ultrices ultrices enim." +
        " Curabitur sit amet mauris." +
        " Morbi in dui quis est pulvinar ullamcorper." +
        " Nulla facilisi." +
        " Integer lacinia sollicitudin massa." +
        " Cras metus." +
        " Sed aliquet risus a tortor." +
        " Integer id quam." +
        " Morbi mi." +
        " Quisque nisl felis, venenatis tristique, dignissim in, ultrices sit amet, augue." +
        " Proin sodales libero eget ante." +
        " Nulla quam." +
        " Aenean laoreet." +
        " Vestibulum nisi lectus, commodo ac, facilisis ac, ultricies eu, pede." +
        " Ut orci risus, accumsan porttitor, cursus quis, aliquet eget, justo." +
        " Sed pretium blandit orci." +
        " Ut eu diam at pede suscipit sodales." +
        " Aenean lectus elit, fermentum non, convallis id, sagittis at, neque." +
        " Nullam mauris orci, aliquet et, iaculis et, viverra vitae, ligula." +
        " Nulla ut felis in purus aliquam imperdiet." +
        " Maecenas aliquet mollis lectus." +
        " Vivamus consectetuer risus et tortor." +
        " Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
        " Integer nec odio." +
        " Praesent libero." +
        " Sed cursus ante dapibus diam." +
        " Sed nisi." +
        " Nulla quis sem at nibh elementum imperdiet." +
        " Duis sagittis ipsum." +
        " Praesent mauris." +
        " Fusce nec tellus sed augue semper porta." +
        " Mauris massa." +
        " Vestibulum lacinia arcu eget nulla." +
        " Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos." +
        " Curabitur sodales ligula in libero." +
        " Sed dignissim lacinia nunc." +
        " Curabitur tortor." +
        " Pellentesque nibh." +
        " Aenean quam." +
        " In scelerisque sem at dolor." +
        " Maecenas mattis." +
        " Sed convallis tristique sem." +
        " Proin ut ligula vel nunc egestas porttitor." +
        " Morbi lectus risus, iaculis vel, suscipit quis, luctus non, massa." +
        " Fusce ac turpis quis ligula lacinia aliquet." +
        " Mauris ipsum." +
        " Nulla metus metus, ullamcorper vel, tincidunt sed, euismod in, nibh." +
        " Quisque volutpat condimentum velit." +
        " Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos." +
        " Nam nec ante." +
        " Sed lacinia, urna non tincidunt mattis, tortor neque adipiscing diam, a cursus ipsum ante quis turpis." +
        " Nulla facilisi." +
        " Ut fringill.";

    loremIpsum = loremIpsum.split(".");

    var locations = ["Sofia", "Plovdiv", "Varna", "Burgas", "Razgrad", "Asenovgrad"];

    for (var i = 0; i < 1000; i++) {

        var urand = Math.floor(((Math.random() * 100) % 20)),
            rt1 = Math.floor((Math.random() * 1000) % loremIpsum.length),
            rt2 = Math.floor((Math.random() * 1000) % loremIpsum.length);
        var start = new Date(2016, 0, 1);
        var end = new Date();
        var uname = "user" + urand;
        var uref = db.users.findOne({username: uname})._id;
        var location = (rt1%2 == 0 ? locations[i%6] : "");
        db.messages.save({
            "className" : "mongoEntities.Message",
            "authorName" : uname,
            "author" : DBRef("users", uref),
            "content" : loremIpsum[rt1] + loremIpsum[rt2],
            "place" : location,
            "datePublished" : new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()))
        });

    }
};