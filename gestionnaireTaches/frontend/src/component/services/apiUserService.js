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