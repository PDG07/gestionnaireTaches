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

export const fetchGroups = async (userId) => {
    const response = await fetch(`http://localhost:8080/api/group/getGroupsFromUserId?userId=${userId}`);
    if (!response.ok) {
        throw new Error('Failed to fetch groups');
    }
    return await response.json();
};

export const fetchTasks = async (groupId) => {
    const response = await fetch(`http://localhost:8080/api/group/getTasksOfGroup?groupId=${groupId}`);
    if (!response.ok) {
        throw new Error('Failed to fetch tasks');
    }
    const tasks = await response.json();
    return tasks.filter(task => task.status !== 'COMPLETED');
};

export const fetchUsers = async (groupId) => {
    const response = await fetch(`http://localhost:8080/api/user/findAllUserFromGroup?groupId=${groupId}`);
    if (!response.ok) {
        throw new Error('Failed to fetch users');
    }
    return await response.json();
};

export const completeTask = async (taskId, groupId) => {
    const taskData = { groupId, id: taskId };
    const response = await fetch('http://localhost:8080/api/group/completeTaskFromGroup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
    });
    if (!response.ok) {
        throw new Error('Failed to complete task');
    }
};

export const assignTask = async (taskId, userId, groupId) => {
    const taskData = { groupId, id: taskId, userId };
    const response = await fetch('http://localhost:8080/api/group/assignTaskForGrTo', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
    });
    if (!response.ok) {
        throw new Error('Failed to assign task');
    }
};

export const filterTasksByCategory = async (groupId, category) => {
    const response = await fetch('http://localhost:8080/api/group/filterByCategoryGroup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ groupId, category }),
    });
    if (!response.ok) {
        throw new Error('Failed to filter tasks');
    }
    return await response.json();
};