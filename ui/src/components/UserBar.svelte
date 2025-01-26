<script>
    import {id} from "../stores/user";
    import {username} from "../stores/user";
    import {email} from "../stores/user";
    import {onMount} from "svelte";
    import api from "$lib/axios";

    onMount(async () => {
        const response = await api.get('api/user/self');

        console.log(response);
        id.set(response.data.id);
        username.set(response.data.username);
        email.set(response.data.email);
    })
</script>

<div class="m-3 min-h-16 bg-fuchsia-300 rounded-lg p-2 flex justify-between items-center">
    <div>ID: {$id}</div>
    <div>Username: {$username}</div>
    <div>Email: {$email}</div>
</div>