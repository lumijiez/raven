<script>
    import { onMount } from 'svelte';
    import { fade } from 'svelte/transition';
    let canvas;
    let ctx;
    let showCanvas = true;

    function createParticle(width, height) {
        const colors = [
            'rgba(59, 130, 246, 0.3)',
            'rgba(16, 185, 129, 0.3)',
            'rgba(168, 85, 247, 0.3)'
        ];

        return {
            x: Math.random() * width,
            y: Math.random() * height,
            radius: Math.random() * 3 + 1,
            dx: (Math.random() - 0.5) * 0.5,
            dy: (Math.random() - 0.5) * 0.5,
            opacity: Math.random() * 0.5 + 0.2,
            color: colors[Math.floor(Math.random() * colors.length)]
        };
    }

    function initParticles(width, height) {
        return Array(100).fill(null).map(() => createParticle(width, height));
    }

    let particles = [];


    function animateParticles() {
        if (!canvas || !ctx) return;

        ctx.clearRect(0, 0, canvas.width, canvas.height);

        particles.forEach(particle => {
            particle.x += particle.dx;
            particle.y += particle.dy;

            if (particle.x < 0) particle.x = canvas.width;
            if (particle.x > canvas.width) particle.x = 0;
            if (particle.y < 0) particle.y = canvas.height;
            if (particle.y > canvas.height) particle.y = 0;

            ctx.beginPath();
            ctx.arc(particle.x, particle.y, particle.radius, 0, Math.PI * 2);
            ctx.fillStyle = particle.color;
            ctx.fill();
            ctx.closePath();

            particles.forEach(other => {
                const dx = particle.x - other.x;
                const dy = particle.y - other.y;
                const distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < 100) {
                    ctx.beginPath();
                    ctx.moveTo(particle.x, particle.y);
                    ctx.lineTo(other.x, other.y);
                    ctx.strokeStyle = `rgba(96, 165, 250, ${1 - distance/100})`;
                    ctx.lineWidth = 0.5;
                    ctx.stroke();
                }
            });
        });

        requestAnimationFrame(animateParticles);
    }

    onMount(() => {
        if (!canvas) return;

        ctx = canvas.getContext('2d');
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;

        particles = initParticles(canvas.width, canvas.height);
        animateParticles();

        const handleResize = () => {
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;
            particles = initParticles(canvas.width, canvas.height);
        };

        window.addEventListener('resize', handleResize);
        return () => {
            window.removeEventListener('resize', handleResize);
        };
    });
</script>

<canvas in:fade={{ duration: 1000 }}
        bind:this={canvas}
        class="fixed inset-0 w-full h-full pointer-events-none"
></canvas>