<script>
    import { chatList } from "../stores/chats.js";
    import ChatListEntry from "./ChatListEntry.svelte";
    import { writable } from "svelte/store";
    import { onMount } from "svelte";
    import api from "$lib/axios.js";

    const selectedChatId = writable('');
    const messages = writable([]);

    $: if ($selectedChatId) {
        fetchMessages($selectedChatId);
    }

    async function fetchMessages(chatId) {
        try {
            const response = await api.post('api/message/get', { chatId });
            messages.set(response.data.messageList);
        } catch (error) {
            messages.set([]);
        }
    }

    onMount(async () => {
        const response = await api.get('api/chat/get-all');
        chatList.set(response.data);
    })
</script>

<div class="bg-fuchsia-200 flex-1 m-3 mt-0 rounded-lg flex overflow-clip">
    <div class="bg-fuchsia-300 flex-[0.3] p-3">
        {#each $chatList as chat}
            <ChatListEntry
                    chatName={chat.name}
                    chatId={chat.id}
                    onSelect={() => selectedChatId.set(chat.id)}
            />
        {/each}
    </div>

    <div class="flex flex-1 p-4">
        {#if $selectedChatId}
            <div class="w-full">
                {#each $messages as message}
                    <div class="mb-2 p-2 bg-white rounded">
                        <div class="text-sm text-gray-600">
                            {new Date(message.timestamp).toLocaleString()}
                        </div>
                        <div class="mt-1">
                            {message.content}
                        </div>
                    </div>
                {/each}
            </div>
        {:else}
            <div class="text-gray-500">Select a chat to view messages</div>
        {/if}
    </div>
</div>