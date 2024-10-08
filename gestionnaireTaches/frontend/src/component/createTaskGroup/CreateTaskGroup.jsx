import React, { useState } from 'react';
import './CreateTaskGroup.css';
import {createTaskGroup} from "../services/apiGroupService";

const CreateTaskGroup = () => {
    const [title, setTitle] = useState('');
    const [message, setMessage] = useState('');
    const userId = JSON.parse(localStorage.getItem('accountInfos')).userId;

    const handleSubmit = async (e) => {
        e.preventDefault();

        const taskGroupData = {
            title,
            userId
        };

        try {
            const response = await createTaskGroup(taskGroupData);

            if (response && response.id) {
                setMessage('Task group created successfully');
                setTitle('');
                const storedUserInfo = JSON.parse(localStorage.getItem('accountInfos'));
                const currentGroups = storedUserInfo.groups || [];
                const updatedGroups = [...currentGroups, response.id];
                localStorage.setItem('accountInfos', JSON.stringify({
                    userId: userId,
                    username: storedUserInfo.username,
                    groups: updatedGroups
                }));
            } else {
                setMessage('Unknown error occurred');
            }
        } catch (error) {
            console.error('Error creating task group:', error);
            setMessage('Error creating task group');
        }
    };

    return (
        <div className="container">
            <h2>Create Task Group</h2>
            <form onSubmit={handleSubmit} className="form">
                <div className="form-group">
                    <label className="label">Title:</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                        className="input"
                    />
                </div>
                <button type="submit" className="button">Create Task Group</button>
            </form>
            {message && <p className={`message ${message.includes('Error') ? 'error' : ''}`}>{message}</p>}
        </div>
    );
};

export default CreateTaskGroup;
