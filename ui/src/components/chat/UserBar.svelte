<script>
    import { id, username, email } from "../../stores/user.js";
    import { onMount } from "svelte";
    import api from "$lib/axios.js";
    import {isLoggedIn} from "../../stores/connection.js";
    import {toast} from "svelte-sonner";
    import {LogOut, User} from "lucide-svelte";

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

<div class="relative bg-white shadow-md overflow-hidden h-20 flex items-center px-4">
    <div class="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-blue-500 to-purple-600"></div>
    <div class="flex items-center justify-between w-full">
        <div class="flex items-center space-x-4">
            <div class="bg-gradient-to-r from-blue-500 to-purple-600 rounded-full w-12 h-12 flex items-center justify-center">
                <User class="text-white" />
            </div>
            <div>
                <p class="text-gray-800 font-semibold">{$username}</p>
                <p class="text-gray-500 text-sm">{$email}</p>
            </div>
        </div>
        <button
                on:click={logout}
                class="text-gray-600 hover:bg-gray-100 px-3 py-1.5 rounded-md transition-colors flex items-center space-x-2"
        >
            <LogOut size={16} class="text-gray-500" />
            <span>Logout</span>
        </button>
    </div>
</div>