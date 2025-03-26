const BASE_URL = "http://localhost:8080/friends/api";

// Sends a friend request to the specified username
function sendFriendRequest() {
    const receiver = document.getElementById("receiverUsername").value;

    if (!receiver) {
        alert("Please enter a username.");
        return;
    }

    fetch(`${BASE_URL}/send/${receiver}`, { method: "POST" })
        .then(response => response.text())
        .then(data => {
            alert(data);
            loadFriendRequests(); // Refresh pending friend requests after sending
        })
        .catch(error => console.error("Error:", error));
}

// Loads pending friend requests and displays them in the UI
function loadFriendRequests() {
    fetch(`${BASE_URL}/pending`)
        .then(response => response.json())
        .then(data => {
            const requestsList = document.getElementById("requestsList");
            requestsList.innerHTML = "";

            if (data.length === 0) {
                requestsList.innerHTML = "<li>No pending friend requests.</li>";
                return;
            }

            data.forEach(request => {
                const listItem = document.createElement("li");
                listItem.innerHTML = `
                    ${request.senderUsername} 
                    <button onclick="respondToRequest('${request.senderUsername}', true)">Accept</button>
                    <button onclick="respondToRequest('${request.senderUsername}', false)">Reject</button>
                `;
                requestsList.appendChild(listItem);
            });
        })
        .catch(error => console.error("Error:", error));
}

// Accepts or rejects a friend request based on user input
function respondToRequest(sender, accept) {
    fetch(`${BASE_URL}/respond?senderUsername=${sender}&accept=${accept}`, { method: "POST" })
        .then(response => response.text())
        .then(data => {
            alert(data);
            loadFriendRequests(); // Refresh pending requests
            loadFriends(); // Update the friend list if accepted
        })
        .catch(error => console.error("Error:", error));
}

// Loads the list of friends and updates the UI
function loadFriends() {
    fetch(`${BASE_URL}/list`)
        .then(response => response.json())
        .then(data => {
            const friendsList = document.getElementById("friendsList");
            friendsList.innerHTML = "";

            if (data.length === 0) {
                friendsList.innerHTML = "<li>You have no friends yet.</li>";
                return;
            }

            data.forEach(friend => {
                const listItem = document.createElement("li");
                listItem.innerHTML = `
                    <img src="${friend.profilePicture}" alt="Profile Picture">
                    ${friend.username} - Score: ${friend.score}
                    <button class="remove-btn" onclick="removeFriend('${friend.username}')">Remove</button>
                `;
                friendsList.appendChild(listItem);
            });
        })
        .catch(error => console.error("Error:", error));
}

// Removes a friend from the friend list
function removeFriend(friendUsername) {
    if (!confirm(`Are you sure you want to remove ${friendUsername}?`)) return;

    fetch(`${BASE_URL}/remove/${friendUsername}`, { method: "DELETE" })
        .then(response => response.text())
        .then(data => {
            alert(data);
            loadFriends(); //  Refresh friend list
            loadFriendRequests(); //  Refresh pending requests in case of issues
        })
        .catch(error => console.error("Error:", error));
}


// Ensures that friend requests and friend list are loaded when the page is fully loaded
document.addEventListener("DOMContentLoaded", () => {
    loadFriendRequests();
    loadFriends();
});
