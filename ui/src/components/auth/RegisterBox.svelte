<script lang="ts">
    import { jwtToken } from "../../stores/connection.js";
    import api from "$lib/axios.js";
    import { toast } from "svelte-sonner";
    import { Input } from "$lib/shad/ui/input/index.js";
    import { Label } from "$lib/shad/ui/label/index.js";
    import { Button } from "$lib/shad/ui/button/index.js";
    import { slide } from 'svelte/transition';

    export let authMode;

    let username = '';
    let email = '';
    let password = '';
    let confirmPassword = '';
    let loading = false;

    async function handleRegister() {
        if (loading) return;

        if (password !== confirmPassword) {
            toast.error("Passwords do not match");
            return;
        }

        loading = true;
        try {
            const response = await api.post('auth/register', {
                username,
                email,
                password,
            });

            const token = response.data.token;
            if (token && token.trim() !== '') {
                jwtToken.set(token);
                toast.success("Registration successful!");
            } else {
                toast.error("Invalid token received");
            }
        } catch (error) {
            toast.error(`Registration Error (${error.response?.status || 'Unknown'}): ${error.response?.data?.error || error.message}`);
        } finally {
            loading = false;
        }
    }
</script>

<form
        on:submit|preventDefault={handleRegister}
        class="space-y-4"
        in:slide={{ axis: 'x', duration: 500 }}
>
    <div class="space-y-2">
        <Label for="username" class="text-gray-700">Username</Label>
        <Input
                type="text"
                id="username"
                bind:value={username}
                placeholder="Choose a username"
                class="border-gray-300 focus:border-green-500 focus:ring focus:ring-green-200 transition-all"
                required
        />
    </div>

    <div class="space-y-2">
        <Label for="email" class="text-gray-700">Email</Label>
        <Input
                type="email"
                id="email"
                bind:value={email}
                placeholder="Enter your email"
                class="border-gray-300 focus:border-green-500 focus:ring focus:ring-green-200 transition-all"
                required
        />
    </div>

    <div class="space-y-2">
        <Label for="password" class="text-gray-700">Password</Label>
        <Input
                type="password"
                id="password"
                bind:value={password}
                placeholder="Create a password"
                class="border-gray-300 focus:border-green-500 focus:ring focus:ring-green-200 transition-all"
                required
        />
    </div>

    <div class="space-y-2">
        <Label for="confirm-password" class="text-gray-700">Confirm Password</Label>
        <Input
                type="password"
                id="confirm-password"
                bind:value={confirmPassword}
                placeholder="Confirm your password"
                class="border-gray-300 focus:border-green-500 focus:ring focus:ring-green-200 transition-all"
                required
        />
    </div>

    <Button
            type="submit"
            class="w-full bg-gradient-to-r from-green-400 to-teal-500 hover:from-green-500 hover:to-teal-600 transition-all"
            disabled={loading}
    >
        {loading ? 'Registering...' : 'Create Account'}
    </Button>
</form>