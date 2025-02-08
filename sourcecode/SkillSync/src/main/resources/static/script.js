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

function resize(e) {
    if (isResizing) {
        let newWidth = e.clientX - leftPanel.getBoundingClientRect().left;
        if (newWidth >= 180 && newWidth <= 250) {
            leftPanel.style.width = `${newWidth}px`;
            viewFrame.style.width = `calc(100% - ${newWidth}px)`;
            barFrame.style.width = `calc(100% - ${newWidth}px)`;
            barFrame.style.left = `${newWidth}px`;
        }
    }
}

function stopResize() {
    isResizing = false;
    document.removeEventListener('mousemove', resize);
    document.removeEventListener('mouseup', stopResize);
}
