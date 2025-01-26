<script>
    import { jwtToken } from "../stores/connection.js";
    import { Toaster } from "$lib/shad/ui/sonner";
    import UserBar from "../components/UserBar.svelte";
    import ChatContainer from "../components/ChatContainer.svelte";
    import AuthBox from "../components/auth/AuthBox.svelte";
    import Background from "../components/auth/Background.svelte";
    import {onMount} from "svelte";
    import {toast} from "svelte-sonner";
    import api from "$lib/axios.js";

    onMount(async () => {
        try {
            const response = await api.get('auth/check-login');

            const token = response.data.token;
            if (token && token.trim() !== '') {
                jwtToken.set(token);
                toast.success("Login successful!");
            } else {
                toast.error("Invalid token received");
            }
        } catch (error) {
            toast.error(`Login Error (${error.response?.status || 'Unknown'}): ${error.response?.data?.error || error.message}`);
        } finally {
            loading = false;
        }
    })
</script>

<Toaster />

<div class="w-screen h-screen flex flex-col">
    {#if $jwtToken === ''}
        <Background />
        <AuthBox />
    {:else }
        <UserBar />
        <ChatContainer />
    {/if}
</div>