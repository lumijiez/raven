<script lang="ts">
    import { writable } from 'svelte/store';
    import LoginBox from './LoginBox.svelte';
    import RegisterBox from './RegisterBox.svelte';

    const authMode = writable('login');
    const modes = {
        register: {
            title: 'Get Started',
            subtitle: 'Create your account',
            bgGradient: 'from-green-400 to-teal-500',
            icon: 'user-plus'
        },
        login: {
            title: 'Welcome Back',
            subtitle: 'Sign in to continue',
            bgGradient: 'from-blue-500 to-purple-600',
            icon: 'login'
        }
    };
</script>

<style>
    @font-face {
        font-family: 'GilroyBoldItalic';
        src: url('/fonts/gilroy/Gilroy-BoldItalic.ttf') format('truetype');
        font-weight: 700;
        font-style: italic;
    }

    .icon {
        font-family: 'GilroyBoldItalic', sans-serif;
    }

</style>

<div class="h-screen w-screen flex justify-center items-center">
    <div class="ml-2 mr-2 bg-white w-full max-w-md h-fit shadow-2xl rounded-2xl overflow-hidden relative">
        <div class="icon flex justify-center items-center pt-6">
            <img src="./icon.png" alt="Raven Icon" class="object-contain h-[50px]"/>
            <h1 class="text-6xl leading-[50px]">RAVEN</h1>
        </div>
        <div class={`absolute top-0 left-0 right-0 h-1 bg-gradient-to-r ${modes[$authMode].bgGradient}`}></div>

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
                <LoginBox />
            {:else}
                <RegisterBox />
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
</div>
