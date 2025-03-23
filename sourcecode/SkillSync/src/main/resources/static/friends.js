const BASE_URL = "http://localhost:8080/friends/api";

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
            loadFriendRequests(); // ✅ Reload pending requests after sending
        })
        .catch(error => console.error("Error:", error));
}


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

function respondToRequest(sender, accept) {
    fetch(`${BASE_URL}/respond?senderUsername=${sender}&accept=${accept}`, { method: "POST" })
        .then(response => response.text())
        .then(data => {
            alert(data);
            loadFriendRequests(); // ✅ Refresh pending requests
            loadFriends(); // ✅ Refresh friend list
        })
        .catch(error => console.error("Error:", error));
}

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

// ✅ Remove Friend
function removeFriend(friendUsername) {
    if (!confirm(`Are you sure you want to remove ${friendUsername}?`)) return;

    fetch(`${BASE_URL}/remove/${friendUsername}`, { method: "DELETE" })
        .then(response => response.text())
        .then(data => {
            alert(data);
            loadFriends(); // ✅ Refresh the friend list after removal
        })
        .catch(error => console.error("Error:", error));
}

document.addEventListener("DOMContentLoaded", () => {
    loadFriendRequests();
    loadFriends();
});
