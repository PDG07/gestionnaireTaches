import React, { useState } from 'react';

const CreateTask = () => {
    const [userId, setUserId] = useState('');
    const [taskName, setTaskName] = useState('');
    const [taskDescription, setTaskDescription] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        const taskData = {
            userId: userId,
            taskName: taskName,
            taskDescription: taskDescription
        };

        try {
            const response = await fetch('http://localhost:8080/api/createtask', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(taskData),
            });
            console.log('Response status:', response.status);
            if (response.status === 201) {
                setMessage('Task created successfully');
            } else {
                const errorData = await response.json();
                setMessage(`Error: ${errorData.message}`);
            }
        } catch (error) {
            console.error('Fetch error:', error);
            setMessage('Error creating task');
        }
    };

    return (
        <div>
            <h2>Create Task</h2>
            <form onSubmit={handleSubmit} noValidate>
                <div>
                    <label>User ID:</label>
                    <input
                        type="text"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Task Name:</label>
                    <input
                        type="text"
                        value={taskName}
                        onChange={(e) => setTaskName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Task Description:</label>
                    <input
                        type="text"
                        value={taskDescription}
                        onChange={(e) => setTaskDescription(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Create Task</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default CreateTask;
