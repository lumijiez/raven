<script>
    import { chatList, selectedChatId, messages } from "../../stores/chats.js";
    import ChatListEntry from "./ChatListEntry.svelte";
    import ChatMessages from "./ChatMessages.svelte";
    import { onMount } from "svelte";
    import api from "$lib/axios.js";
    import UserBar from "./UserBar.svelte";
    import { Search } from 'lucide-svelte';
    import { Input } from "$lib/shad/ui/input/index.js";

    let searchQuery = '';

    $: filteredChats = $chatList.filter(chat =>
        chat.name.toLowerCase().includes(searchQuery.toLowerCase())
    );

    $: if ($selectedChatId) {
        fetchMessages($selectedChatId);
    }

    async function fetchMessages(chatId) {
        try {
            const response = await api.post('api/message/get', {chatId});
            messages.set(response.data.messageList);
        } catch (error) {
            messages.set([]);
        }
    }

    onMount(async () => {
        const response = await api.get('api/chat/get-all');
        chatList.set(response.data.chats);
    })
</script>

<div class="flex h-screen">
    <div class="bg-white flex-col flex-[0.3] border-r border-gray-200">
        <UserBar/>

        <div class="p-3 border-b border-gray-200">
            <div class="relative">
                <Input
                        type="text"
                        id="chat-search"
                        bind:value={searchQuery}
                        placeholder="Search chats"
                        class="border-gray-300 focus:border-blue-500 focus:ring focus:ring-blue-200 transition-all pl-10"
                />
                <div class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400">
                    <Search size={20}/>
                </div>
            </div>
        </div>

        <div class="overflow-y-auto h-[calc(100vh-200px)]">
            {#each filteredChats as chat}
                <ChatListEntry
                        chatName={chat.name}
                        chatId={chat.id}
                        onClick={() => selectedChatId.set(chat.id)}
                />
            {/each}
        </div>
    </div>

    <ChatMessages />
</div>