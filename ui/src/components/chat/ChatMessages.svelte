<script>
    import { Button } from "$lib/shad/ui/button/index.js";
    import { Input } from "$lib/shad/ui/input/index.js";
    import { Paperclip, Send, Smile, MoreVertical, MessageCircle } from 'lucide-svelte';
    import api from "$lib/axios.js";
    import { id } from "../../stores/user.js";
    import { messages, selectedChatId } from "../../stores/chats.js";
    import {stompJsConnection} from "../../stores/connection.js";
    import {toast} from "svelte-sonner";
    import {chatList} from "../../stores/chats.js";
    import { tick } from "svelte";

    let messageListContainer;
    function scrollToBottom() {
        tick().then(() => {
            setTimeout(() => {
                if (messageListContainer) {
                    messageListContainer.scrollTop = messageListContainer.scrollHeight;
                }
            }, 10);
        });
    }

    const iconColors = [
        'from-blue-500 to-purple-600',
        'from-green-500 to-teal-600',
        'from-red-500 to-orange-600',
        'from-indigo-500 to-blue-600',
        'from-pink-500 to-rose-600'
    ];

    const randomColor = iconColors[Math.floor(Math.random() * iconColors.length)];

    $: if (selectedChatId) {
        fetchMessages($selectedChatId);
    }

    async function getUserName(userId) {

    }

    async function fetchMessages(chatId) {
        console.log(`Fetching messages for chatId: ${chatId}`);

        try {
            const response = await api.post('api/message/get', { chatId });

            console.log(`Raw response data for chatId ${chatId}:`, response.data);

            const sortedMessages = response.data.messageList.sort((a, b) =>
                new Date(a.timestamp) - new Date(b.timestamp)
            );

            console.log(`Sorted messages for chatId ${chatId}:`, sortedMessages);

            messages.update(current => {
                const updatedMessages = {
                    ...current,
                    [chatId]: sortedMessages
                };
                console.log(`Updated messages state after processing chatId ${chatId}:`, updatedMessages);
                return updatedMessages;
            });
        } catch (error) {
            console.error(`Error fetching messages for chatId ${chatId}:`, error);
            messages.update(current => {
                const updatedMessages = {
                    ...current,
                    [chatId]: []
                };
                console.log(`Messages state after error for chatId ${chatId}:`, updatedMessages);
                return updatedMessages;
            });
        }
    }

    let newMessage = '';

    async function sendMessage() {
        if (!newMessage.trim() || !$selectedChatId) return;

        if (!newMessage) {
            toast.error('Message content cannot be empty');
        }

        $stompJsConnection.send("/app/chat.send", {}, JSON.stringify({
            chatId: $selectedChatId,
            content: newMessage
        }));
        scrollToBottom();
        newMessage = '';
    }

    function getChatName() {
        const chat = $chatList.find(c => c.id === $selectedChatId);
        return chat ? chat.name : null;
    }

    function getUserCount() {
        const chat = $chatList.find(c => c.id === $selectedChatId);
        return chat ? chat.members.length : 0;
    }
</script>

<div class="flex flex-col flex-1 h-screen bg-gray-50">
    {#if $selectedChatId}
        <div class="min-h-18 shadow-sm bg-white z-10 p-3 flex items-center space-x-3">
            <div class={`bg-gradient-to-r ${randomColor} rounded-full w-10 h-10 flex items-center justify-center`}>
                <MessageCircle class="text-white" size={20}/>
            </div>
            <div class="flex-grow">
                <p class="text-gray-800 font-semibold">{getChatName()}</p>
                <p class="text-gray-800 text-sm">{getUserCount()} participants</p>
            </div>
            <Button variant="ghost" size="icon" class="text-gray-500">
                <MoreVertical size={20}/>
            </Button>
        </div>

        <div class="flex-grow overflow-y-auto p-4 space-y-3" bind:this={messageListContainer}>
            {#each $messages[$selectedChatId] || [] as message}
                <div class={`flex ${message.sender === $id ? 'justify-end' : 'justify-start'}`}>
                    <div class={`max-w-[70%] p-3 rounded-2xl
                        ${message.sender === $id
                            ? 'bg-blue-500 text-white'
                            : 'bg-white text-gray-800 shadow-sm'
                        }`}>
                        <p>{message.content}</p>
                        <p class="text-xs mt-1 opacity-70">
                            {new Date(message.timestamp).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})}
                        </p>
                    </div>
                </div>
            {/each}
        </div>

        <div class="p-2 border-t border-gray-200 bg-white z-10">
            <div class="flex items-center space-x-2">
                <Button variant="ghost" size="icon" class="text-gray-500">
                    <Paperclip size={20}/>
                </Button>
                <Button variant="ghost" size="icon" class="text-gray-500">
                    <Smile size={20}/>
                </Button>
                <div class="flex-grow">
                    <Input
                            type="text"
                            placeholder="Type a message"
                            bind:value={newMessage}
                            on:keydown={(e) => e.key === 'Enter' && sendMessage()}
                            class="w-full"
                    />
                </div>
                <Button
                        size="icon"
                        on:click={sendMessage}
                        disabled={!newMessage.trim()}
                >
                    <Send size={20}/>
                </Button>
            </div>
        </div>
    {:else}
        <div class="flex items-center justify-center h-full text-gray-500">
            Select a chat to start messaging
        </div>
    {/if}
</div>
