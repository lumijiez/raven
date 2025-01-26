<script>
    import { chatList, selectedChatId, messages } from "../../stores/chats.js";
    import ChatListEntry from "./ChatListEntry.svelte";
    import ChatMessages from "./ChatMessages.svelte";
    import { onMount } from "svelte";
    import api from "$lib/axios.js";
    import UserBar from "./UserBar.svelte";
    import { Search } from 'lucide-svelte';
    import { Input } from "$lib/shad/ui/input/index.js";
    import ConnectionStatus from "./ConnectionStatus.svelte";

    let searchQuery = '';

    $: filteredChats = $chatList.filter(chat =>
        chat.name.toLowerCase().includes(searchQuery.toLowerCase())
    );

    async function selectChat(chatId) {
        console.log(chatId);
        selectedChatId.set(chatId);
    }

    onMount(async () => {
        try {
            const response = await api.get('api/chat/get-all');
            chatList.set(response.data.chats);
        } catch (error) {
            console.error('Error fetching chats:', error);
        }
    });
</script>

<div class="flex h-screen w-screen">
    <div class="bg-white z-10 bg-opacity-65 flex flex-col flex-[0.25] border-r border-gray-200">
        <UserBar />

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
                    <Search size={20} />
                </div>
            </div>
        </div>

        <div class="flex flex-1 flex-col">
            <div class="overflow-y-auto flex-1">
                {#each filteredChats as chat}
                    <ChatListEntry
                            chatName={chat.name}
                            chatId={chat.id}
                            onSelect={() => selectChat(chat.id)}
                    />
                {/each}
            </div>

            <ConnectionStatus />
        </div>
    </div>

    <ChatMessages />
</div>
