const BASE_URL = 'http://localhost:8080';

const apiRequest = async (endpoint, options = {}) => {
    try {
        const response = await fetch(`${BASE_URL}${endpoint}`, options);

        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData ? errorData.message : 'Request failed');
        }

        if (response.status !== 204) {
            return await response.json();
        }

        return response.ok;
    } catch (error) {
        console.error('API request error:', error);
        throw new Error('API request failed');
    }
};

export const findGroupByTitle = async (title) => {
    return await apiRequest(`/api/group/findGroupByTitle?title=${encodeURIComponent(title)}`);
};

export const addUserToGroup = async (taskGroupData) => {
    return await apiRequest('/api/group/addUserToGroup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskGroupData),
    });
};

export const createTaskGroup = async (taskGroupData) => {
    return await apiRequest('/api/group/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskGroupData),
    });
};

export const findGroupById = async (groupIds) => {
    const encodedGroupIds = encodeURIComponent(JSON.stringify(groupIds));
    return await apiRequest(`/api/group/findGroupById?groupIds=${encodedGroupIds}`);
};

export const addTaskToGroup = async (taskData) => {
    return await apiRequest('/api/group/addTask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
    });
};

export const fetchGroups = async (userId) => {
    return await apiRequest(`/api/group/getGroupsFromUserId?userId=${userId}`);
};

export const fetchTasks = async (groupId) => {
    const tasks = await apiRequest(`/api/group/getTasksOfGroup?groupId=${groupId}`);
    return tasks.filter(task => task.status !== 'COMPLETED');
};

export const fetchUsers = async (groupId) => {
    return await apiRequest(`/api/user/findAllUserFromGroup?groupId=${groupId}`);
};

export const completeTask = async (taskId, groupId) => {
    const taskData = { groupId, id: taskId };
    return await apiRequest('/api/group/completeTaskFromGroup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
    });
};

export const assignTask = async (taskId, userId, groupId) => {
    const taskData = { groupId, id: taskId, userId };
    return await apiRequest('/api/group/assignTaskForGrTo', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
    });
};

export const filterTasksByCategory = async (groupId, category) => {
    return await apiRequest('/api/group/filterByCategoryGroup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ groupId, category }),
    });
};
