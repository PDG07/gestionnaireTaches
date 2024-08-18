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

            if (response.status === 201) {
                setMessage('Task group created successfully');
                setTitle('');
                let rep = await response.json();
                const storedUserInfo = JSON.parse(localStorage.getItem('accountInfos'));
                const currentGroups = storedUserInfo.groups || [];
                const updatedGroups = [...currentGroups, rep.id];
                localStorage.setItem('accountInfos', JSON.stringify({ userId: userId, username: JSON.parse(localStorage.getItem('accountInfos')).username, groups: updatedGroups }));
            } else {
                const errorData = await response.json();
                setMessage(`Error: ${errorData.message}`);
            }
        } catch (error) {
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
