<script>
    export let chatId;
    export let chatName;
    export let onSelect;
</script>

<div
        tabindex="0"
        role="button"
        class="p-2 border-b border-fuchsia-400 cursor-pointer hover:bg-fuchsia-400 flex-col"
        on:click={onSelect}
        on:keydown={(e) => { if (e.key === 'Enter' || e.key === ' ') onSelect(); }}
>
    <div>
        {chatName}
    </div>
</div>

