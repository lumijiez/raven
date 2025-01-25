<script>
    import { jwtToken } from "../stores/connection.js";
    import LoginBox from "../components/LoginBox.svelte";
    import UserBar from "../components/UserBar.svelte";
    import ChatContainer from "../components/ChatContainer.svelte";
</script>

<div class="w-screen h-screen bg-fuchsia-100 flex flex-col">
    {#if $jwtToken === ''}
        <LoginBox />
    {:else }
        <UserBar />
        <ChatContainer />
    {/if}
</div>