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

export const fetchCompletedTasks = async (userId) => {
    return await apiRequest(`/api/completedtasks?userId=${userId}`);
};

export const createTask = async (taskData) => {
    return await apiRequest('/api/createtask', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
    });
};

export const fetchTasks = async (userId) => {
    return await apiRequest(`/api/tasks?userId=${userId}`);
};

export const fetchTasksByCategory = async (userId, category) => {
    return await apiRequest(`/api/tasks/filter?userId=${userId}&category=${encodeURIComponent(category)}`);
};

export const completeTask = async (taskId, userId) => {
    return await apiRequest(`/api/completetask/${taskId}?userId=${userId}`, {
        method: 'PUT',
    });
};

export const updateTask = async (taskData) => {
    return await apiRequest('/api/updatetask', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData),
    });
};
