import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {isConnected, sockJsConnection, stompJsConnection} from "../stores/connection.js";
import {toast} from "svelte-sonner";
import api from "$lib/axios.js";
import {chatList} from "../stores/chats.js";

async function connectWebSocket() {
    const socket = new SockJS('https://lumijiez.site/ws', null, {
        withCredentials: true
    });


    const stompClient = Stomp.over(socket);
    stompJsConnection.set(stompClient);
    sockJsConnection.set(socket);

    stompClient.connect(
        {
            withCredentials: true
        },
        () => {
            api.get('api/chat/get-all')
                .then(response => {
                    console.log(response.data.chats);
                    chatList.set(response.data.chats);
                })
                .catch(error => {
                    console.error('Error fetching chats:', error);
                });
            isConnected.set(true);
            console.log('WebSocket Connected');
        },
        (error) => {
            isConnected.set(false);
            console.error('WebSocket Connection Error:', error.message);
            toast.error(`WebSocket Connection Failed: ${error.message}`);
        }
    );
}

export default connectWebSocket;