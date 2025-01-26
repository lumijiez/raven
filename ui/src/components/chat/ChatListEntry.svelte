<script>
    export let chatId;
    export let chatName;
    export let onSelect;
    import { MessageCircle } from 'lucide-svelte';

    const iconColors = [
        'from-blue-500 to-purple-600',
        'from-green-500 to-teal-600',
        'from-red-500 to-orange-600',
        'from-indigo-500 to-blue-600',
        'from-pink-500 to-rose-600'
    ];

    const randomColor = iconColors[Math.floor(Math.random() * iconColors.length)];
</script>

<div
        tabindex="0"
        role="button"
        class="p-3 border-b border-gray-200 cursor-pointer hover:bg-gray-100 flex items-center space-x-3 transition-colors relative z-10 bg-white"
        on:click={onSelect}
        on:keydown={(e) => { if (e.key === 'Enter' || e.key === ' ') onSelect(); }}
>
    <div class={`bg-gradient-to-r ${randomColor} rounded-full w-10 h-10 flex items-center justify-center`}>
        <MessageCircle class="text-white" size={20} />
    </div>
    <div class="flex-grow">
        <p class="text-gray-800 font-semibold">{chatName}</p>
        <p class="text-gray-500 text-sm truncate">Hey, how are you doing today?</p>
    </div>
    <div class="flex flex-col items-end">
        <span class="text-xs text-gray-400 mb-1">2m</span>
        <div class="bg-blue-500 text-white text-xs rounded-full px-2 py-0.5">1</div>
    </div>
</div>