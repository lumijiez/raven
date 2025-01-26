<script>
    import { isConnected } from "../../stores/connection.js";
    import { Lock, WifiOff } from 'lucide-svelte';

    const connectionColors = {
        connected: 'from-green-500 to-emerald-600',
        disconnected: 'from-red-500 to-rose-600'
    };
</script>

<style>
    @font-face {
        font-family: 'GilroyBoldItalic';
        src: url('/fonts/gilroy/Gilroy-BoldItalic.ttf') format('truetype');
        font-weight: 700;
        font-style: italic;
    }

    .gilroy {
        font-family: 'GilroyBoldItalic', sans-serif;
    }
</style>

<div
        class={`gilroy h-14 flex items-center justify-center gap-2
        text-white font-bold text-xl
        bg-gradient-to-r ${$isConnected
            ? connectionColors.connected
            : connectionColors.disconnected
        }
    `}
>
    {#if $isConnected}
        <Lock size={24} />
        CONNECTED
    {:else}
        <WifiOff size={24} />
        DISCONNECTED
    {/if}
</div>
