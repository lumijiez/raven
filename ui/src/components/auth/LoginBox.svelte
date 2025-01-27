<script>
    import {isLoggedIn} from "../../stores/connection.js";
    import api from "$lib/axios.js";
    import { toast } from "svelte-sonner";
    import { Input } from "$lib/shad/ui/input/index.js";
    import { Label } from "$lib/shad/ui/label/index.js";
    import { Button } from "$lib/shad/ui/button/index.js";
    import { fade } from 'svelte/transition';
    import connectWebSocket from "$lib/stomp";

    let email = '';
    let password = '';
    let loading = false;
    let showVerification = false;
    let currentCredentials = null;

    let codeInputs = Array(6).fill('');
    let inputRefs = Array(6).fill(null);

    function handleCodeInput(index, event) {
        const value = event.target.value;

        if (value.length <= 1 && /^\d*$/.test(value)) {
            codeInputs[index] = value;
            codeInputs = [...codeInputs];

            if (value && index < 5) {
                inputRefs[index + 1]?.focus();
            }
        }
    }

    function handleKeyDown(index, event) {
        if (event.key === 'Backspace' && !codeInputs[index] && index > 0) {
            inputRefs[index - 1]?.focus();
        }
    }

    async function handleLogin() {
        if (loading) return;

        loading = true;
        try {
            await api.post('auth/login/initiate', {
                email,
                password,
            });

            currentCredentials = { email, password };
            showVerification = true;
            toast.success("Verification code sent to your email.");
        } catch (error) {
            toast.error(`Login Error (${error.response?.status || 'Unknown'}): ${error.response?.data?.error || error.message}`);
        } finally {
            loading = false;
        }
    }

    async function handleVerification() {
        if (loading || !currentCredentials) return;

        const code = codeInputs.join('');
        if (code.length !== 6) return;

        loading = true;
        try {
            await api.post('auth/login/complete', {
                email: currentCredentials.email,
                verificationCode: code
            });

            const loginCheck = await api.get('auth/check-login');

            if (loginCheck.status === 200) {
                isLoggedIn.set(true);
                await connectWebSocket();
                toast.success("Logged in successfully.");
            }
        } catch (error) {
            toast.error(`Verification Error (${error.response?.status || 'Unknown'}): ${error.response?.data?.error || error.message}`);
        } finally {
            loading = false;
        }
    }

    $: isCodeComplete = codeInputs.every(value => value !== '');
</script>

{#if !showVerification}
    <form
            on:submit|preventDefault={handleLogin}
            class="space-y-4"
            in:fade
    >
        <div class="space-y-2">
            <Label for="username" class="text-gray-700">Username or Email</Label>
            <Input
                    type="text"
                    id="username"
                    bind:value={email}
                    placeholder="Enter username or email"
                    class="border-gray-300 focus:border-blue-500 focus:ring focus:ring-blue-200 transition-all"
                    required
            />
        </div>

        <div class="space-y-2">
            <div class="flex justify-between items-center">
                <Label for="password" class="text-gray-700">Password</Label>
                <a href="/forgot-password" class="text-sm text-blue-600 hover:underline">Forgot?</a>
            </div>
            <Input
                    type="password"
                    id="password"
                    bind:value={password}
                    placeholder="Enter password"
                    class="border-gray-300 focus:border-blue-500 focus:ring focus:ring-blue-200 transition-all"
                    required
            />
        </div>

        <Button
                type="submit"
                class="w-full bg-gradient-to-r from-blue-500 to-purple-600 hover:from-blue-600 hover:to-purple-700 transition-all"
                disabled={loading}
        >
            {loading ? 'Sending code...' : 'Sign In'}
        </Button>
    </form>
{:else}
    <div class="space-y-4" in:fade>
        <div class="space-y-2">
            <Label class="text-gray-700">Enter Verification Code</Label>
            <p class="text-sm text-gray-500">Please enter the 6-digit code sent to your email</p>
            <div class="flex gap-2 justify-between mt-2">
                {#each codeInputs as _, index}
                    <Input
                            type="text"
                            maxlength="1"
                            class="w-12 h-12 text-center text-xl border-2
                               {codeInputs[index] ? 'border-green-500' : 'border-gray-300'}
                               focus:border-blue-500 focus:ring focus:ring-blue-200 transition-all"
                            bind:value={codeInputs[index]}
                            bind:this={inputRefs[index]}
                            on:input={(e) => handleCodeInput(index, e)}
                            on:keydown={(e) => handleKeyDown(index, e)}
                    />
                {/each}
            </div>
        </div>

        <Button
                type="button"
                class="w-full bg-gradient-to-r from-blue-500 to-purple-600
                   hover:from-blue-600 hover:to-purple-700 transition-all"
                disabled={loading || !isCodeComplete}
                on:click={handleVerification}
        >
            {loading ? 'Verifying...' : 'Login'}
        </Button>
    </div>
{/if}