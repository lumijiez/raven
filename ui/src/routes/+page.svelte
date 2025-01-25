<script>
    import { jwtToken } from "../stores/connection.js";
    import { Toaster } from "$lib/shad/ui/sonner";
    import UserBar from "../components/UserBar.svelte";
    import ChatContainer from "../components/ChatContainer.svelte";
    import AuthBox from "../components/auth/AuthBox.svelte";
</script>

<Toaster />

<div class="w-screen h-screen bg-fuchsia-100 flex flex-col">
    {#if $jwtToken === ''}
        <AuthBox />
    {:else }
        <UserBar />
        <ChatContainer />
    {/if}
</div>