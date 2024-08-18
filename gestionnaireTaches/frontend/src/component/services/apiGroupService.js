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