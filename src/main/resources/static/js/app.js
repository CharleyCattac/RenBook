
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
            processJsonResponse(comment)
        });
        if (chapterName === "") {
            stompClient.subscribe('/works/view/' + $("#principal").val() + '/' + workName + '?', function (comment) {
                processJsonResponse(comment)
            });
        }
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

function giveAssessment(assessment) {
    var authorName = $("#authorName").val();
    var principalName = $("#principal").val();
    if (authorName === principalName) {
        document.getElementById('assess_info').style = "";
        return;
    }
    stompClient.send("/dst/assessment", {}, JSON.stringify({
            'value': assessment,
            'workName': $("#workName").val()
        }
    ));
}

function processJsonResponse(response) {
    //console.log('response body: ' + response);
    if (response != null) {
        console.log('arrived json: ' + response.body);
        var purpose = JSON.parse(response.body).purpose;
        if (purpose === 'add') {
            document.getElementById('newComments').appendChild(createNewComment(JSON.parse(response.body).id,
                JSON.parse(response.body).authorName,
                JSON.parse(response.body).postTime,
                JSON.parse(response.body).text));
            if (document.getElementById('noComments') !== null) {
                document.getElementById('noComments').style.display = "none";
            }
        } else if (purpose === 'delete') {
            console.log('response deleted: ' + response.body);
            document.getElementById('comment_' + JSON.parse(response.body).id).remove();
        } else if (purpose === 'ass_suc') {
            console.log('assessment successful');
            document.getElementById('assess_success').style = "";
            //document.getElementById('u_').style = "";
        }
    } else {
        //$("#err").append("<p>NULL COMMENT RECEIVED</p>");
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
    authorNameDiv.setAttribute("class", "col-sm-7 primary-text-comment");

    var authorForm = document.createElement("FORM");
    authorForm.id = 'show_author_form' + commentId;
    authorForm.setAttribute("action", '/authors/' + authorName);
    authorForm.setAttribute("method", 'get');

    var authorA = document.createElement("A");
    authorA.setAttribute("class", "link link-dark text-output");
    authorA.style.marginLeft = "5px";
    authorA.addEventListener("click", function () {
        document.getElementById(this.parentNode.id).submit(); return false;
    });
    authorA.textContent = authorName;

    var dateTimeDiv = document.createElement("DIV");
    dateTimeDiv.setAttribute("class", "col-sm-4 secondary-text-comment");
    dateTimeDiv.textContent = moment(postTime).format('DD.MM.YYYY HH:mm:ss');

    var spanDiv = document.createElement("DIV");
    spanDiv.setAttribute("class", "col-sm-1");
    var deleteSpan = document.createElement('SPAN');
    deleteSpan.id = 'span_' + commentId;
    deleteSpan.className = 'secondary-text-comment close';
    deleteSpan.style.float = "right";
    deleteSpan.textContent = 'X';
    deleteSpan.setAttribute('sec:authorize', "isAuthenticated()");
    deleteSpan.addEventListener("click", function () {
        if (confirm(document.getElementById('question').value)) {
            var commentId = this.id.substr(5);
            deleteComment(commentId);
        } else {
            return false;
        }
    });

    var commentBodyDiv = document.createElement("DIV");
    commentBodyDiv.setAttribute("class", "comment-body text-output");
    commentBodyDiv.textContent = commentText;

    spanDiv.appendChild(deleteSpan);

    authorForm.appendChild(authorA);
    authorNameDiv.appendChild(authorForm);

    rowDiv.appendChild(authorNameDiv);
    rowDiv.appendChild(dateTimeDiv);
    rowDiv.appendChild(spanDiv);

    headDiv.appendChild(rowDiv);

    container.appendChild(headDiv);
    container.appendChild(commentBodyDiv);

    mainItem.appendChild(container);
    return mainItem;
}