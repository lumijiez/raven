<script>
    import {isLoggedIn} from "../stores/connection.js";
    import { Toaster } from "$lib/shad/ui/sonner";
    import UserBar from "../components/chat/UserBar.svelte";
    import ChatContainer from "../components/chat/ChatContainer.svelte";
    import AuthBox from "../components/auth/AuthBox.svelte";
    import Background from "../components/auth/Background.svelte";
    import {onMount} from "svelte";
    import {toast} from "svelte-sonner";
    import api from "$lib/axios.js";
    import ChatPage from "../components/chat/ChatPage.svelte";

    onMount(async () => {
        try {
            const response = await api.get('auth/check-login');

            if (response.status === 200) {
                isLoggedIn.set(true);
                toast.success("Refreshed session successfully.");
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