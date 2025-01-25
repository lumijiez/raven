<script>
    import { writable } from 'svelte/store';
    import LoginBox from './LoginBox.svelte';
    import RegisterBox from './RegisterBox.svelte';

    const authMode = writable('login');
</script>

<div class="max-w-md mx-auto mt-8 p-6 bg-white rounded-lg shadow-md">
    {#if $authMode === 'login'}
        <LoginBox {authMode} />
    {:else}
        <RegisterBox {authMode} />
    {/if}

    <div class="mt-4 text-center">
        {#if $authMode === 'login'}
            <p>
                Don't have an account?
                <button
                        on:click={() => $authMode = 'register'}
                        class="text-blue-500 hover:underline"
                >
                    Register
                </button>
            </p>
        {:else}
            <p>
                Already have an account?
                <button
                        on:click={() => $authMode = 'login'}
                        class="text-blue-500 hover:underline"
                >
                    Login
                </button>
            </p>
        {/if}
    </div>
</div>