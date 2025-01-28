<script>
    import { chatList, selectedChatId, messages } from "../../stores/chats.js";
    import ChatListEntry from "./ChatListEntry.svelte";
    import ChatMessages from "./ChatMessages.svelte";
    import UserBar from "./UserBar.svelte";
    import { Search } from 'lucide-svelte';
    import { Input } from "$lib/shad/ui/input/index.js";
    import ConnectionStatus from "./ConnectionStatus.svelte";
    import {get} from "svelte/store";
    import {stompJsConnection} from "../../stores/connection.js";
    import {toast} from "svelte-sonner";
    import api from "$lib/axios.js";
    import {slide} from "svelte/transition";

    function addMessage(chatId, message) {
        console.log("CURRENT", $messages);
        messages.update(current => {
            const chatMessages = current[chatId] || [];

            return {
                ...current,
                [chatId]: [...chatMessages, message],
            };
        });
        console.log("UPDATED", $messages);
    }

    let searchQuery = '';

    $: filteredChats = $chatList.filter(chat =>
        chat.name.toLowerCase().includes(searchQuery.toLowerCase())
    );

    async function selectChat(chatId) {
        console.log(chatId);
        selectedChatId.set(chatId);
    }

    async function createChat() {
        if (name && chatPartner) {
            try {
                const response = await api.post('api/chat/create-direct', {name, chatPartner});
            } catch {
                toast("Error creating chat!");
            }
        }
    }

    async function subscribeChat(chatId) {
        try {
            get(stompJsConnection).subscribe(`/topic/chats/${chatId}`, message => {
                try {
                    const data = JSON.parse(message.body);

                    if (typeof data.timestamp === "string" && data.timestamp.includes(".")) {
                        data.timestamp = data.timestamp.replace(/(\.\d{3})\d+/, "$1");
                    }

                    console.group(`%cMessage Received - Chat ${chatId}`, 'color: lightblue; font-weight: bold');
                    console.log('Parsed Data:', data);
                    console.groupEnd();

                    addMessage(chatId, data);

                } catch (error) {
                    console.error("Message Processing Error:", error);
                    toast('Message Processing Error', {
                        message: error.message,
                        chatId: chatId,
                        messageBody: message.body,
                        stack: error.stack
                    });
                }
            });
        } catch (error) {
            console.error("Chat Subscription Error:", error);
            toast('Chat Subscription Error', {
                message: error.message,
                chatId: chatId,
                stack: error.stack
            });
        }
    }

    $: if ($chatList) {
        $chatList.forEach(chat => {
            subscribeChat(chat.id);
        })
    }

    let isDropdownOpen = false;
    let chatPartner = '';
    let name = '';
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

        <div class="p-3">
            <button
                    class="gilroy w-full py-2 bg-black text-white rounded-lg bg-gradient-to-l from-blue-500 to-purple-600"
                    on:click={() => isDropdownOpen = !isDropdownOpen}
            >
                ADD
            </button>
        </div>

        {#if isDropdownOpen}
            <div transition:slide class="relative p-3 bg-white border rounded-lg shadow-lg w-full">
                <div class="mb-4">
                    <label for="chat-name" class="block text-sm font-medium text-gray-700">Chat Name</label>
                    <Input
                            id="chat-name"
                            type="text"
                            bind:value={name}
                            placeholder="Enter chat name"
                            class="mt-1 block w-full border-gray-300 focus:border-blue-500 focus:ring focus:ring-blue-200"
                    />
                </div>

                <div class="mb-4">
                    <label for="chat-partner" class="block text-sm font-medium text-gray-700">Partner Username</label>
                    <Input
                            id="chat-partner"
                            type="text"
                            bind:value={chatPartner}
                            placeholder="Enter partner username"
                            class="mt-1 block w-full border-gray-300 focus:border-blue-500 focus:ring focus:ring-blue-200"
                    />
                </div>

                <div class="mt-4">
                    <button
                            class="w-full py-2 bg-black text-white rounded-lg"
                            on:click={createChat}
                    >
                        Create
                    </button>
                </div>
            </div>
        {/if}

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

<style>
    .gilroy {
        font-family: 'GilroyBoldItalic', sans-serif;
    }
</style>
