import {writable, get} from "svelte/store";
export const selectedChatId = writable('');
export const messages = writable({});
export const chatList = writable([]);