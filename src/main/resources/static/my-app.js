let stompClient = null;
let username;

function setUpConnect(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    console.log($('.show-when-connect'))
    if (connected) {
        $(".show-when-connect").removeClass('hidden');
        $("#conversation").show();
    } else {
        $(".show-when-connect").addClass('hidden')
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function showPublicMessage(message) {
    $("#greetings").append(`<tr><td> ${message.sender} => ${message.content} </td></tr>`);
}

function showPrivateMessage(message) {
    console.log(`in private message: ${message}`)
    $("#greetings").append(`<tr><td> private msg: ${message.sender} to me => ${message.content} </td></tr>`);
}

function onConnected() {
    console.log('connect successfully')
    setUpConnect(true);
    username = $("#sender").val();

    stompClient.subscribe('/topic/public.group', (message) => {
        showPublicMessage(JSON.parse(message.body));
    });

    stompClient.subscribe(`/username/${username}/topic.private`, message => {
        showPrivateMessage(JSON.parse(message.body));
    })
}

function onError() {
    console.log('connect ERROR')
}

function connect() {
    console.log('==> start connect');
    const sockJS = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(sockJS);
    stompClient.connect({}, onConnected, onError);

}

function disconnect() {
    console.log('==> disconnect');
    stompClient.disconnect();
    setUpConnect(false)
}

function sendMessage() {
    let url;
    const receiver = $('#receiver').val();
    url = receiver ? '/app-1/private-chat' : '/app-1/public-chat';
    stompClient.send(url,
        {},
        JSON.stringify({
            sender: $('#sender').val(),
            receiver: receiver,
            content: $('#content').val()
        }));
}

$(() => {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendMessage());
});