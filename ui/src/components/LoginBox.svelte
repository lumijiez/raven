<script>
    import {jwtToken} from "../stores/connection.js";
    import api from "$lib/axios.js";

    let usernameOrEmail = '';
    let password = '';

    async function handleSubmit() {
        const response = await api.post('auth/login', {
            usernameOrEmail,
            password,
        });

        jwtToken.set(response.data.token);
    }
</script>

<form on:submit|preventDefault={handleSubmit} class="max-w-md mx-auto mt-8 p-6 bg-white rounded-lg shadow-md">
    <h2 class="text-2xl font-bold mb-6 text-center">Login</h2>

    <div class="mb-4">
        <label for="username" class="block mb-2 text-sm font-medium">
            Username
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
    >
        Login
    </button>
</form>