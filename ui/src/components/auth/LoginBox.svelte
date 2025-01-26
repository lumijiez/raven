<script lang="ts">
    import {isLoggedIn} from "../../stores/connection.js";
    import api from "$lib/axios.js";
    import { toast } from "svelte-sonner";
    import { Input } from "$lib/shad/ui/input/index.js";
    import { Label } from "$lib/shad/ui/label/index.js";
    import { Button } from "$lib/shad/ui/button/index.js";
    import { fade } from 'svelte/transition';

    export let authMode;

    let usernameOrEmail = '';
    let password = '';
    let loading = false;

    async function handleLogin() {
        if (loading) return;

        loading = true;
        try {
            await api.post('auth/login', {
                usernameOrEmail,
                password,
            });

            const loginCheck = await api.get('auth/check-login');

            if (loginCheck.status === 200) {
                isLoggedIn.set(true);
                toast.success("Logged in successfully.");
            }
        } catch (error) {
            toast.error(`Login Error (${error.response?.status || 'Unknown'}): ${error.response?.data?.error || error.message}`);
        } finally {
            loading = false;
        }
    }
</script>

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
                bind:value={usernameOrEmail}
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
        {loading ? 'Logging in...' : 'Sign In'}
    </Button>
</form>