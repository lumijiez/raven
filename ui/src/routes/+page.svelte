<script>
    import { jwtToken } from "../stores/connection.js";
    import { Toaster } from "$lib/shad/ui/sonner";
    import UserBar from "../components/UserBar.svelte";
    import ChatContainer from "../components/ChatContainer.svelte";
    import AuthBox from "../components/auth/AuthBox.svelte";
    import Background from "../components/auth/Background.svelte";
    import {onMount} from "svelte";
    import {toast} from "svelte-sonner";

    onMount(() => {
        const token = localStorage.getItem("token");
        if (token) {
            jwtToken.set(token);
            toast("Rejoined existing session.")
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