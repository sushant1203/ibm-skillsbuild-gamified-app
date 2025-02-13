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

let slideNum = 1;
showSlides(slideNum);

function plusSlides(n) {
    showSlides(slideNum += n);
}
function currentSlide(n) {
    showSlides(slideNum  = n);
}
function showSlides(n) {
    let j;
    let slides = document.getElementsByClassName("slides");
    if (n > slides.length) {slideNum = 1
    }
    if (n < 1) {slideNum = slides.length
    }
    for (j = 0; j < slides.length; j++) {
        slides[j].style.display = "none";
    }
    slides[slideNum -1].style.display = "block";
}
