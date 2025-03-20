const resizer = document.querySelector('.resizer');
const leftPanel = document.querySelector('.Navigation-Frame');
const viewFrame = document.querySelector('.View-frame');
const barFrame = document.querySelector('.Bar-frame');
let isResizing = false;

resizer.addEventListener('mousedown', (e) => {
    isResizing = true;
    document.addEventListener('mousemove', resize);
    document.addEventListener('mouseup', stopResize);
});

// Updated JavaScript
function toggleMobileMenu() {
    const navFrame = document.querySelector('.Navigation-Frame');
    const hamburger = document.querySelector('.hamburger-menu');
    navFrame.classList.toggle('active');
    hamburger.classList.toggle('active');
}

// Close menu when clicking outside
document.addEventListener('click', function(event) {
    const navFrame = document.querySelector('.Navigation-Frame');
    const hamburgerMenu = document.querySelector('.hamburger-menu');
    const hamburger = document.querySelector('.hamburger-menu');

    // Check if click is on the hamburger or inside the nav content
    const isClickOnHamburger = event.target.closest('.hamburger-menu');
    const isClickInsideNavContent = event.target.closest('.nav-links, .logo');

    // Close menu only if:
    // 1. Menu is active, and
    // 2. Click is NOT on the hamburger, and
    // 3. Click is NOT inside nav content
    if (navFrame.classList.contains('active') && !isClickOnHamburger && !isClickInsideNavContent) {
        navFrame.classList.remove('active');
        hamburgerMenu.classList.remove('active');
    }
});

// Close menu on window resize
window.addEventListener('resize', function() {
    if (window.innerWidth > 768) {
        document.querySelector('.Navigation-Frame').classList.remove('active');
        document.querySelector('.hamburger-menu').classList.remove('active');
    }
});




