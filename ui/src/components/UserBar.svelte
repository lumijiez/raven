<script>
    import { id, username, email } from "../stores/user";
    import { onMount } from "svelte";
    import api from "$lib/axios";
    import {isLoggedIn} from "../stores/connection.js";
    import {toast} from "svelte-sonner";

    onMount(async () => {
        const response = await api.get('api/user/self');

        console.log(response);
        id.set(response.data.id);
        username.set(response.data.username);
        email.set(response.data.email);
    });

    async function logout() {
        try {
            const response = await api.get('auth/logout');

            if (response.status === 200) {
                isLoggedIn.set(false);
                toast.success("Logged out successfully.");
            }

        } catch (error) {
            toast.error(`Logout Error: ${error.response?.data?.error || error.message}`);
        }
    }
</script>

<div class="m-3 min-h-16 bg-fuchsia-300 rounded-lg p-2 flex justify-between items-center">
    <div>ID: {$id}</div>
    <div>Username: {$username}</div>
    <div>Email: {$email}</div>

    <button on:click={logout} class="bg-red-500 text-white p-2 rounded-lg ml-4">
        Logout
    </button>
</div>
