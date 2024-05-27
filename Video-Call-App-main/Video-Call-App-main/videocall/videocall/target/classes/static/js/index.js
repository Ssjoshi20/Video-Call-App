function loadAndDisplayUsers() {
    // check if the user is connected
    const connectedUser = localStorage.getItem('connectedUser');
    if (!connectedUser) {
        window.location = 'login.html';
        return;
    }

    const userListElement = document.getElementById("userList");
    // Clear any existing content in the userListElement
    userListElement.innerHTML = "Loading...";
    // Retrieve the userList from the server
    fetch('http://localhost:8080/api/v1/users')
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
            displayUsers(data, userListElement);
        });
}

function displayUsers(userList, userListElement) {
    userListElement.innerHTML = "";

    // Loop through the userList and create list items to display each user
    userList.forEach(user => {
        const listItem = document.createElement("li");
        listItem.innerHTML = `
                <div>
                    <i class="fa fa-user-circle"></i>
                    ${user.username} <i class="user-email">(${user.email})</i>
                </div>
                <i class="fa fa-lightbulb-o ${user.status === "online" ? "online" : "offline"}"></i>
            `;

        // Add a class to the user element based on their status
        if (user.status === "offline") {
            listItem.classList.add("offline-user");
        }

        userListElement.appendChild(listItem);
    });
}

// Call the loadAndDisplayUsers function when the page loads
window.addEventListener("load", loadAndDisplayUsers);

function handleLogout() {
    console.log('Logging out...');
    // Now update the database (if necessary)
    fetch('http://localhost:8080/api/v1/users/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: localStorage.getItem('connectedUser')
    })
        .then(response => {
            console.log('Logout response:', response);
            if (!response.ok) {
                throw new Error('Logout failed');
            }
            console.log('Logout successful');
            localStorage.removeItem('connectedUser');
            window.location.href = "login.html"; // Redirect to login page after successful logout
        })
        .catch(error => {
            console.error('Logout error:', error); // Log any errors during logout
        });
}

const logoutBtn = document.getElementById("logoutBtn");
logoutBtn.addEventListener("click", handleLogout);

function handleNewMeeting() {
    const connectedUser = JSON.parse(localStorage.getItem('connectedUser'));
    window.open(`videocall.html?username=${connectedUser.username}`, "_blank");
}

const newMeetingBtn = document.getElementById("newMeetingBtn");
newMeetingBtn.addEventListener("click", handleNewMeeting);

function handleJoinMeeting() {
    const roomId = document.getElementById("meetingName").value;
    const connectedUser = JSON.parse(localStorage.getItem('connectedUser'));

    const url = `videocall.html?roomID=${roomId}&username=${connectedUser.username}`;

    window.open(url, "_blank");
}

const joinMeetingBtn = document.getElementById("joinMeetingBtn");
joinMeetingBtn.addEventListener("click", handleJoinMeeting);
