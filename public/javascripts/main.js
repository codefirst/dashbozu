(function(){
    // Enable pusher logging - don't include this in production
    Pusher.log = function(message) {
        if (window.console && window.console.log) window.console.log(message);
    };

    // Flash fallback logging - don't include this in production
    WEB_SOCKET_DEBUG = true;

    var pusher = new Pusher('a8a3a7b9b972e4107437');
    pusher.connection.bind('initialized', function(data) {
        $(".brand").css("color", "#3F9FD9");
    });
    pusher.connection.bind('connected', function(data) {
        $(".brand").css("color", "#fff");
    });
    pusher.connection.bind('disconnected', function(data) {
        $(".brand").css("color", "#3F9FD9");
    });

    var channel = pusher.subscribe('activity');
    channel.bind('new', function(data) {
        var elem = $(data.html);
        elem.hide();
        $(".activities").prepend(elem);
        elem.fadeIn();
    });
})();
