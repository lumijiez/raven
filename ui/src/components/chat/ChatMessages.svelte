<script>
    import {Button} from "$lib/shad/ui/button/index.js";
    import {Input} from "$lib/shad/ui/input/index.js";
    import {Paperclip, Send, Smile, MoreVertical, MessageCircle} from 'lucide-svelte';
    import api from "$lib/axios.js";
    import {id} from "../../stores/user.js";
    import {messages, selectedChatId} from "../../stores/chats.js";

    const iconColors = [
        'from-blue-500 to-purple-600',
        'from-green-500 to-teal-600',
        'from-red-500 to-orange-600',
        'from-indigo-500 to-blue-600',
        'from-pink-500 to-rose-600'
    ];

    const randomColor = iconColors[Math.floor(Math.random() * iconColors.length)];

    $: if ($selectedChatId) {
        fetchMessages($selectedChatId);
    }

    async function fetchMessages(chatId) {
        try {
            const response = await api.post('api/message/get', { chatId });
            const sortedMessages = response.data.messageList.sort((a, b) =>
                new Date(a.timestamp) - new Date(b.timestamp)
            );
            messages.set(sortedMessages);
        } catch (error) {
            messages.set([]);
            console.error('Error fetching messages:', error);
        }
    }

    let newMessage = '';

    async function sendMessage() {
        if (!newMessage.trim() || !$selectedChatId) return;

        try {
            const response = await api.post('api/message/send', {
                chatId: $selectedChatId,
                content: newMessage
            });

            messages.update(msgs => {
                return [...msgs, response.data]
                    .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
            });

            newMessage = '';
        } catch (error) {
            console.error('Message send failed', error);
        }
    }
</script>

<div class="flex flex-col flex-1 h-screen bg-gray-50">
    {#if $selectedChatId}
        <div class="min-h-18 shadow-sm bg-white z-10 p-3 flex items-center space-x-3">
            <div class={`bg-gradient-to-r ${randomColor} rounded-full w-10 h-10 flex items-center justify-center`}>
                <MessageCircle class="text-white" size={20} />
            </div>
            <div class="flex-grow">
                <p class="text-gray-800 font-semibold">{$selectedChatId}</p>
            </div>
            <Button variant="ghost" size="icon" class="text-gray-500">
                <MoreVertical size={20} />
            </Button>
        </div>

        <div class="flex-grow overflow-y-auto p-4 space-y-3">
            {#each $messages as message}
                <div class={`flex ${message.sender === $id ? 'justify-end' : 'justify-start'}`}>
                    <div class={`
                        max-w-[70%] p-3 rounded-2xl
                        ${message.sender === $id
                            ? 'bg-blue-500 text-white'
                            : 'bg-white text-gray-800 shadow-sm'
                        }
                    `}>
                        <p>{message.content}</p>
                        <p class="text-xs mt-1 opacity-70">
                            {new Date(message.timestamp).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
                        </p>
                    </div>
                </div>
            {/each}
        </div>

        <div class="p-2 border-t border-gray-200 bg-white z-10">
            <div class="flex items-center space-x-2">
                <Button variant="ghost" size="icon" class="text-gray-500">
                    <Paperclip size={20} />
                </Button>
                <Button variant="ghost" size="icon" class="text-gray-500">
                    <Smile size={20} />
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
                    <Send size={20} />
                </Button>
            </div>
        </div>
    {:else}
        <div class="flex items-center justify-center h-full text-gray-500">
            Select a chat to start messaging
        </div>
    {/if}
</div>