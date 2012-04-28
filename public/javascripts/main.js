(function(){
    // Enable pusher logging - don't include this in production
    Pusher.log = function(message) {
        if (window.console && window.console.log) window.console.log(message);
    };

    // Flash fallback logging - don't include this in production
    WEB_SOCKET_DEBUG = true;

    var pusher = new Pusher('a8a3a7b9b972e4107437');
    var channel = pusher.subscribe('activity');
    channel.bind('new', function(data) {
        console.log(data);
    });
})();