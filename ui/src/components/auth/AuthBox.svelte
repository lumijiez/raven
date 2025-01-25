<script lang="ts">
    import { writable } from 'svelte/store';
    import { slide } from 'svelte/transition';
    import LoginBox from './LoginBox.svelte';
    import RegisterBox from './RegisterBox.svelte';
    import Background from "./Background.svelte";

    const authMode = writable('login');
    const modes = {
        login: {
            title: 'Welcome Back',
            subtitle: 'Sign in to continue',
            bgGradient: 'from-blue-500 to-purple-600',
            icon: 'login'
        },
        register: {
            title: 'Get Started',
            subtitle: 'Create your account',
            bgGradient: 'from-green-400 to-teal-500',
            icon: 'user-plus'
        }
    };
</script>

<div
        class="relative w-full max-w-md my-auto mx-auto bg-white shadow-2xl rounded-2xl overflow-hidden"
        in:slide
>
    <div class="font-gilroy flex justify-center align-middle pt-6">
        <h1 class="text-6xl">RAVEN</h1>
    </div>
    <div
            class={`absolute top-0 left-0 right-0 h-1 bg-gradient-to-r ${modes[$authMode].bgGradient}`}
    ></div>

    <div class="px-8 py-6">
        <div class="flex items-center mb-8">
            <div>
                <h2 class="text-3xl font-bold text-gray-800">
                    {modes[$authMode].title}
                </h2>
                <p class="text-gray-500">
                    {modes[$authMode].subtitle}
                </p>
            </div>
        </div>

        {#if $authMode === 'login'}
            <LoginBox {authMode} />
        {:else}
            <RegisterBox {authMode} />
        {/if}

        <div class="mt-6 text-center">
            {#if $authMode === 'login'}
                <p class="text-gray-600">
                    Don't have an account?
                    <button
                            on:click={() => $authMode = 'register'}
                            class="text-blue-600 font-semibold hover:underline"
                    >
                        Sign Up
                    </button>
                </p>
            {:else}
                <p class="text-gray-600">
                    Already have an account?
                    <button
                            on:click={() => $authMode = 'login'}
                            class="text-green-600 font-semibold hover:underline"
                    >
                        Sign In
                    </button>
                </p>
            {/if}
        </div>
    </div>
</div>