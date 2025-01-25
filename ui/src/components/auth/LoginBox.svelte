<script>
    import { jwtToken } from "../../stores/connection.js";
    import api from "$lib/axios.js";
    import { toast } from "svelte-sonner";
    import { goto } from "$app/navigation";

    export let authMode;

    let usernameOrEmail = '';
    let password = '';
    let loading = false;

    async function handleLogin() {
        if (loading) return;

        loading = true;
        try {
            const response = await api.post('auth/login', {
                usernameOrEmail,
                password,
            });

            const token = response.data.token;
            if (token && token.trim() !== '') {
                jwtToken.set(token);
                toast.success("Login successful!");
            } else {
                toast.error("Invalid token received");
            }
        } catch (error) {
            toast.error(`Login Error (${error.response?.status || 'Unknown'}): ${error.response?.data?.error || error.message}`);
        } finally {
            loading = false;
        }
    }
</script>

<form on:submit|preventDefault={handleLogin}>
    <h2 class="text-2xl font-bold mb-6 text-center">Login</h2>

    <div class="mb-4">
        <label for="username" class="block mb-2 text-sm font-medium">
            Username or Email
        </label>
        <input
                type="text"
                id="username"
                bind:value={usernameOrEmail}
                class="w-full px-3 py-2 border rounded-md"
                required
        />
    </div>

    <div class="mb-6">
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

    <button
            type="submit"
            class="w-full py-2 px-4 bg-blue-500 text-white rounded-md hover:bg-blue-600 disabled:opacity-50"
            disabled={loading}
    >
        {loading ? 'Logging in...' : 'Login'}
    </button>
</form>