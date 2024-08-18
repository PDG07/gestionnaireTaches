import React, { useState } from 'react';
import './AddUserToGroup.css';
import { findUserByUsername, findGroupByTitle, addUserToGroup } from '../services/apiService';

const AddUserToGroup = () => {
    const [username, setUsername] = useState('');
    const [groupTitle, setGroupTitle] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const userId = await findUserByUsername(username);
            const groupId = await findGroupByTitle(groupTitle);
            const taskGroupData = {
                groupId: groupId,
                userId: userId,
                title: groupTitle,
            };

            const successMessage = await addUserToGroup(taskGroupData);
            setMessage(successMessage);
        } catch (error) {
            setMessage(`Error: ${error.message}`);
        }
    };

    return (
        <div className="container">
            <h2>Add User to Group</h2>
            <form onSubmit={handleSubmit} className="form">
                <div className="form-group">
                    <label className="label">Username:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        className="input"
                    />
                </div>
                <div className="form-group">
                    <label className="label">Group Title:</label>
                    <input
                        type="text"
                        value={groupTitle}
                        onChange={(e) => setGroupTitle(e.target.value)}
                        required
                        className="input"
                    />
                </div>
                <button type="submit" className="button">Add User to Group</button>
            </form>
            {message && <p className={`message ${message.includes('Error') ? 'error' : ''}`}>{message}</p>}
        </div>
    );
};

export default AddUserToGroup;
