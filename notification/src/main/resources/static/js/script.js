'use strict';
document.querySelector('#loginForm').addEventListener('submit', submit, true)

var stompClient = null;
var token = null;
var name = null;

function submit(event) {
    if (event != null)
        event.preventDefault();

    name = document.querySelector('#login').value.trim();
    var password = document.querySelector('#password').value.trim();

    authenticate(name, password);
}

function connect() {

    if (token == null) {
        notify("error", "Authentication failed", 2000)
        return;
    }

    document.querySelector('#login-page').classList.add('visually-hidden');
    document.querySelector('#dialogue-page').classList.remove('visually-hidden');

    var socket = new SockJS(webSocketUrl + '/wsnotification');
    stompClient = Stomp.over(socket);
    stompClient.connect({"accesstoken": token}, connectionSuccess, disconnectCallback);
}

function disconnectCallback() {
    console.info("Disconnect detected. reconnecting");
    window.setInterval(connect(), 2000);
}

function connectionSuccess() {
    stompClient.subscribe('/topic/notification', onMessageReceived);
    stompClient.subscribe('/user/topic/notification', function (payload) {

        var message = JSON.parse(payload.body);
        notify(message.type, message.content, 5000, message.sender);
    });

    stompClient.send("/app/chat.newUser", {}, JSON.stringify({
        sender : name,
        type : 'newUser'
    }))
}

function onMessageReceived(payload) {

    var message = JSON.parse(payload.body);
    var theme = "success";

    if (message.type == "newUser") {
        theme = 'info';
        message.content = message.sender + " has just logged in";
    }

    notify("info", message.sender + " has just logged in", 3000)
}

async function postData(url = '', data = {}) {
    // Default options are marked with *
    const response = await fetch(url, {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        mode: 'cors', // no-cors, *cors, same-origin
        cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
        headers: {
            'Content-Type': 'application/json'
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        redirect: 'follow', // manual, *follow, error
        referrerPolicy: 'no-referrer', // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
        body: JSON.stringify(data) // body data type must match "Content-Type" header
    });
    return response.json(); // parses JSON response into native JavaScript objects
}

function authenticate (username, password) {

    var data = {
            "userName": username,
            "password": password
    }

    postData(loginUrl, data).then(response => {
        if (response.successfull){
            token = response.authToken.accessToken;
            connect();
        }
        else
        {
            notify("error", "Authentication failed", 2000)
        }
    });
}

function notify (type, message, duration, sender){

    var theme = type;
    if (theme != "info" && theme != "error" && theme != "success" && theme != "warning")
        theme = "info";

    var hasSender = !(sender == null || typeof(sender) === typeof(sender));

    if (hasSender)
        message = sender + ": " + message;

    window.createNotification({
        theme: theme,
        showDuration: duration
    })({
        message: message
    });
}