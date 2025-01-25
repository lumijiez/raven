import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';
import  fs from 'fs';

export default defineConfig({
	plugins: [sveltekit()],
	server: {
		allowedHosts: ['ui'],
		host: '0.0.0.0',

		watch: {
			usePolling: true,
		},
		port: 80
	}
});
