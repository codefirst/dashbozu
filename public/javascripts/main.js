(function($, document, undefined){
    if(Dashbozu.websocket.enable) {
        if (!!console.log) {
            console.log("initialize websocket");
        }
        var ws = new WebSocket("ws://localhost:9000/ws");

        ws.onopen = function(){
            $(".brand").css("color", "#fff");
        }
        ws.onclose = function(){
            $(".brand").css("color", "#3F9FD9");
        }
        ws.onerror = function(){
            $(".brand").css("color", "#3F9FD9");
        }

        ws.onmessage = function(event) {
            var data = JSON.parse(event.data);
            if (!!console.log) {
                console.log(data);
            }
            var elem = $(data.html);
            elem.hide();
            $(".activities").prepend(elem);
            elem.fadeIn();

            notifyWebkitNotification(data);
        };
    }

    if(Dashbozu.pusher.enable) {
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

            notifyWebkitNotification(data);
        });
    }

    // Desktop Notification
    var enableWebkitNotification = function() {
        if (window.webkitNotifications) {
            if (window.webkitNotifications.checkPermission()) {
                window.webkitNotifications.requestPermission(function(){});
            }
        }
    }
    var notifyWebkitNotification = function(data) {
        var assetPrefix = '/assets/images/icons/';
        var assetExt = ".png";
        if (window.webkitNotifications) {
            if (!window.webkitNotifications.checkPermission()) {
                var popup = window.webkitNotifications.createNotification(
                    assetPrefix + data.source + assetExt, data.title, data.body
                );
                popup.show();
            }
        }
    }

    $(function() {
        if (window.webkitNotifications && window.webkitNotifications.checkPermission() != 0) {
            $('.top-right').click(function(){
                enableWebkitNotification();
            }).notify({
                fadeOut: { enabled: true, delay: 6000 },
                closable: false,
                type: 'inverse',
                message: { text: 'Click here to enable Desktop Notifications.' }
            }).show();
        }
    });

})(jQuery, document);
