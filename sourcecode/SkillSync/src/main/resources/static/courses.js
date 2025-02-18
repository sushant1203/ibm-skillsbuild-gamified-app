let currentPage = 0;
const coursesPerPage = 8;
let filteredCourses = []; // Stores filtered courses
let totalPages = 1;

// Fetch all courses on page load
document.addEventListener("DOMContentLoaded", () => {
    fetchCourses(); // Load courses initially
});

// Function to fetch all courses
function fetchCourses() {
    fetch("/courses/filter") // Fetch all courses initially
        .then(response => response.json())
        .then(data => {
            filteredCourses = data;
            totalPages = Math.ceil(filteredCourses.length / coursesPerPage);
            renderCourses();
        })
        .catch(error => console.error("Error fetching courses:", error));
}

// Function to filter courses
function filterCourses() {
    const category = document.getElementById("category").value;
    const difficulty = document.getElementById("difficulty").value;

    let url = `/courses/filter?`;
    if (category) url += `category=${category}&`;
    if (difficulty) url += `difficulty=${difficulty}`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            filteredCourses = data; // Store filtered courses
            currentPage = 0; // Reset to first page
            totalPages = Math.ceil(filteredCourses.length / coursesPerPage);
            renderCourses();
        })
        .catch(error => console.error("Error fetching filtered courses:", error));
}

// Function to render courses with pagination
function renderCourses() {
    const courseContainer = document.getElementById("courseContainer");
    courseContainer.innerHTML = '';

    const startIndex = currentPage * coursesPerPage;
    const endIndex = startIndex + coursesPerPage;
    const currentCourses = filteredCourses.slice(startIndex, endIndex);

    currentCourses.forEach(course => {
        const courseCard = document.createElement("div");
        courseCard.classList.add("course-card");

        courseCard.innerHTML = `
            <img src="${course.imagePath ? course.imagePath : '/images/default-course.jpg'}" alt="${course.title}">
            <div class="content">
                <h1>${course.difficulty}</h1>
                <p>${course.title}</p>
                <div class="button-group">
                    <button type="button" class="btn" onclick="window.location.href='/quiz/${course.id}'">Start Quiz</button>
                    <button type="button" class="btn" onclick="window.location.href='${course.links}'">Start Course</button>
                </div>
            </div>
        `;

        courseContainer.appendChild(courseCard);
    });

    // Update navigation arrows visibility
    updateNavigationButtons();
}

// Function to change page (handles next/previous clicks)
function changePage(direction) {
    currentPage += direction;

    if (currentPage < 0) {
        currentPage = totalPages - 1;
    } else if (currentPage >= totalPages) {
        currentPage = 0;
    }

    renderCourses();
}

// Function to update visibility of Next/Prev buttons
function updateNavigationButtons() {
    const nextButton = document.querySelector(".next");
    const prevButton = document.querySelector(".prev");

    nextButton.style.display = totalPages > 1 ? "block" : "none";
    prevButton.style.display = totalPages > 1 ? "block" : "none";
}

document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("search");
    const courseContainer = document.getElementById("courseContainer");

    // Sample course data
    const courses = [
        { name: "Artificial Intelligence Fundamentals", category: "AI", image: "ai.jpg" },
        { name: "Web Development Fundamentals", category: "Web Development", image: "webdev.jpg" },
        { name: "Data Fundamentals", category: "Data Analyst", image: "data.jpg" },
        { name: "Cybersecurity Fundamentals", category: "Cyber Security", image: "cyber.jpg" },
        { name: "Building Trustworthy AI Enterprise Solutions", category: "AI", image: "ai2.jpg" },
        { name: "Cloud Computing Fundamentals", category: "Web Development", image: "cloud.jpg" },
        { name: "Professional Skills", category: "Computing Education", image: "skills.jpg" },
        { name: "Getting Started with Cybersecurity", category: "Cyber Security", image: "cyber2.jpg" },
        { name: "Machine Learning for Data Science Projects", category: "AI", image: "ml.jpg" },
        { name: "Getting Started with Threat Intelligence and Hunting", category: "Cyber Security", image: "threat.jpg" },
        { name: "Journey to Cloud: Orchestrating Your Solution", category: "Web Development", image: "cloud2.jpg" },
        { name: "Getting Started with Data", category: "Data Analyst", image: "data2.jpg" },
        { name: "Fundamentals of Sustainability and Technology", category: "Computing Education", image: "sustainability.jpg" },
        { name: "Enterprise Security in Practice", category: "Cyber Security", image: "security.jpg" }
    ];

    function displayCourses(filteredCourses) {
        courseContainer.innerHTML = ""; // Clear previous results

        if (filteredCourses.length === 0) {
            courseContainer.innerHTML = `<p style="color: white; font-size: 18px;">No matching courses found.</p>`;
            return;
        }

        filteredCourses.forEach(course => {
            const courseElement = document.createElement("div");
            courseElement.classList.add("course-card");
            courseElement.innerHTML = `
                <img src="${course.image}" alt="Course Image">
                <div class="content">
                    <h1>${course.name}</h1>
                    <p>Category: ${course.category}</p>
                    <div class="button-group">
                        <button>View Course</button>
                    </div>
                </div>
            `;
            courseContainer.appendChild(courseElement);
        });
    }

    function searchCourses() {
        const searchTerm = searchInput.value.toLowerCase();

        const filteredCourses = courses.filter(course =>
            course.name.toLowerCase().includes(searchTerm) ||
            course.category.toLowerCase().includes(searchTerm)
        );

        displayCourses(filteredCourses);
    }

    searchInput.addEventListener("input", searchCourses);

    // Display all courses initially
    displayCourses(courses);
});

