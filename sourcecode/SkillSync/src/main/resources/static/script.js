const resizer = document.querySelector('.resizer');
const leftPanel = document.querySelector('.Navigation-Frame');
let isResizing = false;

resizer.addEventListener('mousedown', (e) => {
    isResizing = true;
    document.addEventListener('mousemove', resize);
    document.addEventListener('mouseup', () => {
        isResizing = false;
        document.removeEventListener('mousemove', resize);
    });
});

function resize(e) {
    if (isResizing) {
        let newWidth = e.clientX;
        if (newWidth > 180 && newWidth < 200) { // Adjusted min/max width
            leftPanel.style.width = `${newWidth}px`;
        }
    }
}
