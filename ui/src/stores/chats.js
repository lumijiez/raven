import {writable} from "svelte/store";

export const selectedChatId = writable('');
export const messages = writable({});
export const chatList = writable([]);

export function addMessage(chatId, message) {
    messages.update(current => {
        const chatMessages = current[chatId] || [];
        return {
            ...current,
            [chatId]: [...chatMessages, message],
        };
    });
}

export function createChat(chatId, chatName) {
    chatList.update(current => [...current, { chatId, name: chatName }]);
    messages.update(current => {
        if (!current[chatId]) {
            current[chatId] = [];
        }
        return current;
    });
}