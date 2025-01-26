import adapter from '@sveltejs/adapter-node';

/** @type {import('@sveltejs/kit').Config} */
const config = {
	kit: {
		adapter: adapter(),
		csp: {
			directives: {
				'script-src': ['self', 'https://lumijiez.pw'],
				'style-src': ['self', 'https://lumijiez.pw'],
				'default-src': ['self', 'https://lumijiez.pw'],
				'frame-ancestors': ['self', 'https://lumijiez.pw'],
				'form-action': ['self', 'https://lumijiez.pw']
			},
			mode: "auto"
		}
	}
};

export default config;
