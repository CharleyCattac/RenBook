
//hidden input creation
function createHiddenInput(hiddenInputId, hiddenInputName, itemValue) {
    var newHiddenInput = document.createElement('INPUT');
    newHiddenInput.setAttribute("type", "hidden");
    newHiddenInput.setAttribute("id", hiddenInputId);
    newHiddenInput.setAttribute("name", hiddenInputName);
    newHiddenInput.setAttribute("value", itemValue);
    return newHiddenInput;
}

//open websocket connection
function connect(stompClient, workName, chapterName) {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        var subscribePostfix = '/works/view/' + workName;
        if (chapterName !== '') {
            subscribePostfix = subscribePostfix + '/' + chapterName;
        }
        subscribePostfix = subscribePostfix + '?';
        console.log('subscribe postfix: ' + subscribePostfix);
        stompClient.subscribe(subscribePostfix, function (comment) {
            processJsonComment(comment)
        });
    });
    return stompClient;
}

function sendCommentToDB() {
    var commentText = $("#comment").val();
    if (commentText === '') {
        document.getElementById('emptyCommentAlert').style.display = "";
        return;
    } else {
        document.getElementById('comment').value = "";
        document.getElementById('commentCounter').value = "1000";
    }

    stompClient.send("/dst/add_comment", {}, JSON.stringify({
            'purpose': 'add',
            'workName': $("#workName").val(),
            'chapterName': $("#chapterName").val(),
            'chapterCount': $("#chapterCount").val(),
            'commentText': commentText
        }
    ));
}

function deleteComment(commentId) {
    stompClient.send("/dst/delete_comment", {}, JSON.stringify({
            'purpose': 'delete',
            'commentId': commentId,
            'workName': $("#workName").val(),
            'chapterName': $("#chapterName").val(),
            'chapterCount': $("#chapterCount").val()
        }
    ));
}

function processJsonComment(comment) {
    //console.log('comment body: ' + comment);
    if (comment != null) {
        console.log('arrived json: ' + comment.body);
        var purpose = JSON.parse(comment.body).purpose;
        if (purpose === 'add') {
            document.getElementById('newComments').appendChild(createNewComment(JSON.parse(comment.body).id,
                JSON.parse(comment.body).authorName,
                JSON.parse(comment.body).postTime,
                JSON.parse(comment.body).text));
            if (document.getElementById('noComments') !== null) {
                document.getElementById('noComments').style.display = "none";
            }
        } if (purpose === 'delete') {
            console.log('comment deleted: ' + comment.body);
            document.getElementById('comment_' + JSON.parse(comment.body).id).remove();
        }
    } else {
        $("#newComments").append("<p>NULL COMMENT RECEIVED</p>");
    }
}

function createNewComment(commentId, authorName, postTime, commentText) {
    var mainItem = document.createElement("DIV");
    mainItem.id = 'comment_' + commentId;
    mainItem.setAttribute("class", "comment-item");

    var container = document.createElement("DIV");
    container.setAttribute("class", "container-fluid");

    var headDiv = document.createElement("DIV");
    headDiv.setAttribute("class", "comment-head");

    var rowDiv = document.createElement("DIV");
    rowDiv.setAttribute("class", "row");
    rowDiv.style.paddingLeft = "5px";

    var authorNameDiv = document.createElement("DIV");
    authorNameDiv.setAttribute("class", "col-sm-7 primary-text");
    authorNameDiv.textContent = authorName;

    var dateTimeDiv = document.createElement("DIV");
    dateTimeDiv.setAttribute("class", "col-sm-4 secondary-text");
    dateTimeDiv.textContent = moment(postTime).format('DD.MM.YYYY HH:mm:ss');

    var spanDiv = document.createElement("DIV");
    spanDiv.setAttribute("class", "col-sm-1");
    var deleteSpan = document.createElement('SPAN');
    deleteSpan.id = 'span_' + commentId;
    deleteSpan.className = 'secondary-text close';
    deleteSpan.style.float = "right";
    deleteSpan.textContent = 'X';
    deleteSpan.setAttribute('sec:authorize', "isAuthenticated()");
    deleteSpan.addEventListener("click", function () {
        var commentId = this.id.substr(5);
        deleteComment(commentId);
    });

    var commentBodyDiv = document.createElement("DIV");
    commentBodyDiv.setAttribute("class", "comment-body text-output");
    commentBodyDiv.textContent = commentText;

    spanDiv.appendChild(deleteSpan);

    rowDiv.appendChild(authorNameDiv);
    rowDiv.appendChild(dateTimeDiv);
    rowDiv.appendChild(spanDiv);

    headDiv.appendChild(rowDiv);

    container.appendChild(headDiv);
    container.appendChild(commentBodyDiv);

    mainItem.appendChild(container);
    return mainItem;
}