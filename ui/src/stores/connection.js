import {writable} from "svelte/store";

export const isLoggedIn = writable(false);

export const sockJsConnection = writable(null);
export const stompJsConnection = writable(null);

export const isConnected = writable(false);
