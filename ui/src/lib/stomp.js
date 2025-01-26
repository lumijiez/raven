import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {isConnected, sockJsConnection, stompJsConnection} from "../stores/connection.js";
import {toast} from "svelte-sonner";

async function connectWebSocket() {
    const socket = new SockJS('https://lumijiez.pw/ws', null, {
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