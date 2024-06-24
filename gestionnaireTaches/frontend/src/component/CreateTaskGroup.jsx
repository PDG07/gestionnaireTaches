import React, { useState } from 'react';

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
            const response = await fetch('http://localhost:8080/api/group/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(taskGroupData),
            });

            if (response.status === 201) {
                setMessage('Task group created successfully');
                setTitle(''); // Clear the input field after successful creation
            } else {
                const errorData = await response.json();
                setMessage(`Error: ${errorData.message}`);
            }
        } catch (error) {
            setMessage('Error creating task group');
        }
    };

    return (
        <div>
            <h2>Create Task Group</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Title:</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Create Task Group</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default CreateTaskGroup;
