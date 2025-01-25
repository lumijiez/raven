<script>
    import { jwtToken } from "../../stores/connection.js";
    import api from "$lib/axios.js";
    import { toast } from "svelte-sonner";

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

<form on:submit|preventDefault={handleRegister}>
    <h2 class="text-2xl font-bold mb-6 text-center">Register</h2>

    <div class="mb-4">
        <label for="username" class="block mb-2 text-sm font-medium">
            Username
        </label>
        <input
                type="text"
                id="username"
                bind:value={username}
                class="w-full px-3 py-2 border rounded-md"
                required
        />
    </div>

    <div class="mb-4">
        <label for="email" class="block mb-2 text-sm font-medium">
            Email
        </label>
        <input
                type="email"
                id="email"
                bind:value={email}
                class="w-full px-3 py-2 border rounded-md"
                required
        />
    </div>

    <div class="mb-4">
        <label for="password" class="block mb-2 text-sm font-medium">
            Password
        </label>
        <input
                type="password"
                id="password"
                bind:value={password}
                class="w-full px-3 py-2 border rounded-md"
                required
        />
    </div>

    <div class="mb-6">
        <label for="confirm-password" class="block mb-2 text-sm font-medium">
            Confirm Password
        </label>
        <input
                type="password"
                id="confirm-password"
                bind:value={confirmPassword}
                class="w-full px-3 py-2 border rounded-md"
                required
        />
    </div>

    <button
            type="submit"
            class="w-full py-2 px-4 bg-blue-500 text-white rounded-md hover:bg-blue-600 disabled:opacity-50"
            disabled={loading}
    >
        {loading ? 'Registering...' : 'Register'}
    </button>
</form>