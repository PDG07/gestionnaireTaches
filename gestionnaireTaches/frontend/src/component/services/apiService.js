export const findUserByUsername = async (username) => {
    try {
        const response = await fetch(`http://localhost:8080/api/user/findUserByUsername?username=${encodeURIComponent(username)}`);
        if (response.ok) {
            const userData = await response.json();
            return userData.id;
        } else {
            throw new Error('User not found');
        }
    } catch (error) {
        console.error('Fetch error:', error);
        throw new Error('Error finding user by username');
    }
};

export const findGroupByTitle = async (title) => {
    try {
        const response = await fetch(`http://localhost:8080/api/group/findGroupByTitle?title=${encodeURIComponent(title)}`);
        if (response.ok) {
            const groupData = await response.json();
            return groupData.id;
        } else {
            throw new Error('Group not found');
        }
    } catch (error) {
        console.error('Fetch error:', error);
        throw new Error('Error finding group by title');
    }
};

export const addUserToGroup = async (taskGroupData) => {
    try {
        const response = await fetch('http://localhost:8080/api/group/addUserToGroup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(taskGroupData),
        });

        if (response.ok) {
            return 'User added to group successfully.';
        } else {
            throw new Error('User already exists in the group');
        }
    } catch (error) {
        throw new Error(error.message);
    }
};

export const fetchCompletedTasks = async (userId) => {
    try {
        const response = await fetch(`http://localhost:8080/api/completedtasks?userId=${userId}`);
        if (!response.ok) {
            throw new Error('Failed to fetch completed tasks');
        }
        return await response.json();
    } catch (error) {
        throw new Error(error.message);
    }
};