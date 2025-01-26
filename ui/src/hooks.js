export async function handle({ request, resolve }) {
    const userAgent = request.headers.get('user-agent');

    if (userAgent && /[^a-zA-Z0-9\-; ]/.test(userAgent)) {
        return new Response('Invalid User-Agent', { status: 400 });
    }

    return await resolve(request);
}