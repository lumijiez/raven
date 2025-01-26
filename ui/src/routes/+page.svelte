<script>
    import {isLoggedIn, isConnected, sockJsConnection, stompJsConnection} from "../stores/connection.js";
    import { Toaster } from "$lib/shad/ui/sonner";
    import AuthBox from "../components/auth/AuthBox.svelte";
    import Background from "../components/auth/Background.svelte";
    import {onMount} from "svelte";
    import {toast} from "svelte-sonner";
    import api from "$lib/axios.js";
    import ChatPage from "../components/chat/ChatPage.svelte";
    import SockJS from 'sockjs-client';
    import { Stomp } from '@stomp/stompjs';

    import 'sockjs-client/lib/utils/browser-crypto.js';

    async function connectWebSocket() {
        const socket = new SockJS('https://lumijiez.pw/ws', null, {
            withCredentials: true
        });


        const stompClient = Stomp.over(socket);

        stompJsConnection.set(stompClient);
        sockJsConnection.set(socket);

        stompClient.connect(
            {},
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

    onMount(async () => {
        try {
            const response = await api.get('auth/check-login');

            if (response.status === 200) {
                isLoggedIn.set(true);
                toast.success("Refreshed session successfully.");

                await connectWebSocket();
            }
        } catch (error) {
            if (error.response?.status === 403) {
                toast.error("Not logged in, please do so.")
            } else {
                toast.error(`Login Error (${error.response?.status || 'Unknown'}): ${error.response?.data?.error || error.message}`);
            }
        }
    })
</script>

<Toaster />

<div class="w-screen h-screen flex flex-col">
    {#if !$isLoggedIn}
        <Background />
        <AuthBox />
    {:else }
        <ChatPage />
    {/if}
</div>