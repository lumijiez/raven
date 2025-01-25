import adapter from '@sveltejs/adapter-node';

/** @type {import('@sveltejs/kit').Config} */
const config = {
	kit: {
		adapter: adapter(),
		csp: {
			directives: {
				'script-src': ['nonce-', 'strict-dynamic']
			},
			reportOnly: {
				'script-src': ['nonce-', 'strict-dynamic'],
				'report-uri': ['/']
			},
			mode: "auto"
		}
	}
};

export default config;
