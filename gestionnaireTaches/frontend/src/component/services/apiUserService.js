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

export const loginUser = async (username, password) => {
    try {
        const response = await fetch('http://localhost:8080/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            return JSON.parse(await response.text());
        } else {
            const errorData = await response.json();
            throw new Error(errorData.message);
        }
    } catch (error) {
        console.error('Error logging in:', error);
        throw new Error('Error logging in');
    }
};

export const signUpUser = async ({ username, password }) => {
    const userData = { username, password, tasks: [] };

    try {
        const response = await fetch('http://localhost:8080/api/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData),
        });

        if (response.status === 201) {
            const createdUserDto = await response.json();
            localStorage.setItem('accountInfos', JSON.stringify({
                userId: createdUserDto.id,
                username: createdUserDto.username,
                groups: []
            }));
            return createdUserDto;
        } else {
            const errorData = await response.json();
            throw new Error(errorData.message);
        }
    } catch (error) {
        console.error('Error creating user:', error);
        throw new Error('Error creating user');
    }
};