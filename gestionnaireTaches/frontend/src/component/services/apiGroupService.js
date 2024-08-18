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

export const createTaskGroup = async (taskGroupData) => {
    return await fetch('http://localhost:8080/api/group/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskGroupData),
    });
};

export const findGroupById = async (groupIds) => {
    try {
        const encodedGroupIds = encodeURIComponent(JSON.stringify(groupIds));
        const response = await fetch(`http://localhost:8080/api/group/findGroupById?groupIds=${encodedGroupIds}`);
        if (response.ok) {
            return await response.json();
        } else {
            throw new Error('Failed to fetch task groups');
        }
    } catch (error) {
        console.error('Fetch error:', error);
        throw new Error('Error finding group by ID');
    }
};

export const addTaskToGroup = async (taskData) => {
    return await fetch('http://localhost:8080/api/group/addTask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
    });
};