const BASE_URL = "http://localhost:8080/friends/api";

document.addEventListener("DOMContentLoaded", function () {
    loadFriendRequests();
    loadFriends();
});


function sendFriendRequest() {
    const receiver = document.getElementById("receiverUsername").value.trim();

    if (!receiver) {
        alert("Please enter the receiver's username.");
        return;
    }

    fetch(`${BASE_URL}/send/${receiver}`, { method: "POST" })
        .then(response => response.text())
        .then(data => {
            alert(data);
            document.getElementById("receiverUsername").value = ""; // Clear input field
        })
        .catch(error => console.error("Error:", error));
}


function loadFriendRequests() {
    fetch(`${BASE_URL}/pending`)
        .then(response => response.json())
        .then(data => {
            const requestsList = document.getElementById("requestsList");
            requestsList.innerHTML = ""; // Clear previous content

            if (!data || data.length === 0) {
                requestsList.innerHTML = "<li>No pending friend requests.</li>";
                return;
            }

            // Display requests with Accept/Reject buttons
            data.forEach(request => {
                const listItem = document.createElement("li");
                listItem.innerHTML = `
                    ${request.senderUsername} 
                    <button onclick="respondToRequest('${request.senderUsername}', true)">✅ Accept</button>
                    <button onclick="respondToRequest('${request.senderUsername}', false)">❌ Reject</button>
                `;
                requestsList.appendChild(listItem);
            });
        })
        .catch(error => console.error("Error loading pending requests:", error));
}


function respondToRequest(senderUsername, accept) {
    fetch(`${BASE_URL}/respond?senderUsername=${senderUsername}&accept=${accept}`, {
        method: "POST"
    })
        .then(response => response.text())
        .then(data => {
            alert(data);
            loadFriendRequests(); // Refresh pending requests
            loadFriends(); // Refresh friends list
        })
        .catch(error => console.error("Error responding to friend request:", error));
}


function loadFriends() {
    fetch(`${BASE_URL}/list`)
        .then(response => response.json())
        .then(data => {
            const friendsList = document.getElementById("friendsList");
            friendsList.innerHTML = ""; // Clear previous content

            if (!data || data.length === 0) {
                friendsList.innerHTML = "<li>You have no friends yet.</li>";
                return;
            }

            data.forEach(friend => {
                const listItem = document.createElement("li");
                listItem.innerHTML = `
                    <img src="${friend.profilePicture}" alt="Profile Picture" width="40">
                    ${friend.username} - Score: ${friend.score}
                `;
                friendsList.appendChild(listItem);
            });
        })
        .catch(error => console.error("Error loading friends:", error));
}
