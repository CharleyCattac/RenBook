var stompClient = null;

function connect(workName, chapterName) {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        var subscribePostfix = '/' + workName;
        if (chapterName != null) {
            subscribePostfix = subscribePostfix + '/' + chapterName
        }
        stompClient.subscribe(subscribePostfix, function (comment) {
            showComment(JSON.parse(comment.body).text)
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendCommentToDB() {
    stompClient.send("/works/dst/comment", {}, JSON.stringify({
            'workName': $("#workName").val(),
            'chapterName': $("#chapterName").val(),
            'commentText': $("#comment").val(),
            'receiverId': $("#receiver").val(),
        }
        ));
}

function showComment(comment) {
    if (comment != null) {
        $("#newComments").append("<p>comment<p>");
    } else {
        $("#newComments").append("<p>NULL COMMENT RECEIVED<p>");
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    /*connect();
    $( "#saveComment" ).click(function() { sendCommentToDB(); });*/
    $( "#saveComment" ).click(function() { connect($("#workName").val(), $("#chapterName").val()); sendCommentToDB(); disconnect; });
});