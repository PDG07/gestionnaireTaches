import React, { useState } from 'react';

const CreateTask = () => {
    const [userId, setUserId] = useState('');
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [status, setStatus] = useState('');
    const [priority, setPriority] = useState('');
    const [deadline, setDeadline] = useState('');
    const [completionDate, setCompletionDate] = useState('');
    const [category, setCategory] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        const taskData = {
            userId: userId,
            title: title,
            description: description,
            status: status,
            priority: priority,
            deadline: deadline,
            completionDate: completionDate,
            category: category
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
            } else if (response.status === 403) {
                setMessage('Access denied: You do not have permission to create a task.');
            } else {
                const errorData = await response.json().catch(() => null);
                setMessage(`Error: ${errorData ? errorData.message : 'Unknown error'}`);
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
                    <label>Title:</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Description:</label>
                    <input
                        type="text"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Status:</label>
                    <select value={status} onChange={(e) => setStatus(e.target.value)}>
                        <option value="">Select status</option>
                        <option value="COMPLETED">Completed</option>
                        <option value="TODO">Todo</option>
                    </select>
                </div>
                <div>
                    <label>Priority:</label>
                    <select value={priority} onChange={(e) => setPriority(e.target.value)}>
                        <option value="">Select priority</option>
                        <option value="HIGH">High</option>
                        <option value="AVERAGE">Average</option>
                        <option value="LOW">Low</option>
                    </select>
                </div>
                <div>
                    <label>Deadline:</label>
                    <input
                        type="date"
                        value={deadline}
                        onChange={(e) => setDeadline(e.target.value)}
                    />
                </div>
                <div>
                    <label>Completion Date:</label>
                    <input
                        type="date"
                        value={completionDate}
                        onChange={(e) => setCompletionDate(e.target.value)}
                    />
                </div>
                <div>
                    <label>Category:</label>
                    <select value={category} onChange={(e) => setCategory(e.target.value)}>
                        <option value="">Select category</option>
                        <option value="WORK">Work</option>
                        <option value="PERSONAL">Personal</option>
                        <option value="SHOPPING">Shopping</option>
                        <option value="SPORTS">Sports</option>
                        <option value="OTHER">Other</option>
                    </select>
                </div>
                <button type="submit">Create Task</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default CreateTask;
