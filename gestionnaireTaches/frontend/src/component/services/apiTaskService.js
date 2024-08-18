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

export const createTask = async (taskData) => {
    try {
        const response = await fetch('http://localhost:8080/api/createtask', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(taskData),
        });

        if (response.status === 201) {
            return 'Task created successfully';
        } else if (response.status === 403) {
            throw new Error('Access denied: You do not have permission to create a task.');
        } else {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData ? errorData.message : 'Unknown error');
        }
    } catch (error) {
        throw new Error(error.message);
    }
};