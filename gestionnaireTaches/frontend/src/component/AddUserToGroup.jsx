import React, { useState } from 'react';

const AddUserToGroup = () => {
    const [username, setUsername] = useState('');
    const [groupId, setGroupId] = useState('');
    const [message, setMessage] = useState('');

    const findUserByUsername = async (username) => {
        try {
            const response = await fetch(`http://localhost:8080/api/user/findUserByUsername?username=${encodeURIComponent(username)}`);
            if (response.ok) {
                const userData = await response.json();
                return userData.id;  // Assuming the UserDto has an id field
            } else {
                throw new Error('User not found');
            }
        } catch (error) {
            console.error('Fetch error:', error);
            throw new Error('Error finding user by username');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const userId = await findUserByUsername(username);
            const taskGroupData = {
                groupId: parseInt(groupId, 10),
                userId: userId,
                title: '',
            };

            const response = await fetch('http://localhost:8080/api/group/addUserToGroup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(taskGroupData),
            });

            if (response.ok) {
                const data = await response.json();
                setMessage(`User added to group successfully.`);
            } else {
                const errorData = await response.json().catch(() => null);
                setMessage(`Error: ${errorData ? errorData.message : 'Unknown error'}`);
            }
        } catch (error) {
            setMessage(`Error: ${error.message}`);
        }
    };

    return (
        <div className="container">
            <h2>Add User to Group</h2>
            <form onSubmit={handleSubmit} className="form">
                <div>
                    <label className="label">Username:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        className="input"
                    />
                </div>
                <div>
                    <label className="label">Group ID:</label>
                    <input
                        type="text"
                        value={groupId}
                        onChange={(e) => setGroupId(e.target.value)}
                        required
                        className="input"
                    />
                </div>
                <button type="submit" className="button">Add User to Group</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default AddUserToGroup;
