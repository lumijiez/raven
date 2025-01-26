<script>
    import { Button } from "$lib/shad/ui/button/index.js";
    import { Input } from "$lib/shad/ui/input/index.js";
    import { Send, Paperclip, Smile } from 'lucide-svelte';
    import api from "$lib/axios.js";
    import { username } from "../../stores/user.js";

    export let selectedChatId;
    export let messages;

    let newMessage = '';

    async function sendMessage() {
        if (!newMessage.trim() || !selectedChatId) return;

        try {
            const response = await api.post('api/message/send', {
                chatId: selectedChatId,
                content: newMessage
            });

            messages.update(msgs => [...msgs, {
                id: response.data.id,
                content: newMessage,
                timestamp: new Date().toISOString(),
                sender: $username
            }]);

            newMessage = '';
        } catch (error) {
            console.error('Message send failed', error);
        }
    }
</script>

<div class="flex flex-col flex-1 h-screen bg-gray-50">
    {#if selectedChatId}
        <div class="flex-grow overflow-y-auto p-4 space-y-3">
            {#each messages as message}
                <div class={`flex ${message.sender === $username ? 'justify-end' : 'justify-start'}`}>
                    <div class={`
                        max-w-[70%] p-3 rounded-2xl
                        ${message.sender === $username
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

        <div class="p-4 border-t border-gray-200 bg-white">
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