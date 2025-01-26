import {writable} from "svelte/store";

export const isLoggedIn = writable(false);

export const connection = writable(null);

export const isConnected = writable(false);
