const BASE_URL = 'http://localhost:8080';

const apiRequest = async (endpoint, options = {}) => {
    try {
        const response = await fetch(`${BASE_URL}${endpoint}`, options);

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Request failed');
        }

        if (response.status !== 204) {
            return await response.json();
        }
    } catch (error) {
        console.error('API request error:', error);
        throw new Error('API request failed');
    }
};

export const findUserByUsername = async (username) => {
    const userData = await apiRequest(`/api/user/findUserByUsername?username=${encodeURIComponent(username)}`);
    return userData.id;
};

export const loginUser = async (username, password) => {
    return await apiRequest('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
    });
};

export const signUpUser = async ({ username, password }) => {
    const userData = { username, password, tasks: [] };

    const createdUserDto = await apiRequest('/api/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
    });

    localStorage.setItem('accountInfos', JSON.stringify({
        userId: createdUserDto.id,
        username: createdUserDto.username,
        groups: [],
    }));

    return createdUserDto;
};
